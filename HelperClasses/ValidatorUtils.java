
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by makis on 1/10/2017.
 */

public class ValidatorUtils {

    private static final String TAG = ValidatorUtils.class.getSimpleName();

    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static final String FULL_NAME_PATTERN = "[A-Za-z]+ [A-Za-z]+";

    /**
     * Method checks if given String object match FULL_NAME_PATTERN pattern.
     *
     * @param fullName String to be verified.
     * @return True if given String match FULL_NAME_PATTERN pattern, false otherwise.
     */
    public static boolean isFullNameValid(String fullName) {
        return isTextValid(fullName, FULL_NAME_PATTERN);
    }

    /**
     * Method checks if given String object match EMAIL_PATTERN pattern.
     *
     * @param email String to be verified.
     * @return True if given String match EMAIL_PATTERN pattern, false otherwise.
     */
    public static boolean isEmailValid(String email) {
        return isTextValid(email, EMAIL_PATTERN);
    }

    /**
     * Method checks if given text match provided pattern.
     *
     * @param text String to be verified.
     * @param pattern Pattern to be used.
     * @return True if given text match provided pattern, false otherwise.
     */
    public static boolean isTextValid(String text, String pattern) {
        if(StringUtils.isNullOrEmpty(pattern)){
            return false;
        }
        if(text == null){
            return false;
        }
        Pattern patt = Pattern.compile(pattern);
        Matcher matcher = patt.matcher(text);
        return matcher.matches();
    }

}
