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
public class AddObiektSportowyController implements Initializable {
    
    @FXML
    private TextField name;
    @FXML
    private TextField location;
    @FXML
    private TextField type;
    @FXML
    private Button button;
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException, SQLException {
        if(ifSomeEmpty()){
            showAlert("None of fields can be empty");
        } else {
            insertIntoDB(name.getText(), location.getText(), type.getText());
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    private boolean ifSomeEmpty(){
        return (name.getText().equals("") || location.getText().equals("") || 
                type.getText().equals(""));
    }
    
    private void showAlert(String message){
        Alert alert = new Alert(AlertType.WARNING, message, ButtonType.OK);
        alert.showAndWait();
    }
    
    private void insertIntoDB(String name, String location, String type) throws SQLException{
        try{
            PreparedStatement pstmt = SportsCenter.manager.getConnection().prepareStatement("INSERT INTO obiekt_sportowy VALUES(seq_id_obiektu.nextval, ?, ?, ?)");
            pstmt.setString(1, name);
            pstmt.setString(2, location);
            pstmt.setString(3, type);
            pstmt.executeUpdate();
            System.out.println("Sports facility added!");
        }catch(SQLException e ){
            System.out.println("Sports facility inserting error");
            e.printStackTrace();
        }
    }
    
}

