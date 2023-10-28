package beforespring.socialfeed.content.infra;

import beforespring.socialfeed.content.domain.Content;
import beforespring.socialfeed.content.domain.ContentQueryRepository;
import beforespring.socialfeed.content.domain.ContentQueryResult;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;


/**
 * 더 많은 컨텐츠 존재 여부, offset 설정 필요 여부를 판단하여 ContentQueryResult로 매핑하는 클래스
 * @see ContentQueryRepository
 * @see ContentQueryResult
 */
@Component
public class ContentQueryMapper {
    boolean hasNext(List<Content> contents, int requestedSize) {
        return contents.size() > requestedSize;
    }

    LocalDateTime getLastCreatedAt(List<Content> contents) {
        return contents.isEmpty() ? null
                   : contents.get(contents.size() - 1).getCreatedAt();
    }

    boolean offsetRequiredNext(List<Content> contents, boolean hasNext) {
        if (!hasNext) {
            return false;
        }
        LocalDateTime nextContentCreationTime = getLastCreatedAt(contents);
        LocalDateTime lastContentCreationTime = contents.get(contents.size() - 2).getCreatedAt();
        return nextContentCreationTime.isEqual(lastContentCreationTime);
    }

    /**
     * ContentQueryResult로 매핑하는 메서드. 더 불러올 컨텐츠가 남았는지, 다음 쿼리는 어떻게 넣어야 하는지에 대한 정보를 포함하여 반환함.
     *
     * @param contents      content list
     * @param requestedSize 요청한 사이즈 (요청한 수량보다 하나 더 가져와서 hasNext 여부를 판단함.)
     * @return ContentQueryResult
     */
    public ContentQueryResult<Content> mapToContentQueryResult(
        List<Content> contents,
        int requestedSize
    ) {
        boolean hasNext = hasNext(contents, requestedSize);
        boolean offsetRequiredNext = offsetRequiredNext(contents, hasNext);

        if (hasNext) {
            contents = contents.subList(0, contents.size() - 1);
        }

        return ContentQueryResult.<Content>builder()
                   .lastContentCreatedAt(getLastCreatedAt(contents))
                   .size(contents.size())
                   .hasNext(hasNext)
                   .offsetRequiredNext(offsetRequiredNext)
                   .contents(contents)
                   .build();
    }
}
