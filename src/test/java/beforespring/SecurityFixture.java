package beforespring;

import static beforespring.Fixture.randString;

import beforespring.socialfeed.config.JwtProperties;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class SecurityFixture {

    static public Jwt aBeforeSpringJwt(Long memberId, String username) throws NoSuchAlgorithmException {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                                  .issuer("self")
                                  .subject("BeforeSpring")
                                  .issuedAt(now)
                                  .expiresAt(now.plusSeconds(60*30))
                                  .claim("memberId", memberId.toString())
                                  .claim("username", username)
                                  .build();
        JwtEncoder jwtEncoder = JwtFixture.getJwtEncoder();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims));
    }

    static public JwtAuthenticationToken aJwtAuthenticationToken(Long memberId, String name)
        throws NoSuchAlgorithmException {
        return new JwtAuthenticationToken(aBeforeSpringJwt(memberId, name));
    }

    static public JwtAuthenticationToken aJwtAuthenticationToken() throws NoSuchAlgorithmException {
        return aJwtAuthenticationToken(Fixture.randomPositiveLong(), randString());
    }

    static public JwtProperties.JwtPropertiesBuilder aJwtProperties() {
        return JwtProperties.builder()
                   .accessTokenLifespanInMinutes(1800)
                   .refreshTokenLifespanInMinutes(180000)
                   .issuer("self")
                   .subject("yawiki");
    }
}
