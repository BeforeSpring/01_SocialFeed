package beforespring.socialfeed.member.service.dto;

import beforespring.socialfeed.member.service.exception.PasswordPatternException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 문자의 패턴을 검사하는 클래스
 */
public class PasswordPatternChecker {

    /**
     * 연속된 문자 검사. 동일한 문자가 3회 이상 나오면 예외 발생
     *
     * @param toCheck 검증할 문자열
     */
    public void checkConsecutiveChars(String toCheck) {
        String consecutiveCharsPattern = "(.)\\1\\1";
        Matcher matcher = Pattern.compile(consecutiveCharsPattern).matcher(toCheck);
        if (matcher.find())
            throw new PasswordPatternException("동일한 문자를 3회 이상 연속으로 사용할 수 없습니다.");
    }

    /**
     * 문자 조합 검사 로직. 문자, 숫자, 특수 문자 중 2가지 이상을 포함하지 않으면 예외 발생
     *
     * @param toCheck 확인할 문자열
     */
    public void checkCharsCombination(String toCheck) {
        boolean alpha = containsAlpha(toCheck);
        boolean num = containsNum(toCheck);
        boolean specialChar = containsSChar(toCheck);

        if (alpha && !num && !specialChar)
            throw new PasswordPatternException("숫자 혹은 특수문자를 포함해야 합니다.");
        else if (!alpha && num && !specialChar)
            throw new PasswordPatternException("문자 혹은 특수문자를 포함해야 합니다.");
        else if (!alpha && !num && specialChar)
            throw new PasswordPatternException("문자 혹은 숫자를 포함해야 합니다.");
    }

    /**
     * 문자열에 알파벳이 포함되는지 여부
     *
     * @param toCheck 확인할 문자열
     * @return 포함되면 true
     */
    private boolean containsAlpha(String toCheck) {
        String complexCharsPattern = "^(?=.*[A-Za-z]).{10,}$";

        return toCheck.matches(complexCharsPattern);
    }

    /**
     * 문자열에 숫자가 포함되는지 여부
     *
     * @param toCheck 확인할 문자열
     * @return 포함되면 true
     */
    private boolean containsNum(String toCheck) {
        String complexCharsPattern = "^(?=.*\\d).{10,}$";

        return toCheck.matches(complexCharsPattern);
    }

    /**
     * 문자열에 특수문자가 포함되는지 여부
     *
     * @param toCheck 확인할 문자열
     * @return 포함되면 true
     */
    private boolean containsSChar(String toCheck) {
        String complexCharsPattern = "^(?=.*[^A-Za-z\\d]).{10,}$";

        return toCheck.matches(complexCharsPattern);
    }
}
