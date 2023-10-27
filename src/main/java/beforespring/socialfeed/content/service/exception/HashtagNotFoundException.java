package beforespring.socialfeed.content.service.exception;

public class HashtagNotFoundException extends RuntimeException {
    public HashtagNotFoundException() {
        super("Hashtag Not Found");
    }
}
