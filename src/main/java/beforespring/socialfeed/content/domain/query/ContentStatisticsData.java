package beforespring.socialfeed.content.domain.query;

import java.util.List;

public record ContentStatisticsData(
    ContentStatisticsQueryParameter parameter,
    int total,
    List<ContentStatisticDataUnit> data
) {

}
