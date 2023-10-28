package beforespring.socialfeed.content.service;

import beforespring.socialfeed.content.domain.ContentQueryParameter;
import beforespring.socialfeed.content.domain.ContentQueryResult;
import beforespring.socialfeed.content.service.dto.ContentListElement;
import beforespring.socialfeed.content.service.dto.ContentStatisticsData;
import beforespring.socialfeed.content.service.dto.ContentStatisticsQueryParameter;
import java.util.List;

public interface ContentQueryService {

    /**
     * <p>Feed에 나타나는 게시물 목록 API</p>
     * <p>
     * 요구사항
     * <li>
     * 게시물 목록 API에선 content 는 최대 20자 까지만 포함됩니다.
     * </li>
     * </p>
     *
     *
     * @param queryParameter
     * @return 페이지 정보와 ContentListElement가 포함된 Page 객체
     * @see ContentQueryParameter
     * @see ContentQueryResult
     */
    ContentQueryResult<ContentListElement> getContentList(ContentQueryParameter queryParameter);

    /**
     * <p>통계 API</p>
     *
     * @param queryParameter
     * @return
     */
    ContentStatisticsData getContentStatistics(ContentStatisticsQueryParameter queryParameter);

    /**
     * 일정 기간 내 가장 많이 사용된 해시태그 목록
     * @param minutes
     * @param howMany
     * @return minutes 분 이내 가장 많이 사용된 해시태그 중, 상위 howMany 개 만큼 반환함. 사용수 기준으로 정렬.
     */
    List<String> hotHashtags(long minutes, int howMany);

    /**
     * 단기간 내 조회수 급상승
     *
     * @return
     */
    List<ContentListElement> onFire();
}
