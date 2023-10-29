package beforespring.socialfeed.web.memberinforesolver;

public class MemberInfoResolverException extends RuntimeException {
    public MemberInfoResolverException() {
        super();
    }

    public MemberInfoResolverException(String message) {
        super(message);
    }

    public MemberInfoResolverException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberInfoResolverException(Throwable cause) {
        super(cause);
    }

    protected MemberInfoResolverException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
