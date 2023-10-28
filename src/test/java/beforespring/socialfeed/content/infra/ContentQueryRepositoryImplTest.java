package beforespring.socialfeed.content.infra;

import static beforespring.Fixture.aContent;
import static beforespring.Fixture.randString;
import static beforespring.Fixture.random;
import static beforespring.Fixture.randomPositiveLong;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import beforespring.Fixture;
import beforespring.socialfeed.content.domain.Content;
import beforespring.socialfeed.content.domain.ContentQueryParameter;
import beforespring.socialfeed.content.domain.HashtagContent;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

// todo 테스트 케이스 보완 (한 content에 여러 hashtag가 설정된 경우 등)
@DataJpaTest
@ExtendWith(MockitoExtension.class)
class ContentQueryRepositoryImplTest {

    @PersistenceContext
    private EntityManager em;

    private ContentQueryRepositoryImpl contentQueryRepository;

    List<HashtagContent> mapToHashtagContent(List<Content> contents, String hashtag) {
        return contents.stream()
                   .map(content -> HashtagContent.builder()
                                       .content(content)
                                       .createdAt(content.getCreatedAt())
                                       .hashtag(hashtag)
                                       .build())
                   .toList();
    }

    /**
     * @param hashtag     content에 반드시 포함되는 해시태그
     * @param howManySets howManySets 만큼 현재 시각에 동시 생성된 content, 과거에 생성된 content 각각 생성.
     */
    private void initDummyData(String hashtag, int howManySets) {
        LocalDateTime now = LocalDateTime.now();
        List<Content> createdAtTheSameTime = Stream.generate(() -> Fixture.aContent()
                                                                       .createdAt(now)
                                                                       .updatedAt(now)
                                                                       .build())
                                                 .limit(howManySets)
                                                 .toList();
        List<Content> createdAtRandomPast = Stream.generate(() -> Fixture.aContent()
                                                                      .build())
                                                .limit(howManySets)
                                                .toList();
        createdAtTheSameTime.forEach(em::persist);
        createdAtRandomPast.forEach(em::persist);
        em.flush();

        List<HashtagContent> createdAtTheSameTimeHC = mapToHashtagContent(createdAtTheSameTime,
            hashtag);
        List<HashtagContent> createdAtRandomPastHC = mapToHashtagContent(createdAtRandomPast,
            hashtag);

        createdAtTheSameTimeHC.forEach(em::persist);
        createdAtRandomPastHC.forEach(em::persist);
        em.flush();
    }

    @Mock
    ContentQueryMapper mockContentQueryMapper;

    @BeforeEach
    void init() {
        contentQueryRepository = new ContentQueryRepositoryImpl(em, mockContentQueryMapper);
    }

    @Captor
    ArgumentCaptor<List<Content>> listContentArgumentCaptor;

    @Test
    @DisplayName("ContentQueryMapper에 인자를 잘 넘겼는지 확인.")
    void findByHashtag_calling_mapper_test() {
        // given
        String givenHashtag = randString();
        int givenContentSets = 10;  // 2배만큼 생성됨. (10개 현재 시점에 동시 생성, 10개 랜덤한 과거에 생성)
        int givenSize = 5;
        initDummyData(givenHashtag, givenContentSets);  // givenHashtag로 생성
        ContentQueryParameter givenParam = ContentQueryParameter.builder()
                                               .size(givenSize)  // 10개 가져오기
                                               .from(null)
                                               .offset(0)
                                               .hashtag(givenHashtag)
                                               .build();

        List<Content> expectedQueryResult = contentQueryRepository.findContentsByQueryParam(
            givenParam);  // 매핑되기 전 쿼리 결과물

        // when
        contentQueryRepository.findByHashtag(givenParam);

        // then
        verify(mockContentQueryMapper).mapToContentQueryResult(listContentArgumentCaptor.capture(),
            eq(givenParam.size()));
        assertThat(listContentArgumentCaptor.getValue())
            .describedAs("매퍼 객체에 결과물을 잘 넘겼는지 확인.")
            .containsExactlyElementsOf(expectedQueryResult);

    }

    @Test
    void findContentsByQueryParam() {
        // given
        String givenHashtag = randString();
        int givenContentSets = 10;  // 2배만큼 생성됨. (10개 현재 시점에 동시 생성, 10개 랜덤한 과거에 생성)
        initDummyData(givenHashtag, givenContentSets);  // givenHashtag로 생성함.
        initDummyData(randString(),
            givenContentSets);  // 무작위 생성된 hashtag로 생성함. (hashtag 일치하는 content만 가져오는 것인지 검증하기 위해 생성함)

        int givenQuerySize = givenContentSets;  // 1개 더 가져오는지 테스트하기 위해서 사용됨.

        // offset 설정되지 않은 파라미터
        ContentQueryParameter noOffsetParam = ContentQueryParameter.builder()
                                                  .size(givenQuerySize)
                                                  .from(null)
                                                  .offset(0)
                                                  .hashtag(givenHashtag)
                                                  .build();
        // offset이 설정된 파라미터
        ContentQueryParameter halfOffsetParam = ContentQueryParameter.builder()
                                                    .size(givenQuerySize)
                                                    .from(null)
                                                    .offset(givenQuerySize)
                                                    .hashtag(givenHashtag)
                                                    .build();
        // offset이 설정되지 않았고, 생성된 총 content보다 더 많은 양의 정보를 불러오는 파라미터
        ContentQueryParameter noOffsetLargeSizeParam = ContentQueryParameter.builder()
                                                           .size(givenQuerySize * 4)
                                                           .from(null)
                                                           .offset(0)
                                                           .hashtag(givenHashtag)
                                                           .build();

        // when
        List<Content> noOffsetResult = contentQueryRepository.findContentsByQueryParam(
            noOffsetParam);
        List<Content> halfOffsetResult = contentQueryRepository.findContentsByQueryParam(
            halfOffsetParam);
        List<Content> noOffsetLargeSizeResult = contentQueryRepository.findContentsByQueryParam(
            noOffsetLargeSizeParam);

        // then
        assertThat(noOffsetResult)
            .describedAs("시간 내림차순 정렬.")
            .isSortedAccordingTo(Comparator.comparing(Content::getCreatedAt).reversed())
            .describedAs("요청 개수보다 1개 더 찾아와야함.")
            .hasSize(givenQuerySize + 1);

        assertThat(halfOffsetResult)
            .describedAs("시간 내림차순 정렬.")
            .isSortedAccordingTo(Comparator.comparing(Content::getCreatedAt).reversed())
            .describedAs("offset이 1/2만큼 설정되었기 때문에 더 가져올 것이 없음.")
            .hasSize(givenQuerySize);

        assertThat(noOffsetResult.get(noOffsetResult.size() - 1).getId())
            .describedAs(
                "offset이 쿼리 사이즈와 동일하게 설정되었기 때문에, halfOffsetResult의 첫번째 element는 noOffsetResult의 첫번째 element와 같음.")
            .isEqualTo(halfOffsetResult.get(0).getId());

        assertThat(noOffsetLargeSizeResult)
            .describedAs("시간 내림차순 정렬.")
            .isSortedAccordingTo(Comparator.comparing(Content::getCreatedAt).reversed())
            .describedAs(
                "givenHashtag에 해당하는 정보보다 더 많은 size로 요청했을 때, 총 size가 일치하지 않는 경우 다른 해시태그를 불러왔거나 불러와야할 일부 검색결과를 불러오지 않았다는 의미임.")
            .hasSize(givenContentSets * 2);
    }

    /**
     * <ol>
     *     <li>최근(현재 - 10분 ~ 현재 - givenTime분) 사이의 데이터 생성</li>
     *     <li>쿼리 대상에 포함되지 않는 과거(현재 - givenTime분 ~ 현재 - 100만분) 사이의 데이터 생성</li>
     * </ol>
     * @param now 현재 시각
     * @param givenTime 위 설명 참조
     * @param hashtagCntBase 생성될 해시태그의 총 숫자
     * @param recentCnt 생성될 최근 데이터의 수 (과거 데이터는 hashtagCntBase - recentCnt 만큼 생성됨.)
     * @param hashtag 해시태그
     */
    void mostPopularHashtags_init(
        LocalDateTime now,
        long givenTime,
        int hashtagCntBase,
        int recentCnt,
        String hashtag
    ) {
        // 기준 시간 이내 태그된 해시태그 생성
        Stream.generate(() -> HashtagContent.builder()
                                  .hashtag(hashtag)
                                  .content(aContent().id(randomPositiveLong()).build())
                                  .createdAt(now.minusMinutes(random.nextLong(10, givenTime)))
                                  .build())
            .limit(recentCnt)
            .forEach(em::persist);

        // 기준 시간 이전 태그된 해시태그 생성
        Stream.generate(() -> HashtagContent.builder()
                                  .hashtag(hashtag)
                                  .content(aContent().id(randomPositiveLong()).build())
                                  .createdAt(now.minusMinutes(random.nextLong(givenTime, 1000000)))
                                  .build())
            .limit(hashtagCntBase - recentCnt)
            .forEach(em::persist);
    }

    /**
     * 최근 3시간 이내 상위 3개의 해시태그를 찾는 테스트 A: 3시간 이내 10개, 그 이전 30개. B: 3시간 이내 20개, 그 이전 20개. ... D: 3시간 이내
     * 40개, 그 이전 40개.
     */
    @Test
    void findMostPopularHashtagsIn() {
        LocalDateTime now = LocalDateTime.now();
        long givenTime = 180;
        int hashtagCntBase = 40;

        String hashtagA = randString() + "hashtagA";
        int hashtagACnt = 10;

        String hashtagB = randString() + "hashtagB";
        int hashtagBCnt = 20;

        String hashtagC = randString() + "hashtagC";
        int hashtagCCnt = 30;

        String hashtagD = randString() + "hashtagD";
        int hashtagDCnt = 40;

        mostPopularHashtags_init(now, givenTime, hashtagCntBase, hashtagACnt, hashtagA);
        mostPopularHashtags_init(now, givenTime, hashtagCntBase, hashtagBCnt, hashtagB);
        mostPopularHashtags_init(now, givenTime, hashtagCntBase, hashtagCCnt, hashtagC);
        mostPopularHashtags_init(now, givenTime, hashtagCntBase, hashtagDCnt, hashtagD);

        List<String> mostPopularHashtagsIn = contentQueryRepository.findMostPopularHashtagsIn(180, 3);

        assertThat(mostPopularHashtagsIn)
            .describedAs("총 3개만 불러와야함.")
            .hasSize(3)
            .describedAs("태그가 많이 된 순서대로 정렬돼야함.")
            .containsExactlyElementsOf(List.of(hashtagD, hashtagC, hashtagB));
    }

    /**
     * 시간 내에 해당되는 결과가 없을 때 테스트
     */
    @Test
    void findMostPopularHashtagsIn_empty_result() {
        LocalDateTime now = LocalDateTime.now();
        long givenTime = 180;
        int hashtagCntBase = 40;

        String hashtagA = randString() + "hashtagA";
        int hashtagACnt = 10;

        String hashtagB = randString() + "hashtagB";
        int hashtagBCnt = 20;

        mostPopularHashtags_init(now, givenTime, hashtagCntBase, hashtagACnt, hashtagA);
        mostPopularHashtags_init(now, givenTime, hashtagCntBase, hashtagBCnt, hashtagB);

        List<String> mostPopularHashtagsIn = contentQueryRepository.findMostPopularHashtagsIn(5, 3);  // 5분 내의 데이터는 생성되지 않음.

        assertThat(mostPopularHashtagsIn)
            .describedAs("null이 아니고, 비어있어야함.")
            .isNotNull()
            .isEmpty();
    }
}