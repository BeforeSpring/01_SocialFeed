package beforespring.socialfeed.content.service;

import beforespring.socialfeed.content.domain.ContentSourceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FacebookApiHandler implements ExternalApiHandler{
    @Override
    public ContentSourceType getSourceType() {
        return ContentSourceType.FACEBOOK;
    }

    @Override
    public void like(String contentSourceId) {
        log.info("Facebook like contentSourceId: {}", contentSourceId);
    }

    @Override
    public void share(String contentSourceId) {
        log.info("Facebook share contentSourceId: {}", contentSourceId);
    }
}
