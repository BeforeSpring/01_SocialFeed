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

    /**
     * 기능만 우선 구현함.
     * 부하를 고려하여 일정 시간마다 쿼리를 캐싱할 필요가 있음.
     *
     * @return
     */
    @Override
    public List<String> hotHashtags(long minutes, int howMany) {
        return contentQueryRepository.findMostPopularHashtagsIn(minutes, howMany);
    }

    @Override
    public List<ContentListElement> onFire() {
        return null;
    }
}
