package beforespring.socialfeed.member.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "member",
    indexes = {
        @Index(
            name = "idx__member__username",
            columnList = "username",
            unique = true
        )
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @Column(name = "memberId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;

    public void validatePassword(String rawPassword, PasswordValidator validator, PasswordHasher hasher)  throws Exception {
        try {
            validator.validate(this, rawPassword, hasher);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid password.");
        }
    }

    public void updatePassword(String rawPassword, PasswordValidator validator, PasswordHasher hasher) {
        try {
            validatePassword(rawPassword, validator, hasher);
            this.password = hasher.hash(rawPassword);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid password.");
        }
    };
}
