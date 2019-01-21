package KlientTable;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import GUI.AlertBox;
import sportscenter.DBManager;
import sportscenter.SportsCenter;
import sportscenter.ValidateData;

public class AddKlientController implements Initializable {
    
    private DBManager dbManager;
    
    @FXML
    private TextField name;
    @FXML
    private TextField surname;

    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException, SQLException {
        String[] providedData = { name.getText(), surname.getText() };
        if(ValidateData.isAnyEmpty(providedData)){
            AlertBox.showAlert("Wszystkie pola muszą być wypełnione!");
        } else {
            dbManager.getDbManagerKlient().insertNewKlient(providedData[0], providedData[1]);
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.dbManager = SportsCenter.dBManager;
    }    
}

