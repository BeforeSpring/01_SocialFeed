package beforespring.socialfeed.member.domain;

import beforespring.socialfeed.member.exception.InvalidPasswordException;

public interface PasswordValidator {

    void validate(Member member, String rawPassword, PasswordHasher passwordHasher) throws InvalidPasswordException;
}
