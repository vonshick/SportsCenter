package TrenerTable;

import GUI.AlertBox;
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
import sportscenter.*;


public class AddTrenerController implements Initializable {
    
    private DBManager dBManager;
    
    private String PESEL;
    @FXML
    private TextField sport;
    @FXML
    private Button button;
    @FXML
    
    private void handleButtonAction(ActionEvent event) throws IOException, SQLException {
        if(sport.getText().equals("")){
            AlertBox.showAlert("None of fields can be empty");
        }else if (ValidateData.isIncorrectPESEL(PESEL)){
            AlertBox.showAlert("Incorrect PESEL format");
        } else {
            dBManager.getdBManagerTrener().insertNewTrener(PESEL, sport.getText());
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.dBManager = SportsCenter.dBManager;
    }    
    
    public void initData(String PESEL){
        this.PESEL = PESEL;
    }

}

