package sportscenter;

import java.util.List;
import javafx.scene.control.ChoiceBox;

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
    
    public static boolean isNumber(String stringToCheck){
        return (stringToCheck.matches("[0-9]+"));
    }
    
        
    public static boolean ifValueNotSelected(ChoiceBox choiceBox){
        return choiceBox.getSelectionModel().isEmpty();
    }
}
