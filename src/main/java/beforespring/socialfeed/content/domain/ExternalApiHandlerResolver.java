package beforespring.socialfeed.content.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExternalApiHandlerResolver {
    private final Map<ContentSourceType, ExternalApiHandler> handlerMap = new HashMap<>();

    @Autowired
    public ExternalApiHandlerResolver(List<ExternalApiHandler> handlers) {
        handlers.forEach(handler -> handlerMap.put(handler.getSourceType(), handler));
    }

    /**
     * 핸들러를 검색하여 반환.
     *
     * @param sourceType sourceType
     * @return 해당 sourceType 에 대응하는 핸들러
     */

    public ExternalApiHandler resolveHandler(ContentSourceType sourceType) {
        return handlerMap.get(sourceType);
    }
}
