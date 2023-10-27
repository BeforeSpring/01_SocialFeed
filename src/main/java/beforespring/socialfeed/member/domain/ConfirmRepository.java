package beforespring.socialfeed.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmRepository extends JpaRepository<Confirm, Long> {
    Confirm findByMember(Member member);
}
