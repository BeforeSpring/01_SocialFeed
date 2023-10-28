package beforespring.socialfeed.content.controller;

import beforespring.socialfeed.content.service.ContentCommandService;
import beforespring.socialfeed.content.service.dto.ContentSpecificData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/content")
public class ContentController {
    private final ContentCommandService contentCommandService;
    public ContentController(ContentCommandService contentCommandService) {
        this.contentCommandService = contentCommandService;
    }

    @GetMapping("/{contentId}")
    public ContentSpecificData getContentSpecific(@PathVariable Long contentId) {
        ContentSpecificData contentSpecific = contentCommandService.getContentSpecific(contentId);
        return contentSpecific;
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
}
