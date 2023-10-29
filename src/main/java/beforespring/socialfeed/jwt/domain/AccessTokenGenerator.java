package beforespring.socialfeed.jwt.domain;

public interface AccessTokenGenerator {

    String generate(Long memberId, String username);
}
