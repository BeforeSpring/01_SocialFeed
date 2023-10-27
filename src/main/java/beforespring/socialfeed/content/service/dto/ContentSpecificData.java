package beforespring.socialfeed.content.service.dto;

import beforespring.socialfeed.content.domain.ContentSourceType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ContentSpecificData(
        Long contentId,
        ContentSourceType contentSourceType,
        String title,
        String content,
        List<String> hashtags,
        Long viewCount,
        Long likeCount,
        Long shareCount,
        LocalDateTime updatedAt,
        LocalDateTime createdAt

) {

}
