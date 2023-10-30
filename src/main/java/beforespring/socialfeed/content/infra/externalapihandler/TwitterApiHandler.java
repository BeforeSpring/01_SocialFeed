package beforespring.socialfeed.content.infra.externalapihandler;

import beforespring.socialfeed.content.domain.ContentSourceType;
import beforespring.socialfeed.content.domain.ExternalApiHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TwitterApiHandler implements ExternalApiHandler {
    @Override
    public ContentSourceType getSourceType() {
        return ContentSourceType.TWITTER;
    }

    @Override
    public void like(String contentSourceId) {
        log.info("Twitter like contentSourceId: {}", contentSourceId);
    }

    @Override
    public void share(String contentSourceId) {
        log.info("Twitter share contentSourceId: {}", contentSourceId);
    }
}
