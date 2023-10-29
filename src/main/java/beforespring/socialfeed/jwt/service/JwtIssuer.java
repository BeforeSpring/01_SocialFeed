package beforespring.socialfeed.jwt.service;

import beforespring.socialfeed.jwt.domain.AuthToken;

public interface JwtIssuer {

    AuthToken issue(Long memberId, String memberName);

    /**
     * Redis key value storage 를 그대로 신뢰했다가 세션 정보가 꼬여서 다른 사용자 정보가 나오는 식으로 오동작한 사례들이 있음. 최소한의 인증을 위해
     * contributorName 이 일치하는지 확인함.
     *
     * @param refreshToken
     * @param memberName
     * @return AuthToken, accessToken, refreshToken의 값이 모두 갱신됨.
     */
    AuthToken renew(String refreshToken, String memberName);

}
