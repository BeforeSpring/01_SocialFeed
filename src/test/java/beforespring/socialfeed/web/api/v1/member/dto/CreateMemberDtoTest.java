package beforespring.socialfeed.web.api.v1.member.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static beforespring.socialfeed.member.service.dto.CreateMemberDto.CreateMemberRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CreateMemberDtoTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void password_validation_test() {
        // given
        String givenUsername = "givenUsername";
        String givenEmail = "givenEmail@gmail.com";
        String givenPassword = "passwdThatShou!dBe0kay";

        // when
        CreateMemberRequest createMemberRequest = new CreateMemberRequest(
            givenUsername,
            givenEmail,
            givenPassword);
        Set<ConstraintViolation<CreateMemberRequest>> violation = validator.validate(createMemberRequest);

        //then
        assertThat(violation.size()).isEqualTo(0)
            .describedAs("예외가 발생하지 않고 생성에 성공할 것.");
    }

    @Test
    @DisplayName("유효하지 않은 이메일은 사용이 불가능합니다.")
    void email_validation_test() {
        // given
        String givenUsername = "givenUsername";
        String givenEmail = "givenEmail@gmail@com"; // 유효하지 않은 이메일 패턴
        String givenPassword = "abscdefg123!@";

        // when
        CreateMemberRequest createMemberRequest = new CreateMemberRequest(
            givenUsername,
            givenEmail,
            givenPassword);
        Set<ConstraintViolation<CreateMemberRequest>> violation = validator.validate(createMemberRequest);
        List<String> messages = new ArrayList<>();
        violation.forEach(i -> messages.add(i.getMessage()));

        //then
        assertThat(violation.size()).isEqualTo(1);
        assertThat(messages).contains("이메일 형식이 올바르지 않습니다.");
    }

    @Test
    @DisplayName("3회 이상 연속되는 문자는 사용이 불가능합니다.")
    void password_validation_passwd_not_valid_consecutive_character() {
        // given
        String givenUsername = "givenUsername";
        String givenEmail = "givenEmail@gmail.com";
        String givenPassword = "paaaaswd1!!"; // a가 3번 반복되는 잘못된 패스워드

        // when then
        assertThatThrownBy(
            () ->
                new CreateMemberRequest(
                    givenUsername,
                    givenEmail,
                    givenPassword
                )
        )
            .describedAs("3회 이상 반복되는 문자에 대해서 예외가 발생해야 함.")
            .hasMessageContaining("동일한 문자를 3회 이상 연속으로 사용할 수 없습니다.");
    }

    @Test
    @DisplayName("비밀번호는 최소 10자 이상이어야 합니다.")
    void password_validation_length_test() {
        // given
        String givenUsername = "givenUsername";
        String givenEmail = "givenEmail@gmail.com";
        String givenPassword = "a1!"; // 10자 미만의 잘못된 패스워드

        // when
        CreateMemberRequest createMemberRequest = new CreateMemberRequest(
            givenUsername,
            givenEmail,
            givenPassword);
        Set<ConstraintViolation<CreateMemberRequest>> violation = validator.validate(createMemberRequest);
        List<String> messages = new ArrayList<>();
        violation.forEach(i -> messages.add(i.getMessage()));

        //then
        assertThat(violation.size()).isEqualTo(1);
        assertThat(messages).contains("비밀번호는 최소 10자 이상이어야 합니다.");
    }

    @Test
    @DisplayName("문자만 포함된 비밀번호는 숫자 혹은 특수문자를 포함해야 합니다.")
    void password_validation_passwd_contains_only_alpha() {
        // given
        String givenUsername = "givenUsername";
        String givenEmail = "givenEmail@gmail.com";
        String givenPassword = "passwordNotOkay"; // 문자만 존재하는 패스워드

        // when then
        assertThatThrownBy(
            () ->
                new CreateMemberRequest(
                    givenUsername,
                    givenEmail,
                    givenPassword
                )
        )
            .describedAs("문자만 포함되면 예외가 발생해야 함.")
            .hasMessageContaining("숫자 혹은 특수문자를 포함해야 합니다.");
    }

    @Test
    @DisplayName("숫자만 포함된 비밀번호는 문자 혹은 특수문자를 포함해야 합니다.")
    void password_validation_passwd_contains_only_num() {
        // given
        String givenUsername = "givenUsername";
        String givenEmail = "givenEmail@gmail.com";
        String givenPassword = "0123456789"; // 숫자만 존재하는 패스워드
        // when then
        assertThatThrownBy(
            () ->
                new CreateMemberRequest(
                    givenUsername,
                    givenEmail,
                    givenPassword
                )
        )
            .describedAs("숫자만 포함되면 예외가 발생해야 함.")
            .hasMessageContaining("문자 혹은 특수문자를 포함해야 합니다.");
    }

    @Test
    @DisplayName("특수문자만 포함된 비밀번호는 문자 혹은 숫자를 포함해야 합니다.")
    void password_validation_passwd_contains_only_specail_char() {
        // given
        String givenUsername = "givenUsername";
        String givenEmail = "givenEmail@gmail.com";
        String givenPassword = "~!@#$%^&*()"; // 특수문자만 존재하는 패스워드
        // when then
        assertThatThrownBy(
            () ->
                new CreateMemberRequest(
                    givenUsername,
                    givenEmail,
                    givenPassword
                )
        )
            .describedAs("특수문자 포함되면 예외가 발생해야 함.")
            .hasMessageContaining("문자 혹은 숫자를 포함해야 합니다.");
    }
}