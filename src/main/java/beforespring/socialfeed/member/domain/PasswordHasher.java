package beforespring.socialfeed.member.domain;

public interface PasswordHasher {

    String hash(String toHash);

    boolean matches(String raw, String hashed);
}
