package beforespring.socialfeed.web.api.v1.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse<T> {
    private HttpStatus status;
    private T message;
    private String path;

    public ErrorResponse(HttpStatus status, T message, String requestURI) {
        this.status = status;
        this.message = message;
        this.path = requestURI;
    }
}
