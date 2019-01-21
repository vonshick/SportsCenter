package UczestnikTable;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import GUI.AlertBox;
import sportscenter.DBManager;
import sportscenter.ValidateData;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
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
    private ComboBox competition;
    @FXML
    private ChoiceBox status;
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException, SQLException {
        try {
            String[] providedData = {PESEL.getText(), surname.getText(), name.getText(), competition.getSelectionModel().getSelectedItem().toString()};
            if (ValidateData.isAnyEmpty(providedData)) {
                AlertBox.showAlert("None of fields can be empty");
            } else if(ValidateData.isIncorrectPESEL(providedData[0])) {
                AlertBox.showAlert("Niepoprawny PESEL!");
            } else {
                dbManager.getDbManagerUczestnik().editUczestnik(uczestnik.getPESEL(), providedData[0], providedData[1], providedData[2], providedData[3], getStatus());
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        } catch (NullPointerException e) {
            AlertBox.showAlert("Pola Zawody i Status nie mogą być puste!");
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.dbManager = SportsCenter.dBManager;
        competitions = dbManager.getDbManagerUczestnik().generateCompetitionsList();
        ArrayList<String> choices = new ArrayList<>();
        choices.clear();
        choices.add("OPŁACONY");
        choices.add("NIEOPŁACONY");
        status.setItems(FXCollections.observableArrayList(choices));
        competition.getItems().addAll(competitions);
        GUI.AutoCompleteComboBoxListener<String> autoComplete = new GUI.AutoCompleteComboBoxListener<>(competition);
    }    
    
    public void setUczestnik(Uczestnik uczestnik) {
        this.uczestnik = uczestnik;
        PESEL.setText(uczestnik.getPESEL());
        surname.setText(uczestnik.getSurname());
        name.setText(uczestnik.getName());
        name.setText(uczestnik.getName());
        status.getSelectionModel().select(setStatus());
//        competition.getSelectionModel().select(uczestnik.get);
    }
    
    private String setStatus(){
        if(uczestnik.getStatus() == 1){
            return "OPŁACONY";
       } else {
           return "NIEOPŁACONY";
       }
    }

    private char getStatus() throws NullPointerException {
       if(status.getSelectionModel().getSelectedItem().equals("OPŁACONY")){
            return 1;
       } else {
           return 0;
       }
    }
}

