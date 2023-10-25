package beforespring.socialfeed.member.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidException extends Exception {
    public InvalidPasswordException(String msg) {
        super(msg);
    }
}
