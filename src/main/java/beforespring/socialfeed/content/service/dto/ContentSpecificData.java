package beforespring.socialfeed.content.service.dto;

import beforespring.socialfeed.content.domain.ContentSourceType;

import java.time.LocalDateTime;

public record ContentSpecificData(
        Long contentId,
        ContentSourceType contentSourceType,
        String title,
        String content,
        int viewCount,
        int likeCount,
        int shareCount,
        LocalDateTime updatedAt,
        LocalDateTime createdAt

) {

}
