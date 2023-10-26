package beforespring.socialfeed.member.controller.dto;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import beforespring.socialfeed.member.controller.dto.CreateMemberDto.Request;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateMemberDtoTest {
    @Test
    void password_validation_test() {
        // given
        String givenUsername = "givenUsername";
        String givenEmail = "givenEmail@gmail.com";
        String givenPassword = "passwdThatShou!dBe0kay";

        // when then
        assertThatCode(
            () ->
                new Request(
                    givenUsername,
                    givenEmail,
                    givenPassword
                    )
        )
            .describedAs("예외가 발생하지 않고 생성에 성공할것.")
            .doesNotThrowAnyException();
    }
    @Test
    @DisplayName("3회 이상 연속되는 문자는 사용이 불가능합니다.")
    void password_validation_passwd_not_valid_consecutive_character() {
        // given
        String givenUsername = "givenUsername";
        String givenEmail = "givenEmail@gmail.com";
        String givenPassword = "paaaswd1!"; // a가 3번 반복되는 잘못된 패스워드

        // when then
        assertThatThrownBy(
            () ->
                new Request(
                    givenUsername,
                    givenEmail,
                    givenPassword
                )
        )
            .describedAs("3회 이상 반복되는 문자에 대해서 예외가 발생해야함.")
            .hasMessageContaining("3회 이상 연속되는 문자는 사용이 불가합니다.");
    }

    @Test
    @DisplayName("특수문자, 숫자, 문자 중 둘 이상은 포함해야함.")
    void password_validation_passwd_not_valid_() {
        // given
        String givenUsername = "givenUsername";
        String givenEmail = "givenEmail@gmail.com";
        String givenPassword = "passwordNotOkay"; // 숫자와 특수문자가 없는 잘못된 패스워드

        // when then
        assertThatThrownBy(
            () ->
                new Request(
                    givenUsername,
                    givenEmail,
                    givenPassword
                )
        )
            .describedAs("숫자, 문자, 특수문자 중 2가지 이상을 포함해야함.")
            .hasMessageContaining("숫자, 문자, 특수문자 중 2가지 이상을 포함해야");
    }
}