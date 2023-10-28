package beforespring.socialfeed.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmRepository extends JpaRepository<Confirm, Long> {
    Optional<Confirm> findByMember(Member member);
}
