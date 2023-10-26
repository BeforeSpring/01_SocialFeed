package beforespring.socialfeed.content.domain;

import javax.persistence.*;

/**
 * 해시태그 검색용 테이블
 */
@Entity
@Table(
    name = "hashtag",
    indexes = {
        @Index(
            name = "idx__hashtag__hashtag",
            columnList = "hashtag"
        ),
        @Index(
            name = "idx__hashtag__content_id__content_source_type",
            columnList = "content_id, content_source_type"
        )
    }
)
public class HashtagContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // UUID 적용 고려할것.
    private String hashtag;
}
