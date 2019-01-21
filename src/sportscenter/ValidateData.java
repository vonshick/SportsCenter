package sportscenter;

import GUI.AlertBox;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

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
    
//    java.sql.SQLException: The Network Adapter could not establish the connection
//    java.sql.SQLException: ORA-01017: invalid username/password; logon denied
    public static void printSQLException(SQLException ex, String message) {
            for (Throwable e : ex) {
                if (e instanceof SQLException) {
                    System.out.println(((SQLException)e).getErrorCode());
                    if(((SQLException)e).getErrorCode() == 1){
                        showErrorPane("Error while inserting/updating data\nValue of '"+message+"' field must be unique");
                    } else if (((SQLException)e).getErrorCode() == 1017){
                        AlertBox.showAlert("Invalid username or password");
                    } else if (((SQLException)e).getErrorCode() == 12505){
                        Alert alert = new Alert(AlertType.ERROR, "The Network Adapter could not establish the connection");
                        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));
                        alert.showAndWait();
                    }
                    else{
                        showErrorPane(e.getMessage());
                    }
                }
            }
        }
    
    private static void showErrorPane(String message){
        Alert alert = new Alert(AlertType.ERROR, message);
        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));
        alert.showAndWait();
    }
}

