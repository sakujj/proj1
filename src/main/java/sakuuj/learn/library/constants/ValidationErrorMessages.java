package sakuuj.learn.library.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationErrorMessages {
    public static final String ON_EMPTY = "Empty value is not allowed";
    public static final String ON_BLANK = "Blank value is not allowed";
    public static final String ON_SMALL_LENGTH = "Entered value's length is too small";
    public static final String ON_LARGE_LENGTH = "Entered value's length is too large";

    public static final String ON_SMALL_VALUE = "Entered value is too small";

    public static final String ON_PERSON_NAME_ALREADY_TAKEN = "Entered name is already taken";

    public static final String ON_NO_SUCH_OWNER_EXIST = "No specified owner exist";
}
