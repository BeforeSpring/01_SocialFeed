package beforespring.socialfeed.content.domain.query;

import java.time.LocalDate;
import lombok.Builder;

/**
 * @param hashtag 찾을 해시태그
 * @param granularity HOURLY | DAILY
 * @param start 통계 시작 날짜
 * @param end 통계 종료 날짜
 * @param countValue 집계할 데이터 (태그 수, 좋아요 수, 공유 수...)
 */
public record ContentStatisticsQueryParameter(
    String hashtag,
    Granularity granularity,
    LocalDate start,
    LocalDate end,
    CountValue countValue
) {

    @Builder
    public ContentStatisticsQueryParameter {
    }
}
