package beforespring.socialfeed.member.domain;

import beforespring.socialfeed.member.exception.PasswordMismatchException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
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
@Getter
public class Member {

    @Id
    @Column(name = "memberId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;

    @Builder
    protected Member(
        Long id,
        String username,
        String raw,
        PasswordHasher hasher
        ){
        this.id = id;
        this.username = username;
        this.password = hasher.hash(raw);
    }

    public void validatePassword(String rawPassword, PasswordValidator validator, PasswordHasher hasher) {
        validator.validate(this, rawPassword, hasher);
        if (!hasher.matches(rawPassword, password)) {
            throw new PasswordMismatchException();
        }
    }

    public void updatePassword(String rawPassword, PasswordValidator validator, PasswordHasher hasher) {
        validator.validate(this, rawPassword, hasher);
        this.password = hasher.hash(rawPassword);
    }
}
