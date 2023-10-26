package beforespring.socialfeed.content.domain;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "content",
    indexes = {
        @Index(
            name = "idx__content_tuple",
            columnList = "content_id, content_source_type",
            unique = true
        )
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Content {
    @Id
    @Column(name = "memberId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ContentSourceType contentSourceType;

    private String title;
    private String content;
    private int viewCount;
    private int likeCount;
    private int shareCount;

    private LocalDateTime updatedAt;
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
}
