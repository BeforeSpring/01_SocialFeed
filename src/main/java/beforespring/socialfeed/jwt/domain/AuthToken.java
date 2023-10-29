package beforespring.socialfeed.jwt.domain;

public record AuthToken(
    String accessToken,
    String refreshToken
) {

}
