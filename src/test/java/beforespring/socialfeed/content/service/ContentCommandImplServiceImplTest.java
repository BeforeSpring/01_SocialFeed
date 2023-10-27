package beforespring.socialfeed.content.service;

import beforespring.socialfeed.content.domain.*;
import beforespring.socialfeed.content.service.dto.ContentSpecificData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContentCommandImplServiceImplTest {
    @InjectMocks
    private ContentCommandImplServiceImpl contentService;

    @Mock
    private ContentRepository contentRepository;

    @Mock
    private HashtagContentRepository hashtagContentRepository;

    @Test
    @DisplayName("getContentSpecific 테스트")
    public void testGetContentSpecific() {

        Content content = Content.builder()
            .id(1L)
            .contentSourceType(ContentSourceType.FACEBOOK)
            .title("test title")
            .content("test content")
            .viewCount(10L)
            .likeCount(3L)
            .shareCount(5L)
            .hashtags("태그1 태그2 태그3")
            .build();

        ContentSpecificData contentSpecificData = ContentSpecificData.builder()
            .contentId(1L)
            .contentSourceType(ContentSourceType.FACEBOOK)
            .title("test title")
            .content("test content")
            .viewCount(11L)
            .likeCount(3L)
            .hashtags(Arrays.asList("태그1", "태그2", "태그3"))
            .shareCount(5L)
            .updatedAt(content.getUpdatedAt())
            .createdAt(content.getCreatedAt())
            .build();

        when(contentRepository.findById(1L)).thenReturn(Optional.of(content));

        ContentCommandService contentCommandService = new ContentCommandImplServiceImpl(contentRepository, hashtagContentRepository);

        ContentSpecificData result = contentCommandService.getContentSpecific(1L);
        System.out.println(result);
        assertEquals(contentSpecificData, result);
    }

    @Test
    @DisplayName("좋아요 처리 테스트")
    void testLike() {
        Content content = Content.builder()
            .id(1L)
            .build();

        when(contentRepository.findById(1L)).thenReturn(Optional.of(content));

        contentService.like(1L);

        assertEquals(1L, content.getLikeCount());
    }

    @Test
    @DisplayName("공유 처리 테스트")
    void testShare() {
        Content content = Content.builder()
            .id(1L)
            .shareCount(2L)
            .build();

        when(contentRepository.findById(1L)).thenReturn(Optional.of(content));
        contentService.share(1L);
        assertEquals(3L, content.getShareCount());
    }
}