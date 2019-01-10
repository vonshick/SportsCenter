package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sportscenter.SportsCenter;

public class AddPracownikController implements Initializable {
    
    @FXML
    private TextField name;
    @FXML
    private TextField surname;
    @FXML
    private TextField PESEL;
    @FXML
    private TextField profession;
    @FXML
    private TextField salary;
    @FXML
    private Button button;
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException, SQLException {
        if(ifSomeEmpty()){
            showAlert("None of fields can be empty");
        }else if (ifIncorrectPESEL()){
            showAlert("Incorrect PESEL format");
        } else {
            insertIntoDB(name.getText(), surname.getText(), PESEL.getText(), profession.getText(), salary.getText(), event);
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    private boolean ifIncorrectPESEL(){
        return (!(PESEL.getText().matches("[0-9]+") && PESEL.getText().length()==11));
    }
    
    private boolean ifSomeEmpty(){
        return (name.getText().equals("") || surname.getText()==null || 
                PESEL.getText()==null || profession.getText()==null || 
                salary.getText()==null);
    }
    
    private void showAlert(String message){
        Alert alert = new Alert(AlertType.WARNING, message, ButtonType.OK);
        alert.showAndWait();
    }
    
    private void insertIntoDB(String name, String surname, String PESEL, String profession, String salary, ActionEvent event) throws IOException {
        try{
            PreparedStatement pstmt = SportsCenter.manager.getConnection().prepareStatement("INSERT INTO pracownik VALUES(?, ?, ?, ?, ?)");
            SportsCenter.manager.getConnection().setAutoCommit(false); // if coach adding fails we have to rollback
            pstmt.setString(1, PESEL);
            pstmt.setString(2, surname);
            pstmt.setString(3, name);
            pstmt.setString(4, profession);
            pstmt.setFloat(5, Float.parseFloat(salary));
            pstmt.executeUpdate();
            System.out.println("Employee added!");
            if(profession.toLowerCase().equals("trener") || profession.toLowerCase().equals("trenerka") ){
                openAddTrenerWindow(event, PESEL);
            }
        }catch(SQLException e ){
            System.out.println("Employee inserting error");
        }
    }
    
       private void openAddTrenerWindow(ActionEvent event, String PESEL) throws IOException{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddTrener.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene((Pane) loader.load()));
            stage.setTitle("Log in");
            stage.setOnCloseRequest((WindowEvent event1) -> {
                showAlert("Employee not added!");
                try {
                    SportsCenter.manager.getConnection().rollback(); // if coach adding fails we have to rollback
                    SportsCenter.manager.getConnection().setAutoCommit(true);
                } catch (SQLException ex) {
                    Logger.getLogger(AddPracownikController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            AddTrenerController controller = loader.<AddTrenerController>getController();
            controller.initData(PESEL);
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
       }
       
}

