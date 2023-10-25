package beforespring.socialfeed.member.controller.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * 멤버 생성 DTO
 */
public class CreateMemberDto {

    /**
     * 멤버 생성 요청 dto. 이메일과 패스워드의 유효성을 검증함
     */
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static public class Request {
        @NotEmpty
        private String username;
        @NotEmpty
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        private String email;
        @NotEmpty
        @Size(min = 10, message = "비밀번호는 최소 10자 이상이어야 합니다.")
        private String password;

        public Request(String username, String email, String password) {
            this.username = username;
            this.email = email;
            validatePasswordPattern(password);
            this.password = password;
        }

        /**
         * 비밀번호 패턴 검증. 유효하지 않으면 에러를 던짐
         *
         * @param password 검증할 패스워드
         */
        private void validatePasswordPattern(String password) {
            if (!isPasswordValid(password)) {
                throw new IllegalArgumentException("숫자, 문자, 특수문자 중 2가지 이상을 포함해야 하고, 3회 이상 연속되는 문자는 사용이 불가합니다.");
            }
        }

        /**
         * 비밀번호 패턴 검사 로직
         *
         * @param password 검증할 패스워드
         * @return 숫자, 문자, 특수문자가 2개 이상 조합, 동일한 문자가 3회 미만 연속 시 true, 그렇지 않으면 else
         */
        private boolean isPasswordValid(String password) {
            String pattern = "^(?=(.*\\\\d.*)(.*[A-Za-z].*|.*[^A-Za-z\\\\d].*))|" +
                                 "(?=(.*[A-Za-z].*)(.*[^A-Za-z\\\\d].*))(?=.*\\\\d.*)|" +
                                 "(?=(.*[A-Za-z].*)(.*\\\\d.*))(.*[^A-Za-z\\\\d].*)$|";
            String consecutiveCharsPattern = "(.)\\1{2,}";

            return password.matches(pattern) && !password.matches(consecutiveCharsPattern);
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static public class Response {
        private Long id;

        public Response(Long id) {
            this.id = id;
        }
    }
}
