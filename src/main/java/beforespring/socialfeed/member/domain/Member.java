package beforespring.socialfeed.member.domain;

import beforespring.socialfeed.member.exception.PasswordDisMatchException;
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

    protected Member(String username, String password) {
        this.username = username;
        this.password = password;
    }

    static public Member create(String username, String password) {
        return new Member(username, password);
    }

    public void validatePassword(String rawPassword, PasswordValidator validator, PasswordHasher hasher) {
        validator.validate(this, rawPassword, hasher);
        if (!hasher.matches(rawPassword, password)) {
            throw new PasswordDisMatchException();
        }
    }

    public void updatePassword(String rawPassword, PasswordValidator validator, PasswordHasher hasher) {
        validator.validate(this, rawPassword, hasher);
        this.password = hasher.hash(rawPassword);
    }
}
