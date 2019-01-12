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
import javafx.scene.control.ChoiceBox;
import sportscenter.DBManager;
import sportscenter.SportsCenter;
import sportscenter.ValidateData;

public class EditWyposazenieController implements Initializable {

    private Wyposazenie wyposazenie;
    private DBManager dbManager;
    
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
//            dbManager.getDbManagerWyposazenie().editWydarzenie(pracownik.getPESEL(), name.getText(), surname.getText(), PESEL.getText(), profession.getText(), salary.getText());
            ((Node) (event.getSource())).getScene().getWindow().hide();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        save.setText("Zapisz");
        this.dbManager = SportsCenter.dBManager;
    }
    
    public void setWyposazenie(Wyposazenie wyposazenie) {
        this.wyposazenie = wyposazenie;
        name.setText(wyposazenie.getName());
        sport.setText(wyposazenie.getSport());
        count.setText(Integer.toString(wyposazenie.getCount()));
//        building.setText(Integer.toString(wyposazenie.getBuildingId()));
//        hall.setText(wyposazenie.getSalary().toString());
    }
    
}
