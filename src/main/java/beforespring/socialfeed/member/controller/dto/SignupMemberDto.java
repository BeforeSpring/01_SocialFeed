package beforespring.socialfeed.member.controller.dto;

import beforespring.socialfeed.member.domain.PasswordHasher;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * 멤버 회원가입 시 사용될 DTO
 */
public class SignupMemberDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static public class Request {
        @NotEmpty
        String userName;

        @NotEmpty
        String password;

        @NotEmpty
        String token;

        public Request(String userName, String password, String token) {
            this.userName = userName;
            this.password = password;
            this.token = token;
        }
    }
}