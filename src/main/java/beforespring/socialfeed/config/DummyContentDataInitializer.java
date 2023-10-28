package beforespring.socialfeed.config;

import beforespring.socialfeed.content.domain.Content;
import beforespring.socialfeed.content.domain.ContentSourceType;
import beforespring.socialfeed.content.domain.HashtagContent;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

/**
 * 더미 데이터 초기화용 클래스.
 * <p>
 * 모든 Content는 하나의 Readable 해시태그와 0~(MAX_HASHTAGS_PER_CONTENT-1)개의 무작위 생성 해시태그를 가짐.
 * </p>
 */
@Slf4j
@Transactional
public class DummyContentDataInitializer {

    private final EntityManager em;

    private final int CONTENTS_TO_CREATE;

    private final int MAX_HASHTAGS_PER_CONTENT;

    private final ContentSourceType[] contentSourceTypes = ContentSourceType.values();

    private final Random random = new Random();

    private final List<String> readableHashtags;

    // todo refactor logging
    /**
     * ApplicationReadyEvent가 발생하면 데이터를 초기화함.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        log.info("Dummy data initializing...");
        long startedAt = System.currentTimeMillis();
        List<Content> contents = Stream.generate(() -> aContent().build())
                                     .limit(CONTENTS_TO_CREATE)
                                     .toList();
        contents.forEach(em::persist);
        contents.forEach(this::persistHashtagContents);
        em.flush();
        long finishedAt = System.currentTimeMillis();
        log.info("Dummy data initialized in " + (finishedAt - startedAt) + "ms\nREADABLE_HASHTAGS: " + Arrays.toString(readableHashtags.toArray(new String[0])) + "\nCONTENTS_TO_CREATE: " + CONTENTS_TO_CREATE + "\nMAX_HASHTAGS_PER_CONTENT: " + MAX_HASHTAGS_PER_CONTENT);
    }

    /**
     * @param em                       EntityManager
     * @param CONTENTS_TO_CREATE       생성할 게시물 숫자
     * @param MAX_HASHTAGS_PER_CONTENT 게시물 당 최대 해시태그 수
     */
    @Builder
    protected DummyContentDataInitializer(
        EntityManager em,
        int CONTENTS_TO_CREATE,
        int MAX_HASHTAGS_PER_CONTENT,
        List<String> readableHashtags
    ) {
        this.em = em;
        this.CONTENTS_TO_CREATE = CONTENTS_TO_CREATE;
        this.MAX_HASHTAGS_PER_CONTENT = MAX_HASHTAGS_PER_CONTENT;
        this.readableHashtags = readableHashtags;
    }


    private void persistHashtagContents(Content content) {
        String hashtags = content.getHashtags();
        String[] hashtagSplit = hashtags.split("\\s");
        Arrays.stream(hashtagSplit)
            .map(hashtag -> HashtagContent.builder()
                                .hashtag(hashtag)
                                .content(content)
                                .createdAt(content.getCreatedAt())
                                .build()
            )
            .forEach(em::persist);
    }

    private <T> T pickOne(List<T> list) {
        return list.get(random.nextInt(0, list.size()));
    }

    private String randString() {
        return RandomString.make();
    }

    /**
     * 최대 MAX_HASHTAG_PER_CONTENT 개의 해시태그를 생성함. readable한 해시태그의 위치는 랜덤 생성됨.
     *
     * @return readable한 해시태그가 1개 포함된 해시태그 목록(공백 구분).
     */
    private String randomHashtagsFieldWithOneReadableHashtag() {
        StringBuilder sb = new StringBuilder();

        int totalRandomHashtags = random.nextInt(0, MAX_HASHTAGS_PER_CONTENT);
        int readableHashtagPosition = random.nextInt(0, MAX_HASHTAGS_PER_CONTENT);

        for (int i = 0; i < readableHashtagPosition; i++) {
            sb.append(" ");
            sb.append(randString());
        }

        sb.append(pickOne(readableHashtags));

        for (int i = 0; i < totalRandomHashtags - readableHashtagPosition; i++) {
            sb.append(" ");
            sb.append(randString());
        }
        return sb.toString();
    }

    private ContentSourceType randomSourceType() {
        return contentSourceTypes[random.nextInt(0, contentSourceTypes.length)];
    }

    /**
     * @return 1 ~ 1,000,000 사이의 Long
     */
    private Long randomPositiveLong() {
        return random.nextLong(0, 1000000);
    }

    private Content.ContentBuilder aContent() {
        LocalDateTime createdAt = LocalDateTime.now().minusSeconds(randomPositiveLong());
        return Content.builder()
                   .contentSourceId(randString())
                   .contentSourceType(randomSourceType())
                   .title(randString())
                   .content(randString() + randString() + randString() + randString())
                   .hashtags(randomHashtagsFieldWithOneReadableHashtag())
                   .likeCount(randomPositiveLong())
                   .shareCount(randomPositiveLong())
                   .viewCount(randomPositiveLong())
                   .createdAt(createdAt)
                   .updatedAt(createdAt);
    }
}
