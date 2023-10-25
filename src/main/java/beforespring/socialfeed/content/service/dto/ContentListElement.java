package beforespring.socialfeed.content.service.dto;

import beforespring.socialfeed.content.domain.ContentSourceType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

/**
 * @param contentId
 * @param type
 * @param title
 * @param content    최대 20자
 * @param hashTags
 * @param viewCount
 * @param likeCount
 * @param shareCount
 * @param updatedAt
 * @param createdAt
 */
public record ContentListElement(
    String contentId,
    ContentSourceType type,
    String title,
    String content,
    List<String> hashTags,
    Long viewCount,
    Long likeCount,
    Long shareCount,
    LocalDateTime updatedAt,
    LocalDateTime createdAt
) {

    @Builder
    public ContentListElement {
    }
}
