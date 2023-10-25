package beforespring.socialfeed.member.service;

public interface MemberService {

    /**
     * 가입 요청. 가입 요청시 6자리의 랜덤 코드를 이메일로 발송. (이메일 발송 생략에 대해서 논의 필요)
     *
     * @param username
     * @param password
     * @param email
     */
    void join(String username, String password, String email);

    /**
     * 가입 승인
     *
     * @param username 가입한 사용자
     * @param password 가입한 사용자의 비밀번호
     * @param token    이메일으로 전송된 토큰
     */
    void joinConfirm(String username, String password, String token);
}
