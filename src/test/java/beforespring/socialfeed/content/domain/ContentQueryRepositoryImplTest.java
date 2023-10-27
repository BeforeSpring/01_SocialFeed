package beforespring.socialfeed.content.domain;

import static beforespring.Fixture.randString;
import static org.assertj.core.api.Assertions.assertThat;

import beforespring.Fixture;
import beforespring.socialfeed.content.infra.ContentQueryRepositoryImpl;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

// todo 테스트 케이스 보완 (한 content에 여러 hashtag가 설정된 경우 등)
@DataJpaTest
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

    @BeforeEach
    void init() {
        contentQueryRepository = new ContentQueryRepositoryImpl(em);
    }

    @Test
    void findByHashtag() {
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
        List<Content> noOffsetResult = contentQueryRepository.findByHashtag(noOffsetParam);
        List<Content> halfOffsetResult = contentQueryRepository.findByHashtag(halfOffsetParam);
        List<Content> noOffsetLargeSizeResult = contentQueryRepository.findByHashtag(
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
}