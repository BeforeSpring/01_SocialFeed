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

    @Test
    @DisplayName("멤버 객체 저장")
    public void save_member() {
        // given
        PasswordHasher hasher = new PasswordHasherImpl();
        Member member = Member.builder()
                            .username("User1")
                            .raw("1234")
                            .id(1L)
                            .hasher(hasher)
                            .build();

        memberRepository.save(member);

        // when & then
        Member findMember = memberRepository.findById(1L).orElseGet(Member::new);
        System.out.println(findMember.toString());

        assertEquals(member.getUsername(), findMember.getUsername(), "빌드된 데이터의 유저 정보가 User1으로 잘 불러와져야됩니다.");
    }

    @Test
    @DisplayName("존재하지 않는 멤버 조회")
    public void find_non_existent_member() {
        // given
        long nonExistentId = 999L;

        // when
        Optional<Member> findMember = memberRepository.findById(nonExistentId);

        // then
        assertTrue(findMember.isEmpty(), "존재하지 않는 멤버 ID로 조회하면 결과가 없어야 합니다.");
    }
}