package beforespring.socialfeed.member.domain;


import beforespring.socialfeed.member.infra.BcryptHasher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
class MemberTest {

    private PasswordHasher defaultHasher;
    private PasswordHasher bcryptHasher;

    @BeforeEach
    public void setup() {
        defaultHasher = new PasswordHasher() {
            @Override
            public String hash(String toHash) {
                return toHash;
            }

            @Override
            public boolean isMatch(String raw, String hashed) {
                return hash(raw).equals(hashed);
            }
        };

        bcryptHasher = new BcryptHasher();
    }

    @Test
    @DisplayName("빌더를 통해 멤버 생성")
    public void create_member_use_builder() {
        // given

        Member member = Member.builder()
                            .username("User1")
                            .raw("1234")
                            .id(1L)
                            .hasher(defaultHasher)
                            .build();

        // when then
        assertEquals(1L, member.getId());
        assertEquals("User1", member.getUsername());
        assertEquals("1234", member.getPassword());
    }

    @Test
    @DisplayName("다른 hasher를 이용해 빌드를 해도 정상적으로 생성되어야합니다.")
    public void create_member_use_another_hasher() {
        Member member = Member.builder()
                            .username("User1")
                            .raw("1234")
                            .id(1L)
                            .hasher(bcryptHasher)
                            .build();

        assertEquals(1L, member.getId());
        assertEquals("User1", member.getUsername());
        assertTrue(bcryptHasher.isMatch("1234", member.getPassword()));
    }
}