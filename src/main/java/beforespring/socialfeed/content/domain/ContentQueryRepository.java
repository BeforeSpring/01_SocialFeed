package beforespring.socialfeed.content.domain;

import java.util.List;


public interface ContentQueryRepository {

    /**
     * <p>
     * 무한스크롤을 염두에 둔 Content 조회 메서드.
     * </p>
     * <p>
     * 서비스의 특성상, 새로운 게시물이 자주 올라올 수 있음.<br> 만약 단순 offset으로 구현한다면, 동일한 게시물이 중복으로 나타나는 일이 자주 발생할 것임.
     * 따라서 특정 게시물을 anchor로 삼고, 그 이후의 데이터를 가져와야함. (보통 incremental하거나 시간 기반으로 생성된 PK를 바탕으로 acnchor를
     * 설정함.)<br> 그러나, 본 PK서비스의 특성상 PK를 anchor로 사용할 수가 없음. (외부 SNS에서 가져오는 것이고, SNS 전체 데이터를 가져오는 것은
     * 불가능함.)
     * </p>
     * <p>
     * 마지막 게시물이 올라온 시각에 대한 정보를 바탕으로, 그 이후에 올라온 게시물들을 불러오게 됨.
     * </p>
     *
     * @param queryParameter ContentQueryParameter 참조.
     * @return list of contents (최대 size: queryParameter.size + 1)
     * @see ContentQueryParameter
     */
    List<Content> findByHashtag(ContentQueryParameter queryParameter);
}
