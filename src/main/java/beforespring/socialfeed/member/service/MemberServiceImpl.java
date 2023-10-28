package beforespring.socialfeed.member.service;

import beforespring.socialfeed.member.domain.*;
import beforespring.socialfeed.member.exception.PasswordMismatchException;
import beforespring.socialfeed.member.exception.TokenMismatchException;
import beforespring.socialfeed.member.infra.TokenSenderImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static beforespring.socialfeed.member.controller.dto.ConfirmTokenDto.ConfirmTokenRequest;
import static beforespring.socialfeed.member.controller.dto.CreateMemberDto.CreateMemberRequest;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ConfirmRepository confirmRepository;
    //private final PasswordHashe passwordhasher;
    //private final TokenSender tokenSender;

    @Override
    public Long join(CreateMemberRequest request) {
        //추후 주입 받는 것으로 변경
        PasswordHasher passwordHasher = new PasswordHasherImpl();
        TokenSender tokenSender = new TokenSenderImpl();

        Member member = Member.builder()
                            .username(request.getUsername())
                            .raw(request.getPassword())
                            .hasher(passwordHasher)
                            .build();
        Long memberId = memberRepository.save(member).getId();
        String token = tokenSender.generateToken();
        tokenSender.sendEmail(request.getEmail(), token);
        Confirm confirm = Confirm.builder()
                              .member(member)
                              .token(token)
                              .build();
        confirmRepository.save(confirm);
        return memberId;
    }

    @Override
    public void joinConfirm(ConfirmTokenRequest request) {
        Member member = memberRepository.findByUsername(request.getUsername()).orElseThrow(IllegalAccessError::new);
        Confirm confirm = confirmRepository.findByMember(member);
        //추후 주입 받는 것으로 변경
        PasswordHasher passwordHasher = new PasswordHasherImpl();
        if (!passwordHasher.matches(request.getPassword(), member.getPassword()))
            throw new PasswordMismatchException();
        if (!confirm.getToken().equals(request.getToken()))
            throw new TokenMismatchException();
        member.joinConfirm();
    }
}
