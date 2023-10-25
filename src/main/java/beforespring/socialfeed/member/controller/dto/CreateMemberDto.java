package beforespring.socialfeed.member.controller.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CreateMemberDto {

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
        @Pattern(
            regexp = "^(?=(.*\\\\d.*)(.*[A-Za-z].*|.*[^A-Za-z\\\\d].*))|" +
                         "(?=(.*[A-Za-z].*)(.*[^A-Za-z\\\\d].*))(?=.*\\\\d.*)|" +
                         "(?=(.*[A-Za-z].*)(.*\\\\d.*))(.*[^A-Za-z\\\\d].*)$|",
            message = "비밀번호는 숫자, 문자, 특수문자 중 2가지 이상을 포함해야 합니다.")
        @Pattern(regexp = "(.)\\\\1{2,}", message = "3회 이상 연속되는 문자는 사용할 수 없습니다.")
        private String password;

        public Request(String username, String email, String password) {
            this.username = username;
            this.email = email;
            this.password = password;
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
