package beforespring.socialfeed.content.service;

import beforespring.socialfeed.content.domain.Content;
import beforespring.socialfeed.content.domain.ContentRepository;
import beforespring.socialfeed.content.domain.ContentSourceType;
import beforespring.socialfeed.content.service.dto.ContentSpecificData;
import beforespring.socialfeed.content.service.exception.ContentNotFoundException;
import beforespring.socialfeed.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ContentCommandImplServiceImpl implements ContentCommandService {
    private final ContentRepository contentRepository;

    @Override
    public ContentSpecificData getContentSpecific(Long contentId) {
        Optional<Content> optionalContent = contentRepository.findById(contentId);

        if (optionalContent.isPresent()) {
            Content content = optionalContent.get();

            content.incrementViewCount();

            contentRepository.save(content);

            ContentSpecificData contentData = new ContentSpecificData(
                contentId,
                content.getContentSourceType(),
                content.getTitle(),
                content.getContent(),
                content.getViewCount(),
                content.getLikeCount(),
                content.getShareCount(),
                content.getUpdatedAt(),
                content.getCreatedAt()
            );

            return contentData;
        } else {
            throw new ContentNotFoundException();
        }
    }


    @Override
    public void like(Long contentId, Member member) {
        Content content = contentRepository.findById(contentId)
            .orElseThrow(() -> new ContentNotFoundException());

        content.incrementLikeCount();

        contentRepository.save(content);
    }

    @Override
    public void share(Long contentId, Member member) {
        Content content = contentRepository.findById(contentId)
            .orElseThrow(() -> new ContentNotFoundException());

        content.incrementShareCount();

        contentRepository.save(content);
    }
}

