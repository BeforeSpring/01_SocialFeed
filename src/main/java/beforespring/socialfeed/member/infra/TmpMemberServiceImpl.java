package beforespring.socialfeed.member.infra;

import beforespring.socialfeed.member.controller.dto.CreateMemberDto;
import beforespring.socialfeed.member.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TmpMemberServiceImpl implements MemberService {

    @Override
    public Long join(CreateMemberDto.Request request) {
        return 1L;
    }

    @Override
    public void joinConfirm(String username, String password, String token) {
    }
}
