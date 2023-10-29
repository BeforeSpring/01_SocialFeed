package beforespring.socialfeed.jwt.domain;

import java.time.LocalDateTime;

public record AccessToken(
    String token,
    LocalDateTime expiresAt
) {

}
