package beforespring.socialfeed.member.service.dto;

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

            PasswordPatternChecker patternChecker = new PasswordPatternChecker();
            patternChecker.checkConsecutiveChars(password);
            patternChecker.checkCharsCombination(password);
            this.password = password;
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
