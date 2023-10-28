package beforespring.socialfeed.member.infra;

import beforespring.socialfeed.member.domain.TokenSender;

import java.util.Random;

public class TokenSenderImpl implements TokenSender {

    @Override
    public void sendEmail(String email, String token) {
        System.out.println("회원 가입을 완료하려면 인증 코드를 입력해주세요.\n인증코드: " + token);
    }

    /**
     * 6자리 난수 생성
     *
     * @return 6자리 랜덤 숫자 문자열
     */
    @Override
    public String generateToken() {
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++)
            code.append(random.nextInt(10));
        return code.toString();
    }
}
