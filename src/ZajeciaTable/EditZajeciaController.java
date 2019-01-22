package ZajeciaTable;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import GUI.AlertBox;
import java.util.ArrayList;
import java.util.Map;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import sportscenter.DBManager;
import sportscenter.SportsCenter;
import sportscenter.ValidateData;

public class EditZajeciaController implements Initializable {
    private Zajecia zajecia;
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
            String[] providedData = {sport.getText(), price.getText(), coach.getSelectionModel().getSelectedItem().toString(), building.getSelectionModel().getSelectedItem().toString()};
            String[] providedTimeData = {dayOfWeek.getSelectionModel().getSelectedItem().toString(),
                startHour.getSelectionModel().getSelectedItem().toString(), startMinute.getSelectionModel().getSelectedItem().toString(),
                endHour.getSelectionModel().getSelectedItem().toString(), endMinute.getSelectionModel().getSelectedItem().toString()};
            String coachLocal = coaches.get(providedData[2]);
            if (coachLocal == null) {
                AlertBox.showAlert("Podaj istniejącego trenera!");
                return;
            }
            try {
                int buildingLocal = buildings.get(providedData[3]);
                if (ValidateData.isAnyEmpty(providedData) && coachLocal != null) {
                    AlertBox.showAlert("Pola Dyscyplina i Cena muszą być wypełnione!");
                } else {
                    String hallId = "";
                    if (!hall.getSelectionModel().isEmpty()) {
                        hallId = hall.getSelectionModel().getSelectedItem().toString();
                    }
                    try {
                        dbManager.getDbManagerZajecia().editZajecia(zajecia.getId(),
                                providedTimeData[0], providedTimeData[1], providedTimeData[2], providedTimeData[3], providedTimeData[4],
                                providedData[0], Float.parseFloat(providedData[1]), coachLocal, buildingLocal, hallId);
                        ((Node) (event.getSource())).getScene().getWindow().hide();
                    } catch (Exception e) {
                        AlertBox.showAlert("Incorrect price value");
                    }
                }
            } catch (Exception e) {
                AlertBox.showAlert("Podaj istniejacy budynek!");
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
    }
        
    public void setZajecia(Zajecia zajecia) {
        this.zajecia = zajecia;
        initializeChoiceBoxes();
        setFieldsValues();
        addChangeListener();
    }
    
    private void initializeChoiceBoxes(){
        fillBuildingCB();
        fillCoachCB();
        fillTimeCB();
        addChangeListener();
    }
    
    private void setFieldsValues(){
        sport.setText(zajecia.getSport());
        price.setText(Float.toString(zajecia.getPrice()));
        dayOfWeek.getSelectionModel().select(zajecia.getDayOfWeek());
        startHour.getSelectionModel().select(extractHours(zajecia.getStartHour()));
        startMinute.getSelectionModel().select(extractMinutes(zajecia.getStartHour()));
        endHour.getSelectionModel().select(extractHours(zajecia.getEndHour()));
        endMinute.getSelectionModel().select(extractMinutes(zajecia.getEndHour()));
        coach.getSelectionModel().select(zajecia.getCoachName());
        hall.getSelectionModel().select(zajecia.getHallId());
        String buildingName = "";
        for (Map.Entry<String, Integer> entry : buildings.entrySet()){  
            if (entry.getValue() == zajecia.getBuildingId()){
                buildingName = entry.getKey();
            }
        } 
        building.getSelectionModel().select(buildingName);
    }
    
    private String extractMinutes(String fullHour){
        return fullHour.substring(fullHour.lastIndexOf(":") + 1);
    }
    private String extractHours(String fullHour){
        return fullHour.substring(0, fullHour.lastIndexOf(":"));
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
            if(i<10){
                hours.add("0"+Integer.toString(i));
            }else{
                hours.add(Integer.toString(i));
            }
        }
        startHour.getItems().addAll(hours);
        endHour.getItems().addAll(hours);
        
        ArrayList<String> minutes = new ArrayList<>();
        for (int i = 0; i<60; i++){
            if(i<10){
                minutes.add("0"+Integer.toString(i));
            }else{
                minutes.add(Integer.toString(i));
            }
        }
        startMinute.getItems().addAll(minutes);
        endMinute.getItems().addAll(minutes);   
    }
    
    private void addChangeListener(){
        building.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldIndex, Number newIndex) {
                try {
                    String buildingName = building.getItems().get((Integer) newIndex).toString();
                    hall.getItems().clear();
                    hall.getItems().addAll(dbManager.getDbManagerZajecia().generateHallsList(buildingName));
                } catch (Exception e) {
                    building.getSelectionModel().clearSelection();
                }
            }
        }); 
    }
    
}
