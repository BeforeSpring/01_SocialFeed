package beforespring.socialfeed.jwt.infra.jpaimpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import beforespring.Fixture;
import beforespring.SecurityFixture;
import beforespring.socialfeed.config.JwtProperties;
import beforespring.socialfeed.jwt.domain.RefreshToken;
import beforespring.socialfeed.jwt.domain.exception.RefreshTokenNotFoundException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JpaRefreshTokenManagerTest {

    @Mock
    RefreshTokenRepository refreshTokenRepository;

    JwtProperties jwtProperties = SecurityFixture.aJwtProperties().refreshTokenLifespanInMinutes(
        100 + Fixture.randomPositiveLong()).build();

    JpaRefreshTokenManager jpaRefreshTokenManager;

    @Mock
    RefreshTokenSpecific mockRefreshTokenSpecific;

    @BeforeEach
    void init() {
        jpaRefreshTokenManager = new JpaRefreshTokenManager(refreshTokenRepository, jwtProperties);
    }

    @Captor
    ArgumentCaptor<RefreshTokenSpecific> refreshTokenSpecificCaptor;

    @Test
    void issue() {
        // given
        Long givenId = Fixture.randomPositiveLong();
        String givenName = Fixture.randString();
        given(refreshTokenRepository.save(any()))
            .willReturn(mockRefreshTokenSpecific);

        // when
        RefreshToken issue = jpaRefreshTokenManager.issue(givenId, givenName);

        // then
        verify(refreshTokenRepository).save(refreshTokenSpecificCaptor.capture());
        verify(mockRefreshTokenSpecific).convert();

        RefreshTokenSpecific captured = refreshTokenSpecificCaptor.getValue();

        assertThat(tuple(captured.getIssuedToId(), captured.getIssuedToName()))
            .describedAs("RefreshToken엔티티를 적절하게 생성했는지 확인")
            .isEqualTo(tuple(givenId, givenName));

        assertThat(captured.getExpiresAt()
                       .minusMinutes(jwtProperties.getRefreshTokenLifespanInMinutes()))
            .describedAs("jwtProperties의 값을 제대로 넘겼는지 테스트")
            .isEqualTo(captured.getRenewedAt());
    }

    @Test
    void renew_not_found() {
        String givenTokenValue = UUID.randomUUID().toString();
        String givenUsername = Fixture.randString();
        given(refreshTokenRepository.findByToken(UUID.fromString(givenTokenValue)))
            .willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> jpaRefreshTokenManager.renew(givenTokenValue, givenUsername))
            .isInstanceOf(RefreshTokenNotFoundException.class);
    }

    @Test
    void renew() {
        UUID givenToken = UUID.randomUUID();
        String givenUsername = Fixture.randString();

        given(refreshTokenRepository.findByToken(givenToken))
            .willReturn(Optional.of(mockRefreshTokenSpecific));

        // when
        RefreshToken renew = jpaRefreshTokenManager.renew(givenToken.toString(), givenUsername);

        // then
        verify(mockRefreshTokenSpecific).renew(eq(jwtProperties.getRefreshTokenLifespanInMinutes()),
            eq(givenUsername));
        verify(mockRefreshTokenSpecific).convert();
    }
}