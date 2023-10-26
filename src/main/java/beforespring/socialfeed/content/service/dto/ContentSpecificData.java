package beforespring.socialfeed.content.service.dto;

import beforespring.socialfeed.content.domain.ContentSourceType;

import java.time.LocalDateTime;

public record ContentSpecificData(
        Long contentId,
        ContentSourceType contentSourceType,
        String title,
        String content,
        Long viewCount,
        Long likeCount,
        Long shareCount,
        LocalDateTime updatedAt,
        LocalDateTime createdAt

) {

}
