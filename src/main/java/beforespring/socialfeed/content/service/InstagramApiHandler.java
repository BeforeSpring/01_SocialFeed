package beforespring.socialfeed.content.service;

import beforespring.socialfeed.content.domain.ContentSourceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InstagramApiHandler implements ExternalApiHandler {
    @Override
    public ContentSourceType getSourceType() {
        return ContentSourceType.INSTAGRAM;
    }

    @Override
    public void like(String contentSourceId) {
        log.info("Instagram like contentSourceId: {}", contentSourceId);
    }

    @Override
    public void share(String contentSourceId) {
        log.info("Instagram share contentSourceId: {}", contentSourceId);
    }
}
