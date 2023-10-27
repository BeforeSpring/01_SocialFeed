package beforespring.socialfeed.content.domain;


import java.time.LocalDateTime;
import lombok.Builder;


/**
 * @param hashtag 찾으려는 해시태그
 * @param from    이 시점 이후에 생성된 content를 가져오게 됨.
 * @param size    가져올 Content의 수
 * @param offset  지정한 offset만큼 건너 뛰어서 값을 반환함. 이전에 offset이 필요하다는 응답이 온 경우, offset을 추가해야 같은 정보를 계속해서
 *                불러오는 일을 방지할 수 있음. 클라이언트 사이드에서 offset을 계산해야함.
 * @see ContentQueryResult
 */
public record ContentQueryParameter(
    String hashtag,
    LocalDateTime from,
    int size,
    int offset
) {

    @Builder
    public ContentQueryParameter {
    }
}
