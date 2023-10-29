package beforespring.socialfeed.jwt.infra.jpaimpl;

import beforespring.socialfeed.config.JwtProperties;
import beforespring.socialfeed.jwt.domain.RefreshToken;
import beforespring.socialfeed.jwt.domain.RefreshTokenManager;
import beforespring.socialfeed.jwt.domain.exception.RefreshTokenNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class JpaRefreshTokenManager implements RefreshTokenManager {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;

    @Override
    public RefreshToken issue(Long memberId, String memberName) {
        RefreshTokenSpecific saved = refreshTokenRepository.save(
            RefreshTokenSpecific.create(
                memberId,
                memberName,
                jwtProperties.getRefreshTokenLifespanInMinutes())
        );

        return saved.convert();
    }

    @Override
    public RefreshToken renew(String refreshToken, String username) {
        RefreshTokenSpecific found = refreshTokenRepository.findByToken(UUID.fromString(refreshToken))
                                         .orElseThrow(() -> RefreshTokenNotFoundException.INSTANCE);
        found.renew(jwtProperties.getRefreshTokenLifespanInMinutes(), username);

        return found.convert();
    }
}
