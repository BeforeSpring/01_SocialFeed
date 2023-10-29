package beforespring.socialfeed.jwt.infra.jpaimpl;

import static beforespring.Fixture.randString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import beforespring.Fixture;
import beforespring.socialfeed.jwt.domain.RefreshToken;
import beforespring.socialfeed.jwt.domain.exception.RefreshTokenExpirationException;
import beforespring.socialfeed.jwt.domain.exception.RefreshTokenRenewException;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RefreshTokenSpecificTest {

    Long givenId;
    UUID givenToken;
    String givenName;
    LocalDateTime now;
    long givenExpiration;

    @BeforeEach
    void init() {
        givenId = Fixture.randomPositiveLong();
        givenToken = UUID.randomUUID();
        givenName = randString();
        now = LocalDateTime.now();
        givenExpiration = 100L;
    }

    @Test
    void create() {
        // when
        RefreshTokenSpecific res = RefreshTokenSpecific.create(givenId, givenName, givenExpiration);

        // then
        assertThat(tuple(res.getIssuedToId(), res.getIssuedToName()))
            .describedAs("발급 받는 자가 제대로 설정되었는지 확인")
            .isEqualTo(tuple(givenId, givenName));

        assertThat(res.getToken())
            .describedAs("토큰이 생성돼야함.")
            .isNotNull();

        assertThat(res.getExpiresAt().minusMinutes(givenExpiration))
            .describedAs("토큰 유효기간이 제대로 설정됐는지 검사.")
            .isEqualTo(res.getIssuedAt());

        assertThat(res.getRenewedAt())
            .describedAs("생성 시점의 토큰은 renewedAt과 issuedAt이 동일함.")
            .isEqualTo(res.getIssuedAt());
    }

    @Test
    void renew() {
        // given
        RefreshTokenSpecific given = RefreshTokenSpecific.create(givenId, givenName,
            givenExpiration);
        UUID givenToken = given.getToken();
        long exp = Fixture.randomPositiveLong();
        LocalDateTime renewRequestedTime = LocalDateTime.now();
        LocalDateTime originalExp = given.getExpiresAt();
        // when
        given.renew(exp, givenName);

        // then
        assertThat(given.getToken())
            .describedAs("토큰이 재생성돼야함.")
            .isNotNull()
            .isNotEqualTo(givenToken);

        assertThat(given.getRenewedAt())
            .describedAs("갱신 요청 시점 기준으로 renewedAt이 갱신되어야함.")
            .isNotEqualTo(now)
            .isAfter(renewRequestedTime)
            .isBefore(renewRequestedTime.plusSeconds(1))  // 요청 시점을 정확히 잡을 수 없으나, 이후에 일어난 것은 확실
        ;

        assertThat(given.getExpiresAt())
            .describedAs("파라미터대로 유효기간이 설정되어야함.")
            .isNotNull()
            .isNotEqualTo(originalExp)
            .isEqualTo(given.getRenewedAt().plusMinutes(exp))
        ;
    }

    @Test
    @DisplayName("유효기간이 지난 토큰 renew 테스트")
    void renew_fails_when_expired() {
        // given
        RefreshTokenSpecific givenRefreshToken = RefreshTokenSpecific
                                                     .builder()
                                                     .issuedToName(givenName)
                                                     .expiresAt(LocalDateTime.now()
                                                                    .minusMinutes(1))  // 만료됨
                                                     .build();
        // when then
        assertThatThrownBy(() -> givenRefreshToken.renew(100, givenName))
            .describedAs("갱신에 실패하고, 적절한 예외를 반환해야함.")
            .isInstanceOf(RefreshTokenExpirationException.class);
    }

    @Test
    @DisplayName("username이 일치하지 않으면 갱신 실패.")
    void renew_fails_when_id_mismatch() {
        // given
        RefreshTokenSpecific givenRefreshToken = RefreshTokenSpecific
                                                     .builder()
                                                     .issuedToName(givenName)
                                                     .expiresAt(LocalDateTime.now()
                                                                    .minusMinutes(1))  // 만료됨
                                                     .build();
        // when then
        assertThatThrownBy(() -> givenRefreshToken.renew(100, randString() + givenName))
            .describedAs("givenName과 다른 값이기 때문에 갱신에 실패하고, 적절한 예외를 반환해야함.")
            .isInstanceOf(RefreshTokenRenewException.class)
            .hasMessageContaining("username mismatch");
    }

    @Test
    @DisplayName("UUID 토큰 값 문자열 반환 테스트")
    void getTokenValue() {
        // given
        UUID givenToken = UUID.randomUUID();
        RefreshTokenSpecific givenRefreshToken = RefreshTokenSpecific
                                                     .builder()
                                                     .token(givenToken)
                                                     .build();
        // when
        String res = givenRefreshToken.getTokenValue();

        // then
        assertThat(res)
            .isEqualTo(givenToken.toString());
    }

    @Test
    @DisplayName("RefreshToken 변환")
    void convert() {
        // given
        RefreshTokenSpecific given = RefreshTokenSpecific.builder()
                                         .expiresAt(LocalDateTime.now().plusMinutes(Fixture.randomPositiveLong()))
                                         .issuedToName(randString())
                                         .issuedToId(Fixture.randomPositiveLong())
                                         .token(UUID.randomUUID())
                                         .build();
        RefreshToken expected = RefreshToken.builder()
                                 .refreshToken(given.getTokenValue())
                                 .issuedToUsername(given.getIssuedToName())
                                 .issuedToId(given.getIssuedToId())
                                 .expiresAt(given.getExpiresAt())
                                 .build();
        // when
        RefreshToken actual = given.convert();

        // then
        assertThat(actual)
            .isEqualTo(expected);
    }
}