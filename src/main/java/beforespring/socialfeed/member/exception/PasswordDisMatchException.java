package beforespring.socialfeed.member.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PasswordDisMatchException extends RuntimeException {
    public PasswordDisMatchException(String msg) {
        super(msg);
    }
}
