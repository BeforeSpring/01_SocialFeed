package beforespring.socialfeed.content.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * contentId(String), ContentSourceType을 쌍으로 하는 ID 클래스
 */
@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentIdTuple implements Serializable {

    @Column(name = "content_id")
    private String contentId;
    @Column(name = "content_source_type")
    private ContentSourceType contentSourceType;

    public ContentIdTuple(String contentId, ContentSourceType contentSourceType) {
        this.contentId = contentId;
        this.contentSourceType = contentSourceType;
    }
}
