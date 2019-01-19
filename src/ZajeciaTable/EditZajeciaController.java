package ZajeciaTable;

import ZajeciaTable.*;
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
    private ChoiceBox coach;
    @FXML
    private ChoiceBox building;
    @FXML
    private ChoiceBox hall;
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException, SQLException {
        String[] providedData = {sport.getText(), price.getText()};
        if(ValidateData.isAnyEmpty(providedData)){
            AlertBox.showAlert("Name field can not be empty");
        } else if (ValidateData.ifValueNotSelected(building)){
            AlertBox.showAlert("None building was chosen");
        } else if (ValidateData.ifValueNotSelected(dayOfWeek)){
            AlertBox.showAlert("None day of week was chosen");
        } else if (ValidateData.ifValueNotSelected(startHour)){
            AlertBox.showAlert("Start hour was not chosen");
        } else if (ValidateData.ifValueNotSelected(startMinute)){
            AlertBox.showAlert("Start hour was not chosen");
        } else if (ValidateData.ifValueNotSelected(endHour)){
            AlertBox.showAlert("End hour was not chosen");
        } else if (ValidateData.ifValueNotSelected(endMinute)){
            AlertBox.showAlert("End hour was not chosen");
        }  else {
            String hallId = "";
            if (!ValidateData.ifValueNotSelected(hall)){
                hallId = (String) hall.getSelectionModel().getSelectedItem();
            }
            String coachPESEL = "";
            if (!ValidateData.ifValueNotSelected(coach)){
               coachPESEL = getCoachPESEL();
            }
            try{
                Float priceValue = Float.parseFloat(providedData[1]);
                dbManager.getDbManagerZajecia().editZajecia(
                        zajecia.getId(),
                        (String) dayOfWeek.getSelectionModel().getSelectedItem(),
                        (String) startHour.getSelectionModel().getSelectedItem(),
                        (String) startMinute.getSelectionModel().getSelectedItem(),
                        (String) endHour.getSelectionModel().getSelectedItem(),
                        (String) endMinute.getSelectionModel().getSelectedItem(),
                        providedData[0], priceValue,
                        getCoachPESEL(), getBuildingId(), hallId);
                ((Node)(event.getSource())).getScene().getWindow().hide();
            } catch(Exception e){
                AlertBox.showAlert("Incorrect price value");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.dbManager = SportsCenter.dBManager;
        buildings = dbManager.getDbManagerZajecia().generateBuildingsMap();
        coaches = dbManager.getDbManagerZajecia().generateCoachessMap();
    }

    public void initializeEditWindow(Zajecia zajecia){
        setZajecia(zajecia);
        initializeChoiceBoxes();
        setFieldsValues();
        addChangeListener();
    }
    
        
    private void setZajecia(Zajecia zajecia) {
        this.zajecia = zajecia;
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
        ArrayList<String> choices = new ArrayList<String>();
        for (Map.Entry<String, Integer> entry : buildings.entrySet()){   
            choices.add(entry.getKey());
        }
        building.setItems(FXCollections.observableArrayList(choices));
    }
    private void fillCoachCB(){
        ArrayList<String> choices = new ArrayList<String>();
        for (Map.Entry<String, String> entry : coaches.entrySet()){   
            choices.add(entry.getKey());
        }
        coach.setItems(FXCollections.observableArrayList(choices));
    }
    
    private void fillTimeCB(){
        String[] daysOfWeek = {"poniedzialek", "wtorek", "sroda", "czwartek", "piatek", "sobota", "niedziela"};
        dayOfWeek.setItems(FXCollections.observableArrayList(daysOfWeek));
        
        ArrayList<String> hours = new ArrayList<String>();
        for (int i = 0; i<24; i++){
            if(i<10){
                hours.add("0"+Integer.toString(i));
            }else{
                hours.add(Integer.toString(i));
            }
        }
        startHour.setItems(FXCollections.observableArrayList(hours));
        endHour.setItems(FXCollections.observableArrayList(hours));
        
        ArrayList<String> minutes = new ArrayList<String>();
        for (int i = 0; i<60; i++){
            if(i<10){
                minutes.add("0"+Integer.toString(i));
            }else{
                minutes.add(Integer.toString(i));
            }
        }
        startMinute.setItems(FXCollections.observableArrayList(minutes));
        endMinute.setItems(FXCollections.observableArrayList(minutes));   
    }
    
    private void addChangeListener(){
        building.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number index1, Number index2) {
                String buildingName = (String) building.getItems().get((Integer) index2);
                ArrayList<String> choices = dbManager.getDbManagerZajecia().generateHallsList(buildingName);
                hall.setItems(FXCollections.observableArrayList(choices));
            }
        }); 
    }
    
    private int getBuildingId(){
        return buildings.get((String) building.getSelectionModel().getSelectedItem());
    }
    
    private String getCoachPESEL(){
        return coaches.get((String) coach.getSelectionModel().getSelectedItem());
    }

    
}
