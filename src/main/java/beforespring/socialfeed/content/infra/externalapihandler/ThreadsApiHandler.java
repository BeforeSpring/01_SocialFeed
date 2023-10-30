package beforespring.socialfeed.content.infra.externalapihandler;

import beforespring.socialfeed.content.domain.ContentSourceType;
import beforespring.socialfeed.content.domain.ExternalApiHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ThreadsApiHandler implements ExternalApiHandler {
    @Override
    public ContentSourceType getSourceType() {
        return ContentSourceType.THREADS;
    }

    @Override
    public void like(String contentSourceId) {
        log.info("Threads like contentSourceId: {}", contentSourceId);
    }

    @Override
    public void share(String contentSourceId) {
        log.info("Threads share contentSourceId: {}", contentSourceId);
    }
}
