package beforespring.socialfeed.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("빌더를 통해 멤버 생성")
    public void create_member_use_builder() {
        // given
        PasswordHasher hasher = new PasswordHasherImpl();
        Member member = Member.builder()
                            .username("User1")
                            .raw("1234")
                            .id(1L)
                            .hasher(hasher)
                            .build();

        // when then
        assertEquals(1L, member.getId());
        assertEquals("User1", member.getUsername());
        assertEquals("1234", member.getPassword());
    }
}