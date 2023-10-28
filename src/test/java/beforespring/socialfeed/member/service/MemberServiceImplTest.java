package beforespring.socialfeed.member.service;

import beforespring.socialfeed.member.domain.*;
import beforespring.socialfeed.member.exception.PasswordMismatchException;
import beforespring.socialfeed.member.exception.TokenMismatchException;
import beforespring.socialfeed.member.infra.TokenSenderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static beforespring.socialfeed.member.controller.dto.ConfirmTokenDto.ConfirmTokenRequest;
import static beforespring.socialfeed.member.controller.dto.CreateMemberDto.CreateMemberRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class MemberServiceImplTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ConfirmRepository confirmRepository;

    private CreateMemberRequest createMemberRequest;
    private ConfirmTokenRequest confirmTokenRequest;

    @Test
    @DisplayName("회원가입한 유저의 이름이 저장된 이름과 동일해야 함.")
    void member_join_test() {
        //given
        String givenUsername = "givenUsername";
        String givenEmail = "givenEmail@gmail.com";
        String givenPassword = "passwdThatShou!dBe0kay";
        CreateMemberRequest request = new CreateMemberRequest(givenUsername, givenEmail, givenPassword);

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
        CreateMemberRequest request = new CreateMemberRequest(givenUsername, givenEmail, givenPassword);

        //when
        Long memberId = memberService.join(request);
        Member findMember = memberRepository.findById(memberId).orElseThrow(IllegalAccessError::new);
        Confirm findConfirm = confirmRepository.findByMember(findMember);

        //then
        assertThat(findConfirm.getToken().length()).isEqualTo(6);

    }

    @BeforeEach
    public void setup() {
        String givenUsername = "givenUsername";
        String givenEmail = "givenEmail@gmail.com";
        String givenPassword = "passwdThatShou!dBe0kay";

        createMemberRequest = new CreateMemberRequest(givenUsername, givenEmail, givenPassword);


    }

    @Test
    @DisplayName("가입 승인 요청의 토큰과 저장된 토큰이 일치하면 멤버가 승인 상태가 되어야 합니다.")
    void join_confirm_test() {
        //given
        Long memberId = memberService.join(createMemberRequest);
        Member findMember = memberRepository.findById(memberId).orElseThrow(IllegalAccessError::new);
        Confirm findConfirm = confirmRepository.findByMember(findMember);

        //when
        String token = findConfirm.getToken();
        confirmTokenRequest = new ConfirmTokenRequest(findMember.getUsername(), findMember.getPassword(), token);
        memberService.joinConfirm(confirmTokenRequest);

        //then
        assertThat(findMember.getStatus()).isEqualTo(ConfirmStatus.AUTHORIZED);
    }

    @Test
    @DisplayName("가입 승인 요청의 토큰과 저장된 토큰이 일치하지 않으면 가입 승인 요청이 실패해야 합니다.")
    void join_confirm_deny_test() {
        //given
        Long memberId = memberService.join(createMemberRequest);
        Member findMember = memberRepository.findById(memberId).orElseThrow(IllegalAccessError::new);
        Confirm findConfirm = confirmRepository.findByMember(findMember);
        TokenSender tokenSender = new TokenSenderImpl();

        //when //then
        String newToken = tokenSender.generateToken();
        confirmTokenRequest = new ConfirmTokenRequest(findMember.getUsername(), findMember.getPassword(), newToken);

        assertThatThrownBy(() -> memberService.joinConfirm(confirmTokenRequest))
            .isInstanceOf(TokenMismatchException.class)
            .describedAs("인증 토큰이 일치하지 않으면 가입 요청에 실패해야 합니다.");
    }

    @Test
    @DisplayName("가입 승인 요청 시 저장된 계정이 없으면 가입 승인 요청이 실패해야 합니다.")
    void join_confirm_no_user_test() {
        //given
        Long memberId = memberService.join(createMemberRequest);
        Member findMember = memberRepository.findById(memberId).orElseThrow(IllegalAccessError::new);
        Confirm findConfirm = confirmRepository.findByMember(findMember);
        String token = findConfirm.getToken();

        //when //then
        String wrongUsername = "aaa";
        confirmTokenRequest = new ConfirmTokenRequest(wrongUsername, findMember.getPassword(), token);

        assertThatThrownBy(() -> memberService.joinConfirm(confirmTokenRequest))
            .isInstanceOf(IllegalAccessError.class)
            .describedAs("계정이 일치하지 않으면 가입 요청에 실패해야 합니다.");

    }

    @Test
    @DisplayName("가입 승인 요청 시 저장된 멤버와 비밀번호가 일치하지 않으면 가입 승인 요청이 실패해야 합니다.")
    void join_confirm_mismatch_password_test() {
        //given
        Long memberId = memberService.join(createMemberRequest);
        Member findMember = memberRepository.findById(memberId).orElseThrow(IllegalAccessError::new);
        Confirm findConfirm = confirmRepository.findByMember(findMember);
        String token = findConfirm.getToken();

        //when //then
        String wrongPassword = "aaa";
        confirmTokenRequest = new ConfirmTokenRequest(findMember.getUsername(), wrongPassword, token);

        assertThatThrownBy(() -> memberService.joinConfirm(confirmTokenRequest))
            .isInstanceOf(PasswordMismatchException.class)
            .describedAs("비밀번호가 일치하지 않으면 가입 요청에 실패해야 합니다.");
    }

}