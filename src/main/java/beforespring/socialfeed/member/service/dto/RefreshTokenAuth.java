package beforespring.socialfeed.member.service.dto;

public record RefreshTokenAuth(
    String username,
    String refreshToken
) {

}
