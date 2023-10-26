package beforespring.socialfeed.content.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 해시태그 검색용 테이블
 */
@Entity
@Getter
@Table(
    name = "hashtag",
    indexes = {
        @Index(
            name = "idx__hashtag__hashtag__created_at",  // 무한스크롤에 사용되는 인덱스
            columnList = "hashtag, created_at"
        ),
        @Index(
            name = "idx__hashtag__content_id",  // 외래키 제약조건을 제거하는 대신 사용한 join용 인덱스
            columnList = "content_id"
        ),
        @Index(
            name = "idx__hashtag__created_at",  // n시간 이내 가장 많이 사용된 해시태그를 찾기 위한 인덱스
            columnList = "created_at"

        )
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HashtagContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_content_id")
    private Long id;  // UUID(v7) 적용 고려할것.

    @Column(name = "hashtag", nullable = false)
    private String hashtag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Content content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Builder
    protected HashtagContent(Long id, String hashtag, Content content, LocalDateTime createdAt) {
        this.id = id;
        this.hashtag = hashtag;
        this.content = content;
        this.createdAt = createdAt;
    }
}
