package WyposazenieTable;

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
    private ChoiceBox building;
    @FXML
    private ChoiceBox hall;
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException, SQLException {
       String[] providedData = {name.getText(), sport.getText(), count.getText()};
        if(ValidateData.isAnyEmpty(providedData)){
            AlertBox.showAlert("None of fields can be empty");
        } else if (!ValidateData.isNumber(providedData[2])){
            AlertBox.showAlert("'Count' value should be an integer");
        } else if (ValidateData.ifValueNotSelected(building)){
            AlertBox.showAlert("None building was chosen");
        }  else {
            String hallId = "";
            if (!ValidateData.ifValueNotSelected(hall)){
                hallId = (String) hall.getSelectionModel().getSelectedItem();
            }
            dbManager.getDbManagerWyposazenie().insertNewWyposazenie(providedData[0], providedData[1], providedData[2], 
                    getBuildingId(), hallId);
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.dbManager = SportsCenter.dBManager;
        buildings = dbManager.getDbManagerWyposazenie().generateBuildingsList();
        ArrayList<String> choices = new ArrayList<String>();
        for (Map.Entry<String, Integer> entry : buildings.entrySet()){   
            choices.add(entry.getKey());
        }
        building.setItems(FXCollections.observableArrayList(choices));
        addChangeListener();
    }    
    
    private void addChangeListener(){
        building.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number index1, Number index2) {
                String buildingName = (String) building.getItems().get((Integer) index2);
                ArrayList<String> choices = dbManager.getDbManagerWyposazenie().generateHallsList(buildingName);
                hall.setItems(FXCollections.observableArrayList(choices));
            }
        }); 
    }
    
    private int getBuildingId(){
        return buildings.get((String) building.getSelectionModel().getSelectedItem());
    }
}

