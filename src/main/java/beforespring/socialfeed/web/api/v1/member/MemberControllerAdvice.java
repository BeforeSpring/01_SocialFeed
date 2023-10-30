package beforespring.socialfeed.web.api.v1.member;

import beforespring.socialfeed.member.service.exception.ConfirmNotFoundException;
import beforespring.socialfeed.member.service.exception.MemberNotFoundException;
import beforespring.socialfeed.web.api.v1.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class MemberControllerAdvice {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MemberNotFoundException.class)
    public ErrorResponse<String> handleMemberNotFoundException(MemberNotFoundException e, HttpServletRequest request) {
        return new ErrorResponse<>(HttpStatus.NOT_FOUND, e.getMessage(), request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ConfirmNotFoundException.class)
    public ErrorResponse<String> handleConfirmNotFoundException(ConfirmNotFoundException e, HttpServletRequest request) {
        return new ErrorResponse<>(HttpStatus.NOT_FOUND, e.getMessage(), request.getRequestURI());
    }
}
