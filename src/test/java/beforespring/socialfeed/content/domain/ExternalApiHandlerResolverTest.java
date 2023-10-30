package beforespring.socialfeed.content.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ExternalApiHandlerResolverTest {
    @Autowired
    private ExternalApiHandlerResolver handlerResolver;

    @Test
    @DisplayName("ExternalApiHandlerResolver 테스트")
    public void testResolveHandler() {
        ContentSourceType facebookType = ContentSourceType.FACEBOOK;
        ContentSourceType instagramType = ContentSourceType.INSTAGRAM;
        ContentSourceType threadsType = ContentSourceType.THREADS;
        ContentSourceType twitterType = ContentSourceType.TWITTER;

        ExternalApiHandler facebookHandler = handlerResolver.resolveHandler(facebookType);
        ExternalApiHandler instagramHandler = handlerResolver.resolveHandler(instagramType);
        ExternalApiHandler threadsHandler = handlerResolver.resolveHandler(threadsType);
        ExternalApiHandler twitterHandler = handlerResolver.resolveHandler(twitterType);

        assertNotNull(facebookHandler);
        assertNotNull(instagramHandler);
        assertNotNull(threadsHandler);
        assertNotNull(twitterHandler);
        assertEquals(facebookType, facebookHandler.getSourceType());
        assertEquals(instagramType, instagramHandler.getSourceType());
        assertEquals(threadsType, threadsHandler.getSourceType());
        assertEquals(twitterType, twitterHandler.getSourceType());
    }
}