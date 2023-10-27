package beforespring.socialfeed.content.domain;

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

import lombok.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 해시태그 검색용 테이블
 */
@Entity
@Getter
@Table(
    name = "hashtag",
    indexes = {
        @Index(
            name = "idx__hashtag__hashtag",
            columnList = "hashtag"
        ),
        @Index(
            name = "idx__hashtag__content_id",
            columnList = "content_id"
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

    @Builder
    protected HashtagContent(Long id, String hashtag, Content content) {
        this.id = id;
        this.hashtag = hashtag;
        this.content = content;
    }

    /**
     * 해시태그 문자열 분할
     */
    public List<String> getHashtagsList() {
        if (hashtag == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(hashtag.split(" "));
    }

}
