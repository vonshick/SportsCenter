package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import sportscenter.SportsCenter;

/**
 *
 * @author kuba
 */
public class AddTrenerController implements Initializable {
    private String PESEL;
    @FXML
    private TextField sport;
    @FXML
    private Button button;
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException, SQLException {
        if(ifSomeEmpty()){
            showAlert("None of fields can be empty");
        }else if (ifIncorrectPESEL()){
            showAlert("Incorrect PESEL format");
        } else {
            insertIntoDB(PESEL, sport.getText());
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void initData(String PESEL){
        this.PESEL = PESEL;
    }
    
    private boolean ifIncorrectPESEL(){
        return (!(PESEL.matches("[0-9]+") && PESEL.length()==11));
    }
    
    private boolean ifSomeEmpty(){
        return (sport.getText().equals(""));
    }
    
    private void showAlert(String message){
        Alert alert = new Alert(AlertType.WARNING, message, ButtonType.OK);
        alert.showAndWait();
    }
    
    private void insertIntoDB(String PESEL, String sport) throws SQLException{
        try{
            PreparedStatement pstmt = SportsCenter.manager.getConnection().prepareStatement("INSERT INTO trener VALUES(?, ?)");
            pstmt.setString(1, PESEL);
            pstmt.setString(2, sport);
            pstmt.executeUpdate();
            System.out.println("Coach added");
            SportsCenter.manager.getConnection().commit();
            SportsCenter.manager.getConnection().setAutoCommit(true);
        }catch(SQLException e ){
            SportsCenter.manager.getConnection().rollback();
            SportsCenter.manager.getConnection().setAutoCommit(true);
            System.out.println("Coach inserting error");
            e.printStackTrace();
        }
    }
}

