package beforespring.socialfeed.jwt.domain.exception;

public class RefreshTokenRenewException extends RuntimeException {

    public RefreshTokenRenewException() {
        super();
    }

    public RefreshTokenRenewException(String message) {
        super(message);
    }

    public RefreshTokenRenewException(String message, Throwable cause) {
        super(message, cause);
    }

    public RefreshTokenRenewException(Throwable cause) {
        super(cause);
    }

    protected RefreshTokenRenewException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
