package WyposazenieTable;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import GUI.AlertBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import sportscenter.DBManager;
import sportscenter.SportsCenter;
import sportscenter.ValidateData;

public class AddWyposazenieController implements Initializable {
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
    private void handleButtonAction(ActionEvent event) throws IOException, SQLException {
        try {
            String[] providedData = {name.getText(), count.getText(), building.getSelectionModel().getSelectedItem().toString()};
            if (ValidateData.isAnyEmpty(providedData)) {
                AlertBox.showAlert("Pole Nazwa, Ilość i Budynek nie mogą być puste!");
            } else if (!ValidateData.isNumber(providedData[1])) {
                AlertBox.showAlert("W pole 'Ilość' należy wpisać liczbę całkowitą!");
            } else {
                String hallId = "";
                if (!hall.getSelectionModel().isEmpty()) {
                    hallId = hall.getSelectionModel().getSelectedItem().toString();
                }
                dbManager.getDbManagerWyposazenie().insertNewWyposazenie(providedData[0], sport.getText(), providedData[1], buildings.get(providedData[2]), hallId);
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        } catch (NullPointerException e) {
            AlertBox.showAlert("Pole Budynek zawierać poprawną nazwę istniejacego budynku!");
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.dbManager = SportsCenter.dBManager;
        buildings = dbManager.getDbManagerWyposazenie().generateBuildingsMap();
        ArrayList<String> choices = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : buildings.entrySet()){   
            choices.add(entry.getKey());
        }
        building.getItems().addAll(choices);
        GUI.AutoCompleteComboBoxListener<String> autoCompleteBuilding = new GUI.AutoCompleteComboBoxListener<>(building);
        GUI.AutoCompleteComboBoxListener<String> autoCompleteHall = new GUI.AutoCompleteComboBoxListener<>(hall);
        addChangeListener();
    }    
    
    private void addChangeListener(){
        building.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number indexOld, Number indexNew) {
                try {
                    String buildingName = building.getItems().get((Integer) indexNew).toString();
                    hall.getItems().clear();
                    hall.getItems().addAll(dbManager.getDbManagerWyposazenie().generateHallsList(buildingName));
                } catch (Exception e) {
                    building.getSelectionModel().clearSelection();
                }
            }
        }); 
    }

}

