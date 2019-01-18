package UczestnikTable;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import GUI.AlertBox;
import sportscenter.DBManager;
import sportscenter.SportsCenter;
import sportscenter.ValidateData;



    /*
        pesel      VARCHAR2(11) NOT NULL,
    nazwisko   VARCHAR2(50) NOT NULL,
    imie       VARCHAR2(20) NOT NULL,
    oplacony   CHAR(1) NOT NULL
*/
public class AddUczestnikController implements Initializable {
    private DBManager dbManager;
    private ArrayList<String> competitions;
    
    @FXML
    private TextField PESEL;
    @FXML
    private TextField surname;
    @FXML
    private TextField name;
    @FXML
    private ChoiceBox competition;
    @FXML
    private ChoiceBox status;
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException, SQLException {
       String[] providedData = {PESEL.getText(), surname.getText(), name.getText()};
        if(ValidateData.isAnyEmpty(providedData)){
            AlertBox.showAlert("None of fields can be empty");
//        } else if (ValidateData.ifValueNotSelected(competition)){
//            AlertBox.showAlert("None competition was chosen");
        } else if (ValidateData.ifValueNotSelected(status)){
            AlertBox.showAlert("None status was chosen");
        } else{
            dbManager.getDbManagerUczestnik().insertNewUczestnik(providedData[0], providedData[1], providedData[2], 
                    (String)competition.getSelectionModel().getSelectedItem(),
                    getStatus());
                    
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbManager = SportsCenter.dBManager;
        competitions = dbManager.getDbManagerUczestnik().generateCompetitionsList();
        ArrayList<String> choices = new ArrayList<String>();
        competition.setItems(FXCollections.observableArrayList(competitions));
        
        //set status values
        choices.clear();
        choices.add("OPŁACONY");
        choices.add("NIEOPŁACONY");
        status.setItems(FXCollections.observableArrayList(choices));
    }    
    
    private int getStatus(){
       if(status.getSelectionModel().getSelectedItem().equals("OPŁACONY")){
            return 1;
       } else {
           return 0;
       }
    }
}

