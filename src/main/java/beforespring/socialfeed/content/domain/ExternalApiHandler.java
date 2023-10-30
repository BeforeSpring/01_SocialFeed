package beforespring.socialfeed.content.domain;

/**
 * 외부 API 핸들러를 정의하는 인터페이스
 */
public interface ExternalApiHandler {
    ContentSourceType getSourceType();
    void like(String contentSourceId);
    void share(String contentSourceId);
}
