package beforespring.socialfeed.content.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HashtagContentRepository extends JpaRepository<HashtagContent, Long> {
    Optional<HashtagContent> findByContent(Content content);
}
