package beforespring.socialfeed.member.controller;

import beforespring.socialfeed.member.controller.dto.CreateMemberDto;
import beforespring.socialfeed.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    @PostMapping("/api/member/new")
    public CreateMemberDto.Response createMember(@RequestBody @Valid CreateMemberDto.Request request) {
        Long requestId = memberService.join(request);
        return new CreateMemberDto.Response(requestId);
    }
}
