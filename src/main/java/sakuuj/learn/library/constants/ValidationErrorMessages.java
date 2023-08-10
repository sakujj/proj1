package sakuuj.learn.library.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationErrorMessages {
    public static final String ON_EMPTY = "The empty value is not allowed";
    public static final String ON_BLANK = "A blank value is not allowed";
    public static final String ON_SMALL_LENGTH = "The length is too small";
    public static final String ON_LARGE_LENGTH = "The length is too large";

    public static final String ON_SMALL_VALUE = "The value is too small";

    public static final String ON_PERSON_NAME_ALREADY_TAKEN = "The name is already taken";

    public static final String ON_NO_SUCH_OWNER_EXISTS = "No specified owner exists";
}
