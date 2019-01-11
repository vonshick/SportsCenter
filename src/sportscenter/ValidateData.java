package sportscenter;

import java.util.List;

public class ValidateData {
    public static boolean isIncorrectPESEL(String PESEL) {
        return (!(PESEL.matches("[0-9]+") && PESEL.length() == 11));
    }

    public static boolean isAnyEmpty(String[] stringsToCheck) {
        for (String string : stringsToCheck) {
            if(string.equals("")) {
                return true;
            }
        }
        return false;
    }
}
