package beforespring.socialfeed.web.memberinforesolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.BDDMockito.given;

import beforespring.Fixture;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@ExtendWith(MockitoExtension.class)
class MemberInfoArgumentResolverTest {

    @Mock
    SecurityContext mockSecurityContext;

    @Mock
    JwtAuthenticationToken mockJwtAuthenticationToken;

    MemberInfoArgumentResolver memberInfoArgumentResolver = new MemberInfoArgumentResolver();

    @Test
    void resolveArgument() throws Exception {
        // given
        Map<String, Object> givenAttributes = new HashMap<>();
        givenAttributes.put("memberId", String.valueOf(Fixture.randomPositiveLong()));
        givenAttributes.put("username", Fixture.randString());

        SecurityContextHolder.setContext(mockSecurityContext);
        given(mockSecurityContext.getAuthentication())
            .willReturn(mockJwtAuthenticationToken);
        given(mockJwtAuthenticationToken.getTokenAttributes())
            .willReturn(givenAttributes);

        // when
        MemberInfo res = (MemberInfo) memberInfoArgumentResolver.resolveArgument(null, null,
            null, null);

        // then
        assertThat(tuple(res.id(), res.username()))
            .isEqualTo(tuple(Long.parseLong((String) givenAttributes.get("memberId")), givenAttributes.get("username")));
    }

    @Test
    void resolveArgument_null() throws Exception {
        // when then
        assertThatThrownBy(() -> memberInfoArgumentResolver.resolveArgument(null, null, null, null))
            .isInstanceOf(MemberInfoResolverException.class)
            .hasMessageContaining("null")
        ;
    }

    @Mock
    Authentication mockAuthentication;

    @Test
    void resolveArgument_not_implemented() throws Exception {
        // given
        SecurityContextHolder.setContext(mockSecurityContext);
        given(mockSecurityContext.getAuthentication())
            .willReturn(mockAuthentication);

        // when then
        assertThatThrownBy(() -> memberInfoArgumentResolver.resolveArgument(null, null, null, null))
            .isInstanceOf(MemberInfoResolverException.class)
            .hasMessageContaining("not implemented")
        ;
    }
}