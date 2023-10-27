package beforespring.socialfeed.member.infra;

import beforespring.socialfeed.member.domain.PasswordHasher;
import org.mindrot.jbcrypt.BCrypt;

public class BcryptHasher implements PasswordHasher {

    @Override
    public String hash(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    @Override
    public boolean isMatch(String raw, String hashed) {
        return  BCrypt.checkpw(raw, hashed);
    }
}
