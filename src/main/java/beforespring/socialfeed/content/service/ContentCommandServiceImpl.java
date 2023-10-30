package beforespring.socialfeed.content.service;

import beforespring.socialfeed.content.domain.*;
import beforespring.socialfeed.content.service.dto.ContentSpecificData;
import beforespring.socialfeed.content.service.exception.ContentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ContentCommandServiceImpl implements ContentCommandService {
    private final ContentRepository contentRepository;
    private final ExternalApiHandlerResolver externalApiHandlerResolver;

    @Override
    public ContentSpecificData getContentSpecific(Long contentId) {
        Content content = contentRepository.findById(contentId).orElseThrow(() -> new ContentNotFoundException());
        content.incrementViewCount();



        ContentSpecificData contentData = ContentSpecificData.builder()
            .contentId(contentId)
            .contentSourceType(content.getContentSourceType())
            .title(content.getTitle())
            .content(content.getContent())
            .viewCount(content.getViewCount())
            .likeCount(content.getLikeCount())
            .hashtags(content.getHashtagsList())
            .shareCount(content.getShareCount())
            .updatedAt(content.getUpdatedAt())
            .createdAt(content.getCreatedAt())
            .build();

        return contentData;
    }


    @Override
    public void like(Long contentId) {
        Content content = contentRepository.findById(contentId)
            .orElseThrow(() -> new ContentNotFoundException());

        ExternalApiHandler externalApiHandler = externalApiHandlerResolver.resolveHandler(content.getContentSourceType());

        content.incrementLikeCount();
        externalApiHandler.like(content.getContentSourceId());
    }

    @Override
    public void share(Long contentId) {
        Content content = contentRepository.findById(contentId)
            .orElseThrow(() -> new ContentNotFoundException());

        ExternalApiHandler externalApiHandler = externalApiHandlerResolver.resolveHandler(content.getContentSourceType());

        content.incrementShareCount();
        externalApiHandler.share(content.getContentSourceId());
    }
}

