package beforespring;

import beforespring.socialfeed.content.domain.Content;
import beforespring.socialfeed.content.domain.ContentSourceType;
import java.time.LocalDateTime;
import java.util.Random;
import net.bytebuddy.utility.RandomString;

public class Fixture {

    static final ContentSourceType[] contentSourceTypes = ContentSourceType.values();

    static public final Random random = new Random();

    static public String randString() {
        return RandomString.make();
    }

    static public ContentSourceType randomSourceType() {
        return contentSourceTypes[random.nextInt(0, contentSourceTypes.length)];
    }

    /**
     * @return 1 ~ 1,000,000 사이의 Long
     */
    static public Long randomPositiveLong() {
        return random.nextLong(0, 1000000);
    }

    static public Content.ContentBuilder aContent() {
        LocalDateTime createdAt = LocalDateTime.now().minusSeconds(randomPositiveLong());
        return Content.builder()
                   .contentSourceId(randString())
                   .contentSourceType(randomSourceType())
                   .title(randString())
                   .content(randString() + randString() + randString() + randString())
                   .likeCount(randomPositiveLong())
                   .shareCount(randomPositiveLong())
                   .viewCount(randomPositiveLong())
                   .createdAt(createdAt)
                   .updatedAt(createdAt);
    }

}
