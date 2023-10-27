package beforespring.socialfeed.content.controller;

import beforespring.socialfeed.content.service.ContentCommandService;
import beforespring.socialfeed.content.service.dto.ContentSpecificData;
import beforespring.socialfeed.content.service.exception.ContentNotFoundException;
import beforespring.socialfeed.content.service.exception.HashtagNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getContentSpecific(@PathVariable Long contentId) {
        try {
            ContentSpecificData contentData = contentCommandService.getContentSpecific(contentId);
            return ResponseEntity.ok(contentData);
        } catch (ContentNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (HashtagNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("해시태그 정보를 가져오는 중 오류 발생");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시물 상세 정보를 가져오는 중 오류 발생");
        }
    }


    /**
     *
     게시물 좋아요 클릭 API
      */
    @PostMapping("/{contentId}/like")
    public ResponseEntity<String> likeContent(@PathVariable Long contentId) {
        try {
            contentCommandService.like(contentId);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시물 좋아요 처리 중 오류 발생");
        }
    }

    /**
     *
     게시물 공유 API
     */
    @PostMapping("/{contentId}/share")
    public ResponseEntity<String> shareContent(@PathVariable Long contentId) {
        try {
            contentCommandService.share(contentId);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시물 공유 처리 중 오류 발생");
        }
    }
}
