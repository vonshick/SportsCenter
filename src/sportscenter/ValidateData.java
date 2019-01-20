package sportscenter;

import GUI.AlertBox;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    
    public static java.sql.Date stringToDate(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date javaDate = format.parse(date);
        java.sql.Date sqlDate = new java.sql.Date(javaDate.getTime());
        return  sqlDate;
    }
    
    public static String dateToString(java.sql.Date date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return (new java.sql.Date(date.getTime())).toString();
    }
      
    public static boolean isDate(String stringToCheck) {
        //        boolean isNumber = true;
//        if(!stringToCheck.matches("20[0-9][0-9]-[0-1][0-9]-[0-9][0-9]")) {
//            return false;
//        }
//        String[] parts = stringToCheck.split("-");
//        int year = Integer.parseInt(parts[0]);
//        int month = Integer.parseInt(parts[1]);
//        int day = Integer.parseInt(parts[2]);
//        DateFormat.parse(stringToCheck);
//        if( year < 0 || month < 1 || month > 12 || day < 1 || day > 31) {
//            
//        }
        return stringToCheck.matches("^((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$");
    }
    public static boolean ifValueNotSelected(ChoiceBox choiceBox){
        return choiceBox.getSelectionModel().isEmpty();
    }
    
    public static void printSQLException(SQLException ex, String message) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {   
                if(((SQLException)e).getErrorCode() == 1){
                    AlertBox.showAlert("Error while inserting/updating data\nValue of '"+message+"' field must be unique");
                }else{
                    AlertBox.showAlert("Error: "+e.getMessage());
                }
            }
        }
    }
}
