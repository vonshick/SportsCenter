package SalaTable;

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
import WyposazenieTable.Wyposazenie;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import sportscenter.DBManager;
import sportscenter.SportsCenter;
import sportscenter.ValidateData;

public class EditSalaController implements Initializable {
    private Sala sala;
    private DBManager dbManager;
    private Map<String, Integer> buildings;
    @FXML
    private TextField name;
    @FXML
    private ChoiceBox building;
    @FXML
    private Button button;
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException, SQLException {
       String[] providedData = {name.getText(), (String) building.getSelectionModel().getSelectedItem()};
        if(ValidateData.isAnyEmpty(providedData)){
            AlertBox.showAlert("None of fields can be empty");
        } else if (ValidateData.ifValueNotSelected(building)){
            AlertBox.showAlert("None building was chosen");
        } else{
            dbManager.getDbManagerSala().editSala(sala.getHallId(), sala.getBuildingId(), providedData[0], buildings.get(providedData[1]));
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.dbManager = SportsCenter.dBManager;
        buildings = dbManager.getDbManagerSala().generateBuildingsMap();
    }    
    
    public void initializeEditWindow(Sala sala){
        setSala(sala);
        initializeValues();
    }
        
    private void setSala(Sala sala) {
        this.sala = sala;
    }
    
    private void initializeValues(){
        ArrayList<String> choices = new ArrayList<String>();
        String currentBuilding = "";
        for (Map.Entry<String, Integer> entry : buildings.entrySet()){   
            choices.add(entry.getKey());
            if (entry.getValue() == sala.getBuildingId()){
                currentBuilding = entry.getKey();
            }
        } 
        building.setItems(FXCollections.observableArrayList(choices));
        building.getSelectionModel().select(currentBuilding);
        name.setText(sala.getHallId());
    }

    
}

