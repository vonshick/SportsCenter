package ZawodyTable;

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
import java.text.ParseException;
import sportscenter.DBManager;
import sportscenter.SportsCenter;
import sportscenter.ValidateData;

public class AddZawodyController implements Initializable {
    
    private DBManager dbManager;
    
    @FXML
    private TextField name;
    @FXML
    private TextField date;
    @FXML
    private TextField disciplin;
    @FXML
    private TextField price;
    @FXML
    private TextField IDSportObject;
    
    @FXML
    private void save(ActionEvent event) throws IOException, SQLException {
        String[] providedData = {name.getText(), date.getText(), disciplin.getText(), price.getText(), IDSportObject.getText()};
        if(ValidateData.isAnyEmpty(providedData)){
            AlertBox.showAlert("None of fields can be empty");
        } else if (!ValidateData.isDate(providedData[1])) {
            AlertBox.showAlert("Incorrect date format");
        } else {
            try {
                dbManager.getdBManagerZawody().insertNewZawody(providedData[0], providedData[1], providedData[2], providedData[3], providedData[4]);
            } catch (ParseException ex) {
                System.out.println("Parsing date failed, wrong format");
            }
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.dbManager = SportsCenter.dBManager;
    }    
}

