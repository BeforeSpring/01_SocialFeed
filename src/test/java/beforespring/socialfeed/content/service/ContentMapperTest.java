package beforespring.socialfeed.content.service;

import static beforespring.Fixture.aContent;
import static beforespring.Fixture.randomPositiveLong;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import beforespring.socialfeed.content.domain.Content;
import beforespring.socialfeed.content.service.dto.ContentListElement;
import java.util.Arrays;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ContentMapperTest {

    ContentMapper mapper = new ContentMapper();

    @Test
    @DisplayName("content 본문, hashtags를 제외한 다른 정보가 제대로 매핑되는지 테스트")
    void mapToContentListElement() {
        // given
        String givenContentBody = "123456789012345678901";  // 21자
        Content givenContent = aContent()
                                   .contentSourceId(UUID.randomUUID().toString())
                                   .content(givenContentBody)
                                   .id(randomPositiveLong())
                                   .build();

        // when
        ContentListElement res = mapper.mapToContentListElement(givenContent);

        // then
        assertThat(
            tuple(
                res.id(),
                res.contentId(),
                res.type(),
                res.createdAt(),
                res.updatedAt(),
                res.likeCount(),
                res.shareCount(),
                res.viewCount(),
                res.title()))
            .describedAs("content body, hashtags 를 제외한 다른 필드가 정상 매핑되었는지 체크")
            .isEqualTo(
                tuple(
                    givenContent.getId(),
                    givenContent.getContentSourceId(),
                    givenContent.getContentSourceType(),
                    givenContent.getCreatedAt(),
                    givenContent.getUpdatedAt(),
                    givenContent.getLikeCount(),
                    givenContent.getShareCount(),
                    givenContent.getViewCount(),
                    givenContent.getTitle())
            );
    }

    @Test
    @DisplayName("기준을 넘는 content 본문이 제대로 매핑되는지 테스트")
    void mapToContentListElement_content_body_length_longer() {
        // given
        String givenContentBody = "123456789012345678901";  // 21자
        Content givenContent = aContent()
                                   .contentSourceId(UUID.randomUUID().toString())
                                   .content(givenContentBody)
                                   .id(randomPositiveLong())
                                   .build();

        // when
        ContentListElement res = mapper.mapToContentListElement(givenContent);

        // then
        assertThat(res.content())
            .describedAs("내용이 20자가 넘는 경우, 20자까지만 잘라서 반환하는지 체크")
            .hasSize(20)
            .isEqualTo(givenContentBody.substring(0, 20));
    }

    @Test
    @DisplayName("기준과 같은 길이를 가진 본문이 제대로 매핑되는지 테스트")
    void mapToContentListElement_content_body_length_exact() {
        // given
        String givenContentBody = "12345678901234567890";  // 20자
        Content givenContent = aContent()
                                   .contentSourceId(UUID.randomUUID().toString())
                                   .content(givenContentBody)
                                   .id(randomPositiveLong())
                                   .build();

        // when
        ContentListElement res = mapper.mapToContentListElement(givenContent);

        // then
        assertThat(res.content())
            .describedAs("내용이 20자인 경우 자르지 않아야함.")
            .isEqualTo(givenContentBody);
    }

    @Test
    @DisplayName("기준보다 짧은 content 본문이 제대로 매핑되는지 테스트")
    void mapToContentListElement_content_body_length_less() {
        // given
        String givenContentBody = "1234567890123456789";  // 19자
        Content givenContent = aContent()
                                   .contentSourceId(UUID.randomUUID().toString())
                                   .content(givenContentBody)
                                   .id(randomPositiveLong())
                                   .build();

        // when
        ContentListElement res = mapper.mapToContentListElement(givenContent);

        // then
        assertThat(res.content())
            .describedAs("내용이 20자 미만인 경우 자르지 않아야함.")
            .isEqualTo(givenContentBody);
    }

    @Test
    @DisplayName("2개 이상의 해시태그가 제대로 매핑되는지 테스트")
    void mapToContentListElement_hashtags() {
        // given
        String givenHashtags = "wanted pre_onboarding backend before_spring";
        Content givenContent = aContent()
                                   .contentSourceId(UUID.randomUUID().toString())
                                   .hashtags(givenHashtags)
                                   .id(randomPositiveLong())
                                   .build();

        // when
        ContentListElement res = mapper.mapToContentListElement(givenContent);

        // then
        assertThat(res.hashTags())
            .describedAs("hashtags가 정상적으로 매핑되었는지 체크")
            .containsExactlyElementsOf(givenContent.getHashtagsList());
    }

    @Test
    @DisplayName("1개의 해시태그가 제대로 매핑되는지 테스트")
    void mapToContentListElement_hashtags_only_one() {
        // given
        String givenHashtags = "before_spring";
        Content givenContent = aContent()
                                   .contentSourceId(UUID.randomUUID().toString())
                                   .hashtags(givenHashtags)
                                   .id(randomPositiveLong())
                                   .build();

        // when
        System.out.println("givenContent.getHashtagsList() = " + Arrays.toString(
            givenContent.getHashtagsList().toArray(new String[0])));
        ContentListElement res = mapper.mapToContentListElement(givenContent);

        // then
        assertThat(res.hashTags())
            .describedAs("hashtags가 정상적으로 매핑되었는지 체크")
            .containsExactlyElementsOf(givenContent.getHashtagsList());
    }
}