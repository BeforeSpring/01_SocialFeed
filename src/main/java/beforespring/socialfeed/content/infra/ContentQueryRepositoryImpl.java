package beforespring.socialfeed.content.infra;

import static beforespring.socialfeed.content.domain.QContent.content1;
import static beforespring.socialfeed.content.domain.QHashtagContent.hashtagContent;

import beforespring.socialfeed.content.domain.Content;
import beforespring.socialfeed.content.domain.ContentQueryParameter;
import beforespring.socialfeed.content.domain.ContentQueryRepository;
import beforespring.socialfeed.content.domain.ContentQueryResult;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
     * @param hashtag 불러올 hashtag
     * @param size 불러올 content 숫자. 더 불러올 content가 존재하는지 판단하기위해 +1만큼 더 불러옴.
     * @param offset 쿼리 시작지점에서 offset만큼 건너 뛰고 조회함.
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
}
