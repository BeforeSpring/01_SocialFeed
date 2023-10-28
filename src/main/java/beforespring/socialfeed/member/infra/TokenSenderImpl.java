package beforespring.socialfeed.member.domain;

import java.util.Random;

/**
 * 메서드가 정적으로 선언되어진 이유는 서비스와 독립적으로 기능을 할 수 있도록 구현하였고, 일회성으로 동작하기 때문입니다.
 */
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
