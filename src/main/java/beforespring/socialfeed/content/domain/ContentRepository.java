package beforespring.socialfeed.content.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    /**
     * 비관적 락을 사용하여 동시 업데이트 문제 방지
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Content> findById(Long id);
}
