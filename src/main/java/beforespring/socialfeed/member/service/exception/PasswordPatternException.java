package beforespring.socialfeed.member.service.exception;

public class PasswordPatternException extends RuntimeException {
    public PasswordPatternException(String message) {
        super(message);
    }
}
