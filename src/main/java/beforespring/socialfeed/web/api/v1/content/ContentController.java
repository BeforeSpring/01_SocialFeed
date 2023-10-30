package beforespring.socialfeed.web.api.v1.content;

import beforespring.socialfeed.content.domain.ContentQueryParameter;
import beforespring.socialfeed.content.domain.ContentQueryResult;
import beforespring.socialfeed.content.domain.query.ContentStatisticsData;
import beforespring.socialfeed.content.domain.query.ContentStatisticsQueryParameter;
import beforespring.socialfeed.content.domain.query.CountValue;
import beforespring.socialfeed.content.domain.query.Granularity;
import beforespring.socialfeed.content.service.ContentCommandService;
import beforespring.socialfeed.content.service.ContentQueryService;
import beforespring.socialfeed.content.service.dto.ContentListElement;
import beforespring.socialfeed.content.service.dto.ContentSpecificData;
import beforespring.socialfeed.web.memberinforesolver.MemberInfo;
import beforespring.socialfeed.web.memberinforesolver.ResolveMemberInfo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/content")
public class ContentController {
    private final ContentCommandService contentCommandService;

    private final ContentQueryService contentQueryService;

    public ContentController(ContentCommandService contentCommandService, ContentQueryService contentQueryService) {
        this.contentCommandService = contentCommandService;
        this.contentQueryService = contentQueryService;
    }

    @GetMapping("/{contentId}")
    public ContentSpecificData getContentSpecific(@PathVariable Long contentId) {
        return contentCommandService.getContentSpecific(contentId);
    }


    /**
     *
     게시물 좋아요 클릭 API
      */
    @PostMapping("/{contentId}/like")
    public void likeContent(@PathVariable Long contentId) {
        contentCommandService.like(contentId);
    }

    /**
     *
     게시물 공유 API
     */
    @PostMapping("/{contentId}/share")
    public void shareContent(@PathVariable Long contentId) {
        contentCommandService.share(contentId);
    }

    @GetMapping
    public ContentQueryResult<ContentListElement> postList(
        @ResolveMemberInfo MemberInfo memberInfo,
        @RequestParam(required = false) String hashtag,
        @RequestParam(defaultValue = "20") Integer size,
        @RequestParam(defaultValue = "0") Integer offset,
        @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime from
    ) {
        if (hashtag == null) {
            hashtag = memberInfo.username();
        }
        return contentQueryService.getContentList(ContentQueryParameter.builder()
                                                      .hashtag(hashtag)
                                                      .size(size)
                                                      .offset(offset)
                                                      .from(from)
                                                      .build());
    }

    @GetMapping("/statistics")
    public ContentStatisticsData statistics(
        @ResolveMemberInfo MemberInfo memberInfo,
        @RequestParam(required = false) String hashtag,
        @RequestParam Granularity type,
        @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate start,
        @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate end,
        @RequestParam(defaultValue = "COUNT") CountValue value
    ) {
        if (hashtag == null) {
            hashtag = memberInfo.username();
        }

        if (start == null) {
            start = LocalDate.now().minusDays(7);
            end = LocalDate.now();
        }

        return contentQueryService.getContentStatistics(
            ContentStatisticsQueryParameter.builder()
                .hashtag(hashtag)
                .granularity(type)
                .start(start)
                .end(end)
                .countValue(value)
                .build());
    }
}
