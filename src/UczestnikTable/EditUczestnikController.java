package UczestnikTable;

import UczestnikTable.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import GUI.AlertBox;
import sportscenter.DBManager;
import sportscenter.ValidateData;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import sportscenter.SportsCenter;

public class EditUczestnikController implements Initializable {
    private Uczestnik uczestnik;
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
        } else if (ValidateData.ifValueNotSelected(competition)){
            AlertBox.showAlert("None competition was chosen");
        } else if (ValidateData.ifValueNotSelected(status)){
            AlertBox.showAlert("None status was chosen");
        } else{
            dbManager.getDbManagerUczestnik().editUczestnik(providedData[0], providedData[1], providedData[2], 
                    (String)competition.getSelectionModel().getSelectedItem(),
                    getStatus());
                    
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.dbManager = SportsCenter.dBManager;
    }    
    
    public void initializeEditWindow(Uczestnik uczestnik){
        setUczestnik(uczestnik);
        initializeValues();
    }
        
    private void setUczestnik(Uczestnik uczestnik) {
        this.uczestnik = uczestnik;
    }
    
    private void initializeValues(){
        dbManager = SportsCenter.dBManager;
        competitions = dbManager.getDbManagerUczestnik().generateCompetitionsList();
        ArrayList<String> choices = new ArrayList<String>();
        competition.setItems(FXCollections.observableArrayList(competitions));
        choices.clear();
        choices.add("OPŁACONY");
        choices.add("NIEOPŁACONY");
        status.setItems(FXCollections.observableArrayList(choices));
        status.getSelectionModel().select(setStatus());
//        competition.getSelectionModel().select(uczestnik.getCompetition());

    }
    
    private String setStatus(){
        if(uczestnik.getStatus() == '1'){
            return "OPŁACONY";
       } else {
           return "NIEOPŁACONY";
       }
    }

    private char getStatus(){
       if(status.getSelectionModel().getSelectedItem().equals("OPŁACONY")){
            return '1';
       } else {
           return '0';
       }
    }

    
}

