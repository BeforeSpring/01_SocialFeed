package beforespring.socialfeed.content.domain;


import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

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
public class Content {

    @EmbeddedId
    private ContentIdTuple idTuple;
}
