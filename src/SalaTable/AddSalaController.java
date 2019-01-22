package SalaTable;

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
import javafx.scene.control.ComboBox;
import sportscenter.DBManager;
import sportscenter.SportsCenter;
import sportscenter.ValidateData;

public class AddSalaController implements Initializable {
    private DBManager dbManager;
    private Map<String, Integer> buildings;
    
    @FXML
    private TextField name;
    @FXML
    private ComboBox building;
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException, SQLException {
        try {
            String[] providedData = {name.getText(), building.getSelectionModel().getSelectedItem().toString()};
            if (ValidateData.isAnyEmpty(providedData)) {
                AlertBox.showAlert("None of fields can be empty");
            } else {
                dbManager.getDbManagerSala().insertNewSala(providedData[0], buildings.get(providedData[1]));
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        } catch (NullPointerException e) {
            AlertBox.showAlert("Pole Budynek musi zawierać poprawną nazwę obiektu!");
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbManager = SportsCenter.dBManager;
        buildings = dbManager.getDbManagerSala().generateBuildingsMap();
        ArrayList<String> choices = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : buildings.entrySet()){   
            choices.add(entry.getKey());
        }
        GUI.AutoCompleteComboBoxListener<String> buildingAutoComplete = new GUI.AutoCompleteComboBoxListener<>(building);
        building.getItems().addAll(choices);
    }    
}

