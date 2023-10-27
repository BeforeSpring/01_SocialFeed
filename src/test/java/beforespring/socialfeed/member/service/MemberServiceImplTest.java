package beforespring.socialfeed.member.service;

import beforespring.socialfeed.member.domain.Confirm;
import beforespring.socialfeed.member.domain.ConfirmRepository;
import beforespring.socialfeed.member.domain.Member;
import beforespring.socialfeed.member.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static beforespring.socialfeed.member.controller.dto.CreateMemberDto.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceImplTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired ConfirmRepository confirmRepository;

    @Test
    @DisplayName("회원가입한 유저의 이름이 저장된 이름과 동일해야 함.")
    void member_join_test() {
        //given
        String givenUsername = "givenUsername";
        String givenEmail = "givenEmail@gmail.com";
        String givenPassword = "passwdThatShou!dBe0kay";
        Request request = new Request(givenUsername, givenEmail, givenPassword);

        //when
        Long memberId = memberService.join(request);
        Member findMember = memberRepository.findById(memberId).orElseThrow(IllegalAccessError::new);

        //then
        assertThat(findMember.getUsername()).isEqualTo(givenUsername);
    }

    @Test
    @DisplayName("confirm에 6자리 승인코드가 저장되어야 함.")
    void create_confirm_test() {
        //given
        String givenUsername = "givenUsername";
        String givenEmail = "givenEmail@gmail.com";
        String givenPassword = "passwdThatShou!dBe0kay";
        Request request = new Request(givenUsername, givenEmail, givenPassword);

        //when
        Long memberId = memberService.join(request);
        Member findMember = memberRepository.findById(memberId).orElseThrow(IllegalAccessError::new);
        Confirm findConfirm = confirmRepository.findByMember(findMember);

        //then
        assertThat(findConfirm.getToken().length()).isEqualTo(6);

    }

}