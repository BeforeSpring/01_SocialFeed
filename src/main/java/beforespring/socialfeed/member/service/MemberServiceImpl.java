package beforespring.socialfeed.member.service;

import beforespring.socialfeed.jwt.domain.AuthToken;
import beforespring.socialfeed.jwt.service.JwtIssuer;
import beforespring.socialfeed.member.domain.*;
import beforespring.socialfeed.member.service.dto.PasswordAuth;
import beforespring.socialfeed.member.service.dto.RefreshTokenAuth;
import beforespring.socialfeed.member.service.exception.ConfirmNotFoundException;
import beforespring.socialfeed.member.service.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import static beforespring.socialfeed.member.service.dto.ConfirmTokenDto.ConfirmTokenRequest;
import static beforespring.socialfeed.member.service.dto.CreateMemberDto.CreateMemberRequest;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final JwtIssuer jwtIssuer;

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
        confirm.verifyToken(request.getToken());
        member.joinConfirm();
    }

    @Override
    public AuthToken authenticate(PasswordAuth passwordAuth) {
        Member member = memberRepository.findByUsername(passwordAuth.username())
                            .orElseThrow(MemberNotFoundException::new);
        member.verifyPassword(passwordAuth.password(), passwordHasher);

        return jwtIssuer.issue(member.getId(), member.getUsername());
    }

    @Override
    public AuthToken authenticate(RefreshTokenAuth refreshTokenAuth) {
        return jwtIssuer.renew(refreshTokenAuth.refreshToken(), refreshTokenAuth.username());
    }
}
