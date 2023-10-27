package beforespring.socialfeed.member.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(
    name = "confirm",
    indexes = {
        @Index(
            name = "idx__confirm__memberId",
            columnList = "memberId",
            unique = true
        )
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Confirm {

    @Id
    @Column(name = "confirmId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;
    @Column(nullable = false)
    private String token;

    @Builder
    public Confirm(Member member, String token) {
        this.member = member;
        this.token = token;
    }

    public void updateToken(String Token) {
        this.token = token;
    }

}