package PracownikTable;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import GUI.AlertBox;
import sportscenter.DBManager;
import sportscenter.SportsCenter;

public class AddPracownikController implements Initializable {
    
    private DBManager dbManager;
    
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
            AlertBox.showAlert("None of fields can be empty");
        }else if (ifIncorrectPESEL()){
            AlertBox.showAlert("Incorrect PESEL format");
        } else {
            dbManager.getdBManagerPracownik().insertNewPracownik(name.getText(), surname.getText(), PESEL.getText(), profession.getText(), salary.getText(), event);
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.dbManager = SportsCenter.dBManager;
    }    
    
    private boolean ifIncorrectPESEL(){
        return (!(PESEL.getText().matches("[0-9]+") && PESEL.getText().length()==11));
    }
    
    private boolean ifSomeEmpty(){
        return (name.getText().equals("") || surname.getText()==null || 
                PESEL.getText()==null || profession.getText()==null || 
                salary.getText()==null);
    }

}

