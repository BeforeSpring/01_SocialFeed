package beforespring.socialfeed.member.controller;

import beforespring.socialfeed.member.controller.dto.CreateMemberDto;
import beforespring.socialfeed.member.controller.dto.SignupMemberDto;
import beforespring.socialfeed.member.domain.Member;
import beforespring.socialfeed.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 멤버 생성. 가입 요청과 가입 승인 서비스를 호출합니다.
     *
     * @param request 멤버 생성을 위한 dto
     * @return 생성된 멤버의 아이디를 반환합니다.
     */
    @PostMapping("/api/member/new")
    public CreateMemberDto.Response createMember(@RequestBody @Valid CreateMemberDto.Request request) {
        Long memberId = memberService.join(request);
        return new CreateMemberDto.Response(memberId);
    }

    @PostMapping("/api/member/signup/{token}")
    public void signupMember(@RequestBody @Valid SignupMemberDto.Request request, @PathVariable("token") Long token) {
        Member member = Member.builder()

        memberService.joinConfirm();
    }
}
