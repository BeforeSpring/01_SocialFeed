package beforespring.socialfeed.content.service;

import beforespring.socialfeed.content.domain.ContentIdTuple;
import beforespring.socialfeed.content.service.dto.ContentSpecificData;
import beforespring.socialfeed.member.domain.Member;

public interface ContentCommandService {

    /**
     * <p>
     * 유저가 게시물을 클릭 시 사용되는 API
     * </p>
     * <li>
     * 모든 필드 값을 확인 합니다.
     * </li>
     * <li>
     * API 호출 시, 해당 게시물 `view_count` 가 1 증가합니다.
     * </li>
     * <li>
     * 횟수 제한이 없습니다.
     * </li>
     *
     * @param contentIdTuple 찾을 게시물 ID
     * @return ContentSpecificData
     */
    ContentSpecificData getContentSpecific(ContentIdTuple contentIdTuple);

    /**
     * <p>
     * 게시물 목록 또는 상세 에서 게시물 `좋아요` 클릭 시 사용되는 API
     * </p>
     * <li>
     * 게시물들은 본 서비스가 아닌 외부 서비스에서 관리됩니다. 그렇기에 좋아요 클릭 시 각 SNS 별 아래 명시된 API 를 호출합니다.
     * </li>
     *
     * @param contentIdTuple 좋아요할 게시물 ID
     * @param member         공유하는 멤버
     */
    void like(ContentIdTuple contentIdTuple, Member member);

    /**
     * <p>
     * 게시물 목록 또는 상세 에서 공유하기 클릭 시 사용되는 API
     * </p>
     * <li>
     * 게시물들은 본 서비스가 아닌 외부 서비스에서 관리됩니다. 그렇기에 좋아요 클릭 시 각 SNS 별 아래 명시된 API 를 호출합니다.
     * </li>
     * <li>
     * 해당 호출이 성공할 시 해당 게시물의 shareCount가 1 증가합니다.
     * </li>
     * <li>
     * 횟수 제한이 없습니다.
     * </li>
     *
     * @param contentIdTuple 공유할 게시물 아이디
     * @param member         공유하는 멤버
     */
    void share(ContentIdTuple contentIdTuple, Member member);
}
