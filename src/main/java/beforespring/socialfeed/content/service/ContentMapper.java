package beforespring.socialfeed.content.service;

import beforespring.socialfeed.content.domain.Content;
import beforespring.socialfeed.content.service.dto.ContentListElement;
import org.springframework.stereotype.Component;

@Component
public class ContentMapper {
    /**
     * 최대 20자
     *
     * @param content
     * @return content.content()가 20자가 넘으면 20자 까지만 잘라서 반환.
     */
    String getContentBody(Content content) {
        return content.getContent().length() > 20
                   ? content.getContent().substring(0, 20)
                   : content.getContent();
    }

    /**
     * Content 를 받아 ContentListElement로 매핑함. 본문은 최대 20자 까지만 포함해야한다는 제약 준수
     *
     * @param content 변환할 Content
     * @return ContentListElement - 본문은 최대 20자 까지만, 해시태그를 List형태로 변환함.
     * @see ContentListElement
     */
    public ContentListElement mapToContentListElement(Content content) {
        return ContentListElement.builder()
                   .id(content.getId())
                   .contentId(content.getContentSourceId())
                   .title(content.getTitle())
                   .content(getContentBody(content))
                   .type(content.getContentSourceType())
                   .hashTags(content.getHashtagsList())
                   .viewCount(content.getViewCount())
                   .shareCount(content.getShareCount())
                   .likeCount(content.getLikeCount())
                   .createdAt(content.getCreatedAt())
                   .updatedAt(content.getUpdatedAt())
                   .build();
    }
}
