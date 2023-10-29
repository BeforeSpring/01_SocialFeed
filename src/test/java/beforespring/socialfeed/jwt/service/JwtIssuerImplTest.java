package beforespring.socialfeed.jwt.service;

import static beforespring.Fixture.randString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.BDDMockito.given;

import beforespring.Fixture;
import beforespring.socialfeed.jwt.domain.AccessTokenGenerator;
import beforespring.socialfeed.jwt.domain.AuthToken;
import beforespring.socialfeed.jwt.domain.RefreshTokenManager;
import beforespring.socialfeed.jwt.domain.RefreshToken;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JwtIssuerImplTest {

    JwtIssuerImpl jwtIssuer;

    @Mock
    AccessTokenGenerator accessTokenGenerator;

    @Mock
    RefreshTokenManager refreshTokenManager;

    Long givenMemberId;
    String givenUsername;
    String expectingAccessToken;
    String expectingRefreshToken;
    String expectingRenewedRefreshToken;

    @BeforeEach
    void init() {
        givenMemberId = Fixture.randomPositiveLong();
        givenUsername = randString();
        expectingAccessToken = givenUsername + givenMemberId.toString();
        expectingRefreshToken = UUID.randomUUID().toString();
        jwtIssuer = new JwtIssuerImpl(accessTokenGenerator, refreshTokenManager);
    }


    @Test
    void issue() {
        // given
        given(accessTokenGenerator.generate(givenMemberId, givenUsername))
            .willReturn(expectingAccessToken);

        given(refreshTokenManager.issue(givenMemberId, givenUsername))
            .willReturn(new RefreshToken(expectingRefreshToken, givenMemberId, givenUsername, LocalDateTime.now()));

        // when
        AuthToken issued = jwtIssuer.issue(givenMemberId, givenUsername);

        // then
        assertThat(tuple(issued.accessToken(), issued.refreshToken()))
            .isEqualTo(tuple(expectingAccessToken, expectingRefreshToken));
    }

    @Test
    void renew() {
        // given
        given(accessTokenGenerator.generate(givenMemberId, givenUsername))
            .willReturn(expectingAccessToken);

        given(refreshTokenManager.renew(expectingRefreshToken, givenUsername))
            .willReturn(new RefreshToken(expectingRenewedRefreshToken, givenMemberId, givenUsername, LocalDateTime.now()));

        String givenRefreshToken = expectingRefreshToken;

        // when
        AuthToken renew = jwtIssuer.renew(givenRefreshToken, givenUsername);

        // then
        assertThat(renew)
            .isNotNull()
            .isEqualTo(new AuthToken(expectingAccessToken, expectingRenewedRefreshToken));
    }
}