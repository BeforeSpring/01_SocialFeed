package beforespring.socialfeed.content.domain;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

/**
 * Content QueryRepository의 결과물을 래핑할 목적으로 만들어진 클래스.
 * @param lastContentCreatedAt 무한스크롤을 위해 사용되는 기준점. (이전 쿼리 결과의 게시물 게시 시점)
 * @param contents             쿼리 결과
 * @param size                 쿼리 결과 수량
 * @param offsetRequiredNext   이전 쿼리 마지막 Content의 createdAt 과 동일한 다른 게시물이 여럿 존재하는 상황. 만약 true인 경우,
 *                             offset을 계산해서 쿼리해야함.
 * @param hasNext              불러오지 않은 컨텐츠가 더 있는지 여부
 * @see ContentQueryParameter
 */
public record ContentQueryResult<T>(
    LocalDateTime lastContentCreatedAt,
    List<T> contents,
    int size,
    boolean offsetRequiredNext,
    boolean hasNext
) {
    @Builder
    public ContentQueryResult {
    }
}
