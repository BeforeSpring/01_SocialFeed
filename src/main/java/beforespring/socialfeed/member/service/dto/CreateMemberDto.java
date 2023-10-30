package beforespring.socialfeed.member.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 멤버 생성 DTO
 */
public class CreateMemberDto {

    /**
     * 멤버 생성 요청 dto. 이메일과 패스워드의 유효성을 검증함
     */
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static public class CreateMemberRequest {
        @NotEmpty
        private String username;
        @NotEmpty
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        private String email;
        @NotEmpty
        @Size(min = 10, message = "비밀번호는 최소 10자 이상이어야 합니다.")
        private String password;

        public CreateMemberRequest(String username, String email, String password) {
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
            if (isConsecutiveCharsPattern(password)) {
                throw new IllegalArgumentException("동일한 문자를 3회 이상 연속으로 사용할 수 없습니다.");
            }
            if (!isComplexCharsPattern(password)) {
                throw new IllegalArgumentException("숫자, 문자, 특수문자 중 2가지 이상을 포함해야 합니다.");
            }
        }

        /**
         * 비밀번호 패턴 검사 로직
         *
         * @param password 검증할 패스워드
         * @return 숫자, 문자, 특수문자가 중 2개 이상을 포함하면 true, 아니면 false
         */
        private boolean isComplexCharsPattern(String password) {
            String complexCharsPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{10,}$|" +
                                             "(?=.*[A-Za-z])(?=.*\\d).{10,}$|" +
                                             "(?=.*[A-Za-z])(?=.*[^A-Za-z\\d]).{10,}$|" +
                                             "(?=.*\\d)(?=.*[^A-Za-z\\d]).{10,}$";
            Matcher matcher = Pattern.compile(complexCharsPattern).matcher(password);
            return matcher.matches();
        }

        /**
         * 비밀번호 패턴 검사 로직
         *
         * @param password 검증할 패스워드
         * @return 동일한 문자가 3회 이상 연속되면 true, 아니면 false
         */
        private boolean isConsecutiveCharsPattern(String password) {
            String consecutiveCharsPattern = "(.)\\1\\1";
            Matcher matcher = Pattern.compile(consecutiveCharsPattern).matcher(password);
            return matcher.find();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static public class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
