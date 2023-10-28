package beforespring.socialfeed.member.domain;

public interface TokenSender {
    void sendEmail(String email, String token);

    String generateToken();
}
