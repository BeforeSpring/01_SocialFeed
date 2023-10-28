package beforespring.socialfeed.content.infra;

import static beforespring.Fixture.aContent;
import static org.assertj.core.api.Assertions.assertThat;

import beforespring.socialfeed.content.domain.Content;
import beforespring.socialfeed.content.domain.Content.ContentBuilder;
import beforespring.socialfeed.content.domain.ContentQueryResult;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ContentQueryMapperTest {

    ContentQueryMapper mapper = new ContentQueryMapper();

    @Test
    void mapToContentQueryResult() {
    }

    @Test
    @DisplayName("List 크기가 요청한 것 보다 크면 hasNext true")
    void mapFrom_has_next_true() {
        // given
        int givenRequestedSize = 10;
        List<Content> contentsSortedByCreatedAtDesc = Stream.generate(() -> aContent().build())
                                                          .limit(givenRequestedSize + 1)  // givenRequestedSize보다 1 많이 생성
                                                          .sorted(Comparator.comparing(
                                                              Content::getCreatedAt).reversed())
                                                          .toList();

        // when
        ContentQueryResult<Content> result = mapper.mapToContentQueryResult(
            contentsSortedByCreatedAtDesc, givenRequestedSize
        );

        // then
        assertThat(result.hasNext())
            .isTrue();
    }

    @Test
    @DisplayName("List 크기가 요청한 것 보다 크지 않으면(작거나 같으면) hasNext true")
    void mapFrom_has_next_false() {
        // given
        int givenRequestedSize = 10;
        List<Content> contentsSortedByCreatedAtDesc = Stream.generate(() -> aContent().build())
                                                          .limit(
                                                              givenRequestedSize)  // givenRequestedSize와 동일
                                                          .sorted(Comparator.comparing(
                                                              Content::getCreatedAt).reversed())
                                                          .toList();

        // when
        ContentQueryResult<Content> result = mapper.mapToContentQueryResult(
            contentsSortedByCreatedAtDesc, givenRequestedSize
        );

        // then
        assertThat(result.hasNext())
            .isFalse();
    }

    @Test
    @DisplayName("마지막 두 Content가 같은 시점에 생성되고, hasNext인 경우 offset required true")
    void mapToContentListElements_offset_required_true() {
        // given
        int givenRequestedSize = 10;
        LocalDateTime lastTwoElementCreatedAt = LocalDateTime.now().minusYears(1);
        List<Content> contentsSortedByCreatedAtDesc = IntStream.range(0, givenRequestedSize + 1) // givenRequestedSize보다 1개 많이.
                                                          .mapToObj(i -> {
                                                              ContentBuilder builder = aContent();
                                                              if (i >= givenRequestedSize - 1) {  // 마지막 두 Content가 같은 시점에 생성됨.
                                                                  builder.createdAt(lastTwoElementCreatedAt);
                                                              }
                                                              return builder.build();
                                                          })
                                                          .toList();

        // when
        ContentQueryResult<Content> result = mapper.mapToContentQueryResult(
            contentsSortedByCreatedAtDesc, givenRequestedSize
        );

        // then
        assertThat(result.offsetRequiredNext())
            .isTrue();
    }

    @Test
    @DisplayName("마지막 두 Content가 같은 시점에 생성되었지만 hasNext가 false인 경우 offsetRequired false")
    void mapToContentListElements_offset_required_true_when_has_next_is_false() {
        // given
        int givenRequestedSize = 10;
        LocalDateTime lastTwoElementCreatedAt = LocalDateTime.now().minusYears(1);
        List<Content> contentsSortedByCreatedAtDesc = IntStream.range(0, givenRequestedSize) // givenRequestedSize와 동일한 수량을 생성함.
                                                          .mapToObj(i -> {
                                                              ContentBuilder builder = aContent();
                                                              if (i >= givenRequestedSize - 1) {  // 마지막 두 Content가 같은 시점에 생성됨.
                                                                  builder.createdAt(lastTwoElementCreatedAt);
                                                              }
                                                              return builder.build();
                                                          })
                                                          .toList();

        // when
        ContentQueryResult<Content> result = mapper.mapToContentQueryResult(
            contentsSortedByCreatedAtDesc, givenRequestedSize
        );

        // then
        assertThat(result.hasNext())
            .isFalse();
        assertThat(result.offsetRequiredNext())
            .isFalse();
    }

    @Test
    @DisplayName("마지막 두 element가 다르면 offsetRequired false")
    void mapToContentListElements_offset_not_required() {
        // given
        int givenRequestedSize = 10;
        List<Content> contentsSortedByCreatedAtDesc = Stream.generate(() -> aContent().build())
                                                          .limit(givenRequestedSize + 1)  // givenRequestedSize보다 1 많이 생성
                                                          .sorted(Comparator.comparing(
                                                              Content::getCreatedAt).reversed())
                                                          .toList();

        // when
        ContentQueryResult<Content> result = mapper.mapToContentQueryResult(
            contentsSortedByCreatedAtDesc, givenRequestedSize
        );

        // then
        assertThat(result.hasNext())
            .isTrue();
        assertThat(result.offsetRequiredNext())
            .isFalse();
    }
}