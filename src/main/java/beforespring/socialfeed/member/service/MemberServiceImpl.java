package beforespring.socialfeed.member.service;

import beforespring.socialfeed.member.domain.*;
import beforespring.socialfeed.member.service.exception.ConfirmNotFoundException;
import beforespring.socialfeed.member.service.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import static beforespring.socialfeed.member.controller.dto.ConfirmTokenDto.ConfirmTokenRequest;
import static beforespring.socialfeed.member.controller.dto.CreateMemberDto.CreateMemberRequest;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ConfirmRepository confirmRepository;
    private final PasswordHasher passwordHasher;
    private final TokenSender tokenSender;
    private final TransactionTemplate transactionTemplate;

    @Override
    public Long join(CreateMemberRequest request) {

        String token = tokenSender.generateToken();
        Long memberId = transactionTemplate.execute(transactionStatus -> {
            Member member = Member.builder()
                                .username(request.getUsername())
                                .raw(request.getPassword())
                                .hasher(passwordHasher)
                                .build();
            memberRepository.save(member);
            Confirm confirm = Confirm.builder()
                                  .member(member)
                                  .token(token)
                                  .build();
            confirmRepository.save(confirm);

            return member.getId();
        });

        tokenSender.sendEmail(request.getEmail(), token);

        return memberId;
    }

    @Override
    @Transactional
    public void joinConfirm(ConfirmTokenRequest request) {
        Member member = memberRepository.findByUsername(request.getUsername()).orElseThrow(MemberNotFoundException::new);
        Confirm confirm = confirmRepository.findByMember(member).orElseThrow(ConfirmNotFoundException::new);
        member.verifyPassword(request.getPassword(), passwordHasher);
        confirm.verifyToken(request.getToken());
        member.joinConfirm();
    }
}
