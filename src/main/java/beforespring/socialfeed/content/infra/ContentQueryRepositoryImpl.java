package beforespring.socialfeed.content.infra;

import static beforespring.socialfeed.content.domain.QContent.content1;
import static beforespring.socialfeed.content.domain.QHashtagContent.hashtagContent;

import beforespring.socialfeed.content.domain.Content;
import beforespring.socialfeed.content.domain.ContentQueryParameter;
import beforespring.socialfeed.content.domain.ContentQueryRepository;
import beforespring.socialfeed.content.domain.ContentQueryResult;
import beforespring.socialfeed.content.domain.query.ContentStatisticDataUnit;
import beforespring.socialfeed.content.domain.query.ContentStatisticsData;
import beforespring.socialfeed.content.domain.query.ContentStatisticsQueryParameter;
import beforespring.socialfeed.content.domain.query.CountValue;
import beforespring.socialfeed.content.domain.query.Granularity;
import beforespring.socialfeed.content.domain.query.QContentStatisticDataUnit;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.NullExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class ContentQueryRepositoryImpl implements ContentQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final ContentQueryMapper contentQueryMapper;


    public ContentQueryRepositoryImpl(
        EntityManager em,
        ContentQueryMapper contentQueryMapper
    ) {
        this.queryFactory = new JPAQueryFactory(em);
        this.contentQueryMapper = contentQueryMapper;
    }

    @Override
    public ContentQueryResult<Content> findByHashtag(ContentQueryParameter queryParameter) {
        return contentQueryMapper
                   .mapToContentQueryResult(
                       findContentsByQueryParam(queryParameter),
                       queryParameter.size()
                   );
    }

    @Override
    public List<String> findMostPopularHashtagsIn(long minutes, int howMany) {
        LocalDateTime now = LocalDateTime.now();
        return queryFactory
                   .select(hashtagContent.hashtag)
                   .from(hashtagContent)
                   .groupBy(hashtagContent.hashtag)
                   .where(hashtagContent.createdAt.between(now.minusMinutes(minutes), now))
                   .orderBy(hashtagContent.hashtag.count().desc())
                   .limit(howMany)
                   .fetch()
            ;
    }


    List<Content> findContentsByQueryParam(ContentQueryParameter queryParameter) {
        BooleanExpression condition =
            queryParameter.from() == null ? null
                : createdBefore(queryParameter.from());

        return findContents(
            queryParameter.hashtag(),
            queryParameter.size(),
            queryParameter.offset(),
            condition);
    }

    private static BooleanExpression createdBefore(LocalDateTime from) {
        return hashtagContent.createdAt.before(from);
    }

    private static BooleanExpression hashtagMatches(String hashtag) {
        return hashtagContent.hashtag.eq(hashtag);
    }

    /**
     * @param hashtag   불러올 hashtag
     * @param size      불러올 content 숫자. 더 불러올 content가 존재하는지 판단하기위해 +1만큼 더 불러옴.
     * @param offset    쿼리 시작지점에서 offset만큼 건너 뛰고 조회함.
     * @param condition 커스텀 가능한 where 조건문. And 조건으로 작동함.
     * @return List of Content
     */
    private List<Content> findContents(
        String hashtag,
        int size,
        int offset,
        Predicate condition
    ) {
        return queryFactory
                   .select(content1)  // Content 가져와야함.
                   .from(hashtagContent)  // 드라이빙 테이블
                   .join(content1)
                   .on(hashtagContent.content.eq(content1))  // content
                   .where(hashtagMatches(hashtag)
                              .and(condition))  // condition은 null일 수 있음.
                   .orderBy(
                       hashtagContent.createdAt.desc())  // hashtagContent 최신순
                   .offset(offset)  // 해당 시간에 올라온 게시물이 여럿 존재하는 경우 사용됨.
                   .limit(size + 1)  // 요청된 size보다 하나 더 불러옴.
                   .fetch();
    }

    static private final NumberExpression<Integer> year = hashtagContent.createdAt.year();
    static private final NumberExpression<Integer> month = hashtagContent.createdAt.month();
    static private final NumberExpression<Integer> dayOfMonth = hashtagContent.createdAt.dayOfMonth();
    static private final NumberExpression<Integer> hour = hashtagContent.createdAt.hour();

    static private final OrderSpecifier[] orderByYearMonthDayHour = {
        year.desc(),
        month.desc(),
        dayOfMonth.desc(),
        hour.desc()
    };

    static private final OrderSpecifier[] orderByYearMonthDay = {
        year.desc(),
        month.desc(),
        dayOfMonth.desc()
    };

    static private final Expression[] groupByYearMonthDayHour = {
        year,
        month,
        dayOfMonth,
        hour
    };

    static private final Expression[] groupByYearMonthDay = {
        year,
        month,
        dayOfMonth
    };


    @Override
    public ContentStatisticsData findStatisticData(ContentStatisticsQueryParameter parameter) {
        List<ContentStatisticDataUnit> queryResult = statisticQuery(
            getSelectExpression(parameter),
            getGroupByExpression(parameter),
            getOrderSpecifier(parameter),
            parameter.hashtag(),
            parameter.start(),
            parameter.end()
        );
        return new ContentStatisticsData(parameter, queryResult.size(), queryResult);
    }

    QContentStatisticDataUnit getSelectExpression(ContentStatisticsQueryParameter parameter) {
        return new QContentStatisticDataUnit(
            year,
            month,
            dayOfMonth,
            parameter.granularity() == Granularity.HOURLY ? hour : Expressions.asNumber(-1),
            getCountValueExpression(parameter.countValue())
        );
    }

    Expression[] getGroupByExpression(ContentStatisticsQueryParameter parameter) {
        return switch (parameter.granularity()) {
            case HOURLY -> groupByYearMonthDayHour;
            case DAILY -> groupByYearMonthDay;
        };
    }

    OrderSpecifier[] getOrderSpecifier(ContentStatisticsQueryParameter parameter) {
        return switch (parameter.granularity()) {
            case HOURLY -> orderByYearMonthDayHour;
            case DAILY -> orderByYearMonthDay;
        };
    }

    List<ContentStatisticDataUnit> statisticQuery(
        QContentStatisticDataUnit selectExpression,
        Expression[] groupByExpression,
        OrderSpecifier[] orderSpecifiers,
        String hashtag,
        LocalDate start,
        LocalDate end
    ) {
        return queryFactory
                   .select(selectExpression)
                   .from(hashtagContent)
                   .join(content1).on(hashtagContent.content.eq(content1))
                   .where(
                       hashtagMatches(hashtag),
                       createdBetween(start, end)
                   )
                   .groupBy(groupByExpression)
                   .orderBy(orderSpecifiers)
                   .fetch();
    }

    private static BooleanExpression createdBetween(LocalDate start, LocalDate end) {
        return hashtagContent.createdAt.between(
            start.atStartOfDay(),
            end.plusDays(1).atStartOfDay());
    }

    private static NumberExpression<Long> getCountValueExpression(CountValue countValue) {
        return countValue == null ? count()
                   : switch (countValue) {
                       case COUNT -> count();
                       case LIKE_COUNT -> likeCount();
                       case VIEW_COUNT -> viewCount();
                       case SHARE_COUNT -> shareCount();
                   };
    }

    private static NumberExpression<Long> count() {
        return hashtagContent.count();
    }

    private static NumberExpression<Long> viewCount() {
        return content1.viewCount.sum();
    }

    private static NumberExpression<Long> likeCount() {
        return content1.likeCount.sum();
    }

    private static NumberExpression<Long> shareCount() {
        return content1.shareCount.sum();
    }

}
