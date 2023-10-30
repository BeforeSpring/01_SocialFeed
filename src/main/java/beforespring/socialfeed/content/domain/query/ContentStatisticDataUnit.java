package beforespring.socialfeed.content.domain.query;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;


@Getter
@EqualsAndHashCode
public final class ContentStatisticDataUnit {
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final long count;

    @Builder
    @QueryProjection
    public ContentStatisticDataUnit(int year, int month, int day, Integer hour, long count) {
        LocalDate localDate = LocalDate.of(year, month, day);
        if (hour.equals(-1)) {
            this.start = localDate.atStartOfDay();
            this.end = this.start.plusDays(1L);
        } else {
            this.start = localDate.atTime(hour, 0);
            this.end = this.start.plusHours(1);
        }
        this.count = count;
    }
}
