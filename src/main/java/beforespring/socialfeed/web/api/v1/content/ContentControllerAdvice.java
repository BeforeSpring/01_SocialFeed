package beforespring.socialfeed.web.api.v1.content;

import beforespring.socialfeed.web.api.v1.response.ErrorResponse;
import beforespring.socialfeed.content.service.exception.ContentNotFoundException;
import beforespring.socialfeed.web.memberinforesolver.MemberInfoResolverException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ContentControllerAdvice {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ContentNotFoundException.class)
    public ErrorResponse<String> handleContentNotFoundException(ContentNotFoundException e, HttpServletRequest request) {
        return new ErrorResponse<>(HttpStatus.NOT_FOUND, e.getMessage(), request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(MemberInfoResolverException.class)
    public ErrorResponse<String> memberInfoResolverException(MemberInfoResolverException e, HttpServletRequest request) {
        return new ErrorResponse<>(HttpStatus.UNAUTHORIZED, "unauthorized", request.getRequestURI());
    }
}

