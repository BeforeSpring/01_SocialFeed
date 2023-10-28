package beforespring.socialfeed.member.service;

import beforespring.socialfeed.member.domain.*;
import beforespring.socialfeed.member.exception.PasswordMismatchException;
import beforespring.socialfeed.member.exception.TokenMismatchException;
import beforespring.socialfeed.member.infra.BcryptHasher;
import beforespring.socialfeed.member.infra.TokenSenderImpl;
import beforespring.socialfeed.member.service.exception.ConfirmNotFoundException;
import beforespring.socialfeed.member.service.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static beforespring.socialfeed.member.controller.dto.ConfirmTokenDto.ConfirmTokenRequest;
import static beforespring.socialfeed.member.controller.dto.CreateMemberDto.CreateMemberRequest;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ConfirmRepository confirmRepository;
//    private final PasswordHashe passwordhasher;
    //private final TokenSender tokenSender;

    @Override
    public Long join(CreateMemberRequest request) {
        //추후 주입 받는 것으로 변경
        PasswordHasher passwordHasher = new BcryptHasher();
        TokenSender tokenSender = new TokenSenderImpl();

        Member member = Member.builder()
                            .username(request.getUsername())
                            .raw(request.getPassword())
                            .hasher(passwordHasher)
                            .build();
        Long memberId = saveMember(member);

        String token = tokenSender.generateToken();

        Confirm confirm = Confirm.builder()
                              .member(member)
                              .token(token)
                              .build();
        saveConfirm(confirm);
        tokenSender.sendEmail(request.getEmail(), token);
        return memberId;
    }

    @Transactional
    public Long saveMember(Member member) {
        return memberRepository.save(member).getId();
    }

    @Transactional
    public Long saveConfirm(Confirm confirm) {
        return confirmRepository.save(confirm).getId();
    }

    @Override
    @Transactional
    public void joinConfirm(ConfirmTokenRequest request) {
        Member member = memberRepository.findByUsername(request.getUsername()).orElseThrow(MemberNotFoundException::new);
        Confirm confirm = confirmRepository.findByMember(member).orElseThrow(ConfirmNotFoundException::new);
        //추후 주입 받는 것으로 변경
        PasswordHasher passwordHasher = new BcryptHasher();
        member.verifyPassword(request.getPassword(), passwordHasher);
        confirm.verifyToken(request.getToken());
        member.joinConfirm();
    }
}
