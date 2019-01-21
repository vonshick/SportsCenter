package WyposazenieTable;

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
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import sportscenter.DBManager;
import sportscenter.SportsCenter;
import sportscenter.ValidateData;

public class EditWyposazenieController implements Initializable {

    private Wyposazenie wyposazenie;
    private DBManager dbManager;
    private Map<String, Integer> buildings;

    @FXML
    private TextField name;
    @FXML
    private TextField sport;
    @FXML
    private TextField count;
    @FXML
    private ComboBox building;
    @FXML
    private ComboBox hall;

    @FXML
    private void editWyposazenieTable(ActionEvent event) throws IOException, SQLException {
        try {
            String[] providedData = {name.getText(), count.getText(), building.getSelectionModel().getSelectedItem().toString()};
            if (ValidateData.isAnyEmpty(providedData)) {
                AlertBox.showAlert("Pole Nazwa, Ilość i Budynek nie mogą być puste!");
            } else if (!ValidateData.isNumber(count.getText())) {
                AlertBox.showAlert("W pole 'Ilość' należy wpisać liczbę całkowitą!");
            } else {
                String hallId = "";
                if (!hall.getSelectionModel().isEmpty()) {
                    hallId = hall.getSelectionModel().getSelectedItem().toString();
                }
                dbManager.getDbManagerWyposazenie().editWyposazenie(wyposazenie.getId(), providedData[0], sport.getText(), providedData[1], buildings.get(providedData[2]), hallId);
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        } catch (NullPointerException e) {
            AlertBox.showAlert("Pole Budynek nie może być puste!");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.dbManager = SportsCenter.dBManager;
        buildings = dbManager.getDbManagerWyposazenie().generateBuildingsMap();
        GUI.AutoCompleteComboBoxListener<String> autoCompleteBuilding = new GUI.AutoCompleteComboBoxListener<>(building);
        GUI.AutoCompleteComboBoxListener<String> autoCompleteHall = new GUI.AutoCompleteComboBoxListener<>(hall);
    }
    
    public void setWyposazenie(Wyposazenie wyposazenie) {
        this.wyposazenie = wyposazenie;
        initializeChoiceBoxes();
        setFieldsValues();
        addChangeListener();
    }
    
    private void initializeChoiceBoxes(){
        ArrayList<String> choices = new ArrayList<>();
        String currentBuilding = "";
        for (Map.Entry<String, Integer> entry : buildings.entrySet()){   
            choices.add(entry.getKey());
            if (entry.getValue() == wyposazenie.getBuildingId()){
                currentBuilding = entry.getKey();
            }
        } 
        building.getItems().addAll(choices);
        hall.getItems().addAll(dbManager.getDbManagerWyposazenie().generateHallsList(currentBuilding));
    }
    
    private void setFieldsValues(){
        name.setText(wyposazenie.getName());
        sport.setText(wyposazenie.getSport());
        count.setText(Integer.toString(wyposazenie.getCount()));
        hall.getSelectionModel().select(wyposazenie.getHallId());
        String buildingName = "";
        for (Map.Entry<String, Integer> entry : buildings.entrySet()){  
            if (entry.getValue() == wyposazenie.getBuildingId()){
                buildingName = entry.getKey();
            }
        } 
        building.getSelectionModel().select(buildingName);
    }
    
    private void addChangeListener(){
        building.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number indexOld, Number indexNew) {
                String buildingName = building.getItems().get((Integer) indexNew).toString();
                hall.getItems().clear();
                hall.getItems().addAll(dbManager.getDbManagerWyposazenie().generateHallsList(buildingName));
            }
        }); 
    }

}
