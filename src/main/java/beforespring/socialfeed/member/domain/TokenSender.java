package beforespring.socialfeed.member.domain;

import java.util.Random;

public class TokenSender {
    public static void sendEmail(String email, String token) {
        System.out.println("회원 가입을 완료하려면 인증 코드를 입력해주세요.\n인증코드: " + token);
    }

    /**
     * 6자리 난수 생성
     *
     * @return 6자리 랜덤 숫자 문자열
     */
    public static String generateToken() {
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++)
            code.append(random.nextInt(10));
        return code.toString();
    }
}
