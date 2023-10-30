package beforespring.socialfeed.web.api.v1.member;

import beforespring.socialfeed.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static beforespring.socialfeed.member.service.dto.ConfirmTokenDto.*;
import static beforespring.socialfeed.member.service.dto.CreateMemberDto.CreateMemberRequest;
import static beforespring.socialfeed.member.service.dto.CreateMemberDto.CreateMemberResponse;

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
    public CreateMemberResponse createMember(@RequestBody @Valid CreateMemberRequest request) {
        Long memberId = memberService.join(request);
        return new CreateMemberResponse(memberId);
    }

    /**
     * 가입 승인 요청시 사용될 메서드입니다.
     * request로 받은 유저의 정보를 토대로
     * joinConfirm service를 호출합니다.
     *
     * @param request
     */
    @PostMapping("/api/member/confirm")
    public void signupMember(@RequestBody @Valid ConfirmTokenRequest request) {
        memberService.joinConfirm(request);
    }
}