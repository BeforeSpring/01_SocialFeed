package beforespring.socialfeed.member.domain;

public interface PasswordValidator {

    void validate(Member member, String rawPassword, PasswordHasher passwordHasher) throws Exception;
}
