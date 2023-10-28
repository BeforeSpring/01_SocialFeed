package beforespring.socialfeed.member.domain;

public class PasswordHasherImpl implements PasswordHasher {
    @Override
    public String hash(String toHash) {
        return toHash;
    }

    @Override
    public boolean matches(String raw, String hashed) {
        return hashed.equals(hash(raw));
    }
}
