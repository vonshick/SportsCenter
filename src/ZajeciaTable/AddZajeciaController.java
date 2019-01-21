package ZajeciaTable;

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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import sportscenter.DBManager;
import sportscenter.SportsCenter;
import sportscenter.ValidateData;

public class AddZajeciaController implements Initializable {
    private DBManager dbManager;
    private Map<String, Integer> buildings;
    private Map<String, String> coaches;

    @FXML
    private TextField sport;
    @FXML
    private TextField price;
    @FXML
    private ChoiceBox dayOfWeek;
    @FXML
    private ChoiceBox startHour;
    @FXML
    private ChoiceBox startMinute;
    @FXML
    private ChoiceBox endHour;
    @FXML
    private ChoiceBox endMinute;
    @FXML
    private ComboBox coach;
    @FXML
    private ComboBox building;
    @FXML
    private ComboBox hall;
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException, SQLException {
        try {
            String[] providedData = { sport.getText(), price.getText(), coach.getSelectionModel().getSelectedItem().toString(), building.getSelectionModel().getSelectedItem().toString() };
            String[] providedTimeData = { dayOfWeek.getSelectionModel().getSelectedItem().toString(),
                startHour.getSelectionModel().getSelectedItem().toString(), startMinute.getSelectionModel().getSelectedItem().toString(),
                endHour.getSelectionModel().getSelectedItem().toString(), endMinute.getSelectionModel().getSelectedItem().toString() };
            if (ValidateData.isAnyEmpty(providedData)) {
                AlertBox.showAlert("Pola Dyscyplina i Cena muszą być wypełnione!");
            } else {
                String hallId = "";
                if (!hall.getSelectionModel().isEmpty()) {
                    hallId = hall.getSelectionModel().getSelectedItem().toString();
                }
                try {
                    dbManager.getDbManagerZajecia().insertNewZajecia(
                            providedTimeData[0], providedTimeData[1], providedTimeData[2], providedTimeData[3], providedTimeData[4],
                            providedData[0], Float.parseFloat(providedData[1]), coaches.get(providedData[2]), buildings.get(providedData[3]), hallId );
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                } catch (NumberFormatException e) {
                    AlertBox.showAlert("Cena musi być liczbą!");
                }
            }
        } catch (NullPointerException e) {
            AlertBox.showAlert("Wszystkie pola (oprócz opcjonalnego pola 'Sala') muszą być wypełnione!");
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.dbManager = SportsCenter.dBManager;
        buildings = dbManager.getDbManagerZajecia().generateBuildingsMap();
        coaches = dbManager.getDbManagerZajecia().generateCoachessMap();
        GUI.AutoCompleteComboBoxListener<String> autoCompleteBuilding = new GUI.AutoCompleteComboBoxListener<>(building);
        GUI.AutoCompleteComboBoxListener<String> autoCompleteCoach = new GUI.AutoCompleteComboBoxListener<>(coach);
        GUI.AutoCompleteComboBoxListener<String> autoCompleteHall = new GUI.AutoCompleteComboBoxListener<>(hall);
        fillChoiceBoxes();
        addChangeListener();
    }    
    
    private void fillChoiceBoxes(){
        fillBuildingCB();
        fillCoachCB();
        fillTimeCB();
    }
    
    private void fillBuildingCB(){
        ArrayList<String> choices = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : buildings.entrySet()){   
            choices.add(entry.getKey());
        }
        building.getItems().addAll(choices);
    }
    private void fillCoachCB(){
        ArrayList<String> choices = new ArrayList<>();
        for (Map.Entry<String, String> entry : coaches.entrySet()){   
            choices.add(entry.getKey());
        }
        coach.getItems().addAll(choices);
    }
    
    private void fillTimeCB(){
        String[] daysOfWeek = {"poniedzialek", "wtorek", "sroda", "czwartek", "piatek", "sobota", "niedziela"};
        dayOfWeek.setItems(FXCollections.observableArrayList(daysOfWeek));
        
        ArrayList<String> hours = new ArrayList<>();
        for (int i = 0; i<24; i++){
            hours.add(Integer.toString(i));
        }
        startHour.setItems(FXCollections.observableArrayList(hours));
        endHour.setItems(FXCollections.observableArrayList(hours));
        
        ArrayList<String> minutes = new ArrayList<>();
        for (int i = 0; i<60; i++){
            if(i<10){
                minutes.add("0"+Integer.toString(i));
            }else{
                minutes.add(Integer.toString(i));
            }
        }
        startMinute.setItems(FXCollections.observableArrayList(minutes));
        endMinute.setItems(FXCollections.observableArrayList(hours));   
    }
    
    private void addChangeListener(){
        building.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number indexOld, Number indexNew) {
                String buildingName = building.getItems().get((Integer) indexNew).toString();
                hall.getItems().clear();
                hall.getItems().addAll(dbManager.getDbManagerZajecia().generateHallsList(buildingName));
            }
        }); 
    }
    
}

