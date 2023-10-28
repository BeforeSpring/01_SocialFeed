package beforespring.socialfeed.content.domain;

import static beforespring.Fixture.random;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ContentQueryResultTest {

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void mapTo(boolean bool) {
        // given
        ContentQueryResult<String> given =
            ContentQueryResult.<String>builder()
                .contents(List.of("1", "2", "3"))
                .offsetRequiredNext(bool)
                .hasNext(!bool)
                .size(random.nextInt(0,1000000))
                .lastContentCreatedAt(LocalDateTime.now())
                .build();

        // when
        ContentQueryResult<Integer> res = given.map(Integer::parseInt);

        // then
        assertThat(
            tuple(
                given.offsetRequiredNext(), given.hasNext(), given.size(),
                given.lastContentCreatedAt()
            ))
            .describedAs("contents를 제외한 필드가 동일한지 확인")
            .isEqualTo(
                tuple(
                    res.offsetRequiredNext(), given.hasNext(), given.size(),
                    given.lastContentCreatedAt()
                ));

        List<Integer> expected = given.contents().stream()
                                     .map(Integer::valueOf)
                                     .toList();

        List<Integer> actual = res.contents();

        assertThat(actual)
            .describedAs("content가 변환되었는지 확인")
            .containsExactlyElementsOf(expected);
    }
}