package WyposazenieTable;

import PracownikTable.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import GUI.AlertBox;
import java.util.ArrayList;
import java.util.Map;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
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
    private ChoiceBox building;
    @FXML
    private ChoiceBox hall;
    @FXML
    private Button save;

    @FXML
    private void save(MouseEvent event) throws IOException, SQLException {
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
            System.out.println("clicked save");
            //TO DO 
//            dbManager.getDbManagerWyposazenie().editWydarzenie();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        save.setText("Zapisz");
        this.dbManager = SportsCenter.dBManager;
        buildings = dbManager.getDbManagerWyposazenie().generateBuildingsMap();
        ArrayList<String> choices = new ArrayList<String>();
        String currentBuilding = "";
        for (Map.Entry<String, Integer> entry : buildings.entrySet()){   
            choices.add(entry.getKey());
            if (entry.getValue() == wyposazenie.getBuildingId()){
                currentBuilding = entry.getKey();
            }
        } 
        building.setItems(FXCollections.observableArrayList(choices));
        
        choices = dbManager.getDbManagerWyposazenie().generateHallsList(currentBuilding);
        hall.setItems(FXCollections.observableArrayList(choices));
        
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
    
    public void setWyposazenie(Wyposazenie wyposazenie) {
        this.wyposazenie = wyposazenie;
        name.setText(wyposazenie.getName());
        sport.setText(wyposazenie.getSport());
        count.setText(Integer.toString(wyposazenie.getCount()));
        //TO DO!!!
//        building.setText(Integer.toString(wyposazenie.getBuildingId()));
//        hall.setText(wyposazenie.getSalary().toString());
    }
    
}
