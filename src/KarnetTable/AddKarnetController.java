package KarnetTable;

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

public class AddKarnetController implements Initializable {
    
    private DBManager dbManager;
    
    @FXML
    private TextField idClient;
    @FXML
    private TextField idActivity;
    @FXML
    private TextField price;
    @FXML
    private TextField dateStart;
    @FXML
    private TextField dateEnd;

    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException, SQLException {
        String[] providedData = {idClient.getText(), idActivity.getText(), price.getText(), dateStart.getText(), dateEnd.getText()};
        if(ValidateData.isAnyEmpty(providedData)){
            AlertBox.showAlert("Wszystkie pola muszą być wypełnione!");
        } else {
            dbManager.getdBManagerKarnet().insertNewKarnet(providedData[0], providedData[1], providedData[2], providedData[3], providedData[4]);
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.dbManager = SportsCenter.dBManager;
    }    
}

