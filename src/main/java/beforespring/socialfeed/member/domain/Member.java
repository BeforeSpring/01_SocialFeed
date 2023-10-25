package beforespring.socialfeed.member.domain;

import beforespring.socialfeed.member.exception.InvalidPasswordException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    public void validatePassword(String rawPassword, PasswordValidator validator, PasswordHasher hasher) throws InvalidPasswordException {
        try {
            validator.validate(this, rawPassword, hasher);
        } catch (InvalidPasswordException e) {
            throw new InvalidPasswordException("Invalid password.");
        }
    }

    public void updatePassword(String rawPassword, PasswordValidator validator, PasswordHasher hasher) throws InvalidPasswordException {
        validatePassword(rawPassword, validator, hasher);
        this.password = hasher.hash(rawPassword);
    }

    ;
}
