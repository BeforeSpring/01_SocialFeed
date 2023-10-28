package beforespring.socialfeed.member.infra;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BcryptHasherTest {

    private BcryptHasher hasher;

    @BeforeEach
    public void setup() {
        hasher = new BcryptHasher();
    }

    @Test
    @DisplayName("해싱을 진행하면 raw와는 다른 해싱 스트링이 저장되어 있어야됩니다.")
    public void hash_create_hashed_password() {

        String rawPassword = "testPassword";
        String hashedPassword = hasher.hash(rawPassword);

        assertThat(hashedPassword).isNotNull().isNotEqualTo(rawPassword);
    }

    @Test
    @DisplayName("같은 rawPassword의 hashedPassword를 가지고 체크할 경우,  True 가 나와야됩니다.")
    public void check_verifies_correct_password() {
        String rawPassword = "testPassword";
        String hashedPassword = hasher.hash(rawPassword);

        assertThat(hasher.isMatch(rawPassword, hashedPassword)).isTrue();
    }

    @Test
    @DisplayName("다른 rawPassword를 가지고 체크할 경우,  fail 가 나와야됩니다.")
    public void check_fail_incorrect_password() {
        String rawPassword = "testPassword";
        String hashedPassword = hasher.hash(rawPassword);

        assertThat(hasher.isMatch("mismatchPassword", hashedPassword)).isFalse();
    }
}