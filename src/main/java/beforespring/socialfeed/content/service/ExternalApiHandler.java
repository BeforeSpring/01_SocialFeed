package beforespring.socialfeed.content.service;

import beforespring.socialfeed.content.domain.ContentSourceType;
/**
 * 외부 API 핸들러를 정의하는 인터페이스
 */
public interface ExternalApiHandler {
    ContentSourceType getSourceType();
    void like(String contentSourceId);
    void share(String contentSourceId);
}
