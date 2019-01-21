package KarnetTable;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import GUI.AlertBox;
import KlientTable.Klient;
import ZajeciaTable.Zajecia;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import sportscenter.DBManager;
import sportscenter.SQLObject;
import sportscenter.SportsCenter;
import sportscenter.ValidateData;

public class AddKarnetController implements Initializable {
    
    private DBManager dbManager;
    
    @FXML
    private ComboBox idClient;
    @FXML
    private ComboBox idActivity;
    @FXML
    private TextField price;
    @FXML
    private TextField dateStart;
    @FXML
    private TextField dateEnd;

    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException, SQLException {
        try {
            String[] providedData = {idClient.getSelectionModel().getSelectedItem().toString(), idActivity.getSelectionModel().getSelectedItem().toString(), price.getText(), dateStart.getText(), dateEnd.getText()};
            if (ValidateData.isAnyEmpty(providedData)) {
                AlertBox.showAlert("Wszystkie pola muszą być wypełnione!");
            } else {
                dbManager.getdBManagerKarnet().insertNewKarnet(providedData[0], providedData[1], providedData[2], providedData[3], providedData[4]);
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        } catch (NullPointerException e) {
            AlertBox.showAlert("Pola ID Obiektu i ID Klienta nie mogą być puste!");
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.dbManager = SportsCenter.dBManager;
        GUI.AutoCompleteComboBoxListener<String> idClientAutoComplete = new GUI.AutoCompleteComboBoxListener<>(idClient);
        ObservableList<SQLObject> sqlList = SportsCenter.dBManager.selectFromTable("klient");
        List<Integer> idKlients = new ArrayList<>();
        for (SQLObject sQLObject : sqlList) {
            idKlients.add(((Klient) sQLObject).getID());
        }
        idClient.getItems().addAll(idKlients);
        GUI.AutoCompleteComboBoxListener<String> idActivityAutoComplete = new GUI.AutoCompleteComboBoxListener<>(idActivity);
        sqlList = SportsCenter.dBManager.selectFromTable("v_zajecia");
        List<Integer> idZajecia = new ArrayList<>();
        for (SQLObject sQLObject : sqlList) {
            idZajecia.add(((Zajecia) sQLObject).getId());
        }
        idActivity.getItems().addAll(idZajecia);
    }
}

