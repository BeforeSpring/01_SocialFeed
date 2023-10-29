package beforespring.socialfeed.jwt.infra;

import static beforespring.Fixture.randString;
import static beforespring.Fixture.randomPositiveLong;
import static beforespring.JwtFixture.getJwtDecoder;
import static beforespring.JwtFixture.getJwtEncoder;
import static org.assertj.core.api.Assertions.assertThat;

import beforespring.socialfeed.config.JwtProperties;
import com.nimbusds.jose.JOSEException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

class AccessTokenGeneratorImplTest {

    JwtEncoder jwtEncoder;
    JwtDecoder jwtDecoder;
    JwtProperties jwtProperties;

    AccessTokenGeneratorImpl accessTokenGenerator;

    Long givenId;
    String givenName;

    @BeforeEach
    void init() throws NoSuchAlgorithmException, JOSEException {
        Random random = new Random();

        jwtEncoder = getJwtEncoder();
        jwtDecoder = getJwtDecoder();
        jwtProperties = JwtProperties.builder()
                            .issuer(randString())
                            .subject(randString())
                            .accessTokenLifespanInMinutes(random.nextInt(180, 1800))
                            .build();
        accessTokenGenerator = new AccessTokenGeneratorImpl(jwtProperties, jwtEncoder);
        givenId = randomPositiveLong();
        givenName = randString();
    }

    @Test
    void generate() {
        // when
        String s = accessTokenGenerator.generate(givenId, givenName);

        // then
        Jwt decoded = jwtDecoder.decode(s);
        Map<String, Object> claims = decoded.getClaims();
        String memberId = (String) claims.get("memberId");
        String name = (String) claims.get("username");
        String issuer = (String) claims.get("iss");
        String subject = decoded.getSubject();

        // jwt payload 체크
        assertThat(memberId)
            .isEqualTo(givenId.toString());
        assertThat(name)
            .isEqualTo(givenName);

        // properties 체크
        assertThat(decoded.getExpiresAt().minusSeconds(jwtProperties.getAccessTokenLifespanInMinutes() * 60))
            .isNotNull()
            .isEqualTo(decoded.getIssuedAt());
        assertThat(issuer)
            .isEqualTo(jwtProperties.getIssuer());
        assertThat(subject)
            .isEqualTo(jwtProperties.getSubject());
    }
}