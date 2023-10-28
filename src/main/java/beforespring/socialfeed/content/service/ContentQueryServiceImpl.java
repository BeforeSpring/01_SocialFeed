package beforespring.socialfeed.content.service;

import beforespring.socialfeed.content.domain.ContentQueryParameter;
import beforespring.socialfeed.content.domain.ContentQueryRepository;
import beforespring.socialfeed.content.domain.ContentQueryResult;
import beforespring.socialfeed.content.service.dto.ContentListElement;
import beforespring.socialfeed.content.service.dto.ContentStatisticsData;
import beforespring.socialfeed.content.service.dto.ContentStatisticsQueryParameter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContentQueryServiceImpl implements ContentQueryService {

    private final ContentQueryRepository contentQueryRepository;

    private final ContentMapper contentMapper;

    @Override
    public ContentQueryResult<ContentListElement> getContentList(
        ContentQueryParameter queryParameter
    ) {
        return contentQueryRepository.findByHashtag(queryParameter)
                   .map(contentMapper::mapToContentListElement);
    }

    @Override
    public ContentStatisticsData getContentStatistics(
        ContentStatisticsQueryParameter queryParameter
    ) {
        return null;
    }

    @Override
    public List<String> hotHashtags() {
        return null;
    }

    @Override
    public List<ContentListElement> onFire() {
        return null;
    }
}
