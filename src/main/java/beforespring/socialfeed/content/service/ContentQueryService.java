package beforespring.socialfeed.content.service;

import beforespring.socialfeed.content.service.dto.ContentListElement;
import beforespring.socialfeed.content.service.dto.ContentListQueryParameter;
import beforespring.socialfeed.content.service.dto.ContentStatisticsData;
import beforespring.socialfeed.content.service.dto.ContentStatisticsQueryParameter;
import java.util.List;
import org.springframework.data.domain.Page;

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
     * <p>Spring 프레임워크에서 제공하는 Page 인터페이스 활용.</p>
     *
     * @param contentListQueryParameter
     * @return 페이지 정보와 ContentListElement가 포함된 Page 객체
     */
    Page<ContentListElement> getContentList(ContentListQueryParameter queryParameter);

    /**
     * <p>통계 API</p>
     *
     * @param queryParameter
     * @return
     */
    ContentStatisticsData getContentStatistics(ContentStatisticsQueryParameter queryParameter);

    /**
     * 3시간 동안 가장 많이 사용된 Tag 추천
     *
     * @return
     */
    List<String> hotHashtags();

    /**
     * 단기간 내 조회수 급상승
     *
     * @return
     */
    List<ContentListElement> onFire();
}
