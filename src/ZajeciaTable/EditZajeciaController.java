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
    private void editZajeciaTable(ActionEvent event) throws IOException, SQLException {
        String[] providedData = {name.getText()};
        if(ValidateData.isAnyEmpty(providedData)){
            AlertBox.showAlert("Name field can't be empty");
        } else if (!ValidateData.isNumber(count.getText())){
            AlertBox.showAlert("'Count' value should be an integer");
        } else if (ValidateData.ifValueNotSelected(building)){
            AlertBox.showAlert("None building was chosen");
        }  else {
            String hallId = "";
            if (!ValidateData.ifValueNotSelected(hall)){
                hallId = (String) hall.getSelectionModel().getSelectedItem();
            }
            System.out.println("clicked save");
//            dbManager.getDbManagerZajecia().editZajecia(zajecia.getId(), providedData[0], sport.getText(), count.getText(), getBuildingId(), hallId);
            ((Node) (event.getSource())).getScene().getWindow().hide();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.dbManager = SportsCenter.dBManager;
//        buildings = dbManager.getDbManagerZajecia().generateBuildingsMap();
    }
    
    private int getBuildingId(){
        return buildings.get((String) building.getSelectionModel().getSelectedItem());
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
        ArrayList<String> choices = new ArrayList<String>();
        String currentBuilding = "";
        for (Map.Entry<String, Integer> entry : buildings.entrySet()){   
            choices.add(entry.getKey());
            if (entry.getValue() == zajecia.getBuildingId()){
                currentBuilding = entry.getKey();
            }
        } 
        building.setItems(FXCollections.observableArrayList(choices));
        
//        choices = dbManager.getDbManagerZajecia().generateHallsList(currentBuilding);
        hall.setItems(FXCollections.observableArrayList(choices));
    }
    
    private void setFieldsValues(){
//        name.setText(zajecia.getName());
        sport.setText(zajecia.getSport());
//        count.setText(Integer.toString(zajecia.getCount()));
        hall.getSelectionModel().select(zajecia.getHallId());
        String buildingName = "";
        for (Map.Entry<String, Integer> entry : buildings.entrySet()){  
            if (entry.getValue() == zajecia.getBuildingId()){
                buildingName = entry.getKey();
            }
        } 
        building.getSelectionModel().select(buildingName);
    }
    
    private void addChangeListener(){
        building.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number index1, Number index2) {
                String buildingName = (String) building.getItems().get((Integer) index2);
//                ArrayList<String> choices = dbManager.getDbManagerZajecia().generateHallsList(buildingName);
//                hall.setItems(FXCollections.observableArrayList(choices));
            }
        }); 
    }

    
}
