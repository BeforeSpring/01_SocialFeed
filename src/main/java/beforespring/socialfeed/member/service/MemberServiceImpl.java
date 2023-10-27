package beforespring.socialfeed.member.service;

import beforespring.socialfeed.member.controller.dto.CreateMemberDto;
import beforespring.socialfeed.member.domain.*;
import beforespring.socialfeed.member.exception.PasswordMismatchException;
import beforespring.socialfeed.member.exception.TokenMismatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ConfirmRepository confirmRepository;

    @Override
    public Long join(CreateMemberDto.Request request) {
        Member member = Member.create(request.getUsername(), request.getPassword());
        Long memberId = memberRepository.save(member).getId();
        String token = TokenSender.generateToken();
        TokenSender.sendEmail(request.getEmail(), token);
        Confirm confirm = Confirm.builder()
                              .member(member)
                              .token(token)
                              .build();
        confirmRepository.save(confirm);
        return memberId;
    }

    @Override
    public void joinConfirm(String username, String password, String token) {
        Member member = memberRepository.findByUsername(username).orElseThrow(IllegalAccessError::new);
        Confirm confirm = confirmRepository.findByMember(member);
        PasswordHasher passwordHasher = new PasswordHasherImpl();
        if (!passwordHasher.matches(password, member.getPassword()))
            throw new PasswordMismatchException();
        if (!confirm.getToken().equals(token))
            throw new TokenMismatchException();
        //member 승인 상태 변경 필요
    }
}
