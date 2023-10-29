package beforespring.socialfeed.jwt.domain;

public interface RefreshTokenManager {

    RefreshToken issue(Long memberId, String username);

    // Redis key value storage 를 그대로 신뢰했다가 세션 정보가 꼬여서 다른 사용자 정보가 나오는 식으로 오동작한 사례들이 있음. 최소한의 인증을 위해 contributorName 이 일치하는지 확인함.
    RefreshToken renew(String refreshToken, String username);
}
