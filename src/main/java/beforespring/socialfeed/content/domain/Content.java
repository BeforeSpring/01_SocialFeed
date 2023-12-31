package beforespring.socialfeed.content.domain;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(
    name = "content",
    indexes = {
        @Index(
            name = "idx__content__content_source_id__content_source_type",
            columnList = "content_source_id, content_source_type",
            unique = true
        )
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Content {
    @Id
    @Column(name = "content_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /**
     * <p>외부 시스템에서 관리되는 ID로, unique함이 보장되지 않음. unique를 보장하려면 source와 함께 묶어야함.</p>
     */
    @Column(name = "content_source_id")
    private String contentSourceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_source_type")
    private ContentSourceType contentSourceType;

    @Column(name = "hashtags", columnDefinition = "varchar(1000)")
    private String hashtags;

    private String title;

    private String content;

    @Column(name = "view_count")
    private Long viewCount;

    @Column(name = "like_count")
    private Long likeCount;

    @Column(name = "share_count")
    private Long shareCount;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_at")

    private LocalDateTime createdAt;

    public void incrementViewCount() {
        this.viewCount++;
    }

    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void incrementShareCount() {
        this.shareCount++;
    }

    /**
     * 해시태그 문자열 분할
     */
    public List<String> getHashtagsList() {
        if (hashtags == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(hashtags.split(" "));
    }

    @Builder
    protected Content(
        Long id,
        String contentSourceId,
        ContentSourceType contentSourceType,
        String hashtags,
        String title,
        String content,
        Long viewCount,
        Long likeCount,
        Long shareCount,
        LocalDateTime updatedAt,
        LocalDateTime createdAt
    ) {
        this.id = id;
        this.contentSourceId = contentSourceId;
        this.contentSourceType = contentSourceType;
        this.hashtags = hashtags;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.shareCount = shareCount;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }
}
