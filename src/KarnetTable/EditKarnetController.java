package KarnetTable;

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
import KlientTable.Klient;
import ZajeciaTable.Zajecia;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import sportscenter.SQLObject;
import sportscenter.DBManager;
import sportscenter.SportsCenter;
import sportscenter.ValidateData;

public class EditKarnetController implements Initializable {

    private Karnet karnet;
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
    private Button save;
    @FXML
    private Button delete;
    
    @FXML
    private void delete(MouseEvent event) throws IOException, SQLException {
        dbManager.getdBManagerKarnet().deleteKarnet(karnet.getIDClient(), karnet.getIDActivity());
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
    
    @FXML
    private void save(MouseEvent event) throws IOException, SQLException {
        try {
            String[] providedData = {idClient.getSelectionModel().getSelectedItem().toString(), idActivity.getSelectionModel().getSelectedItem().toString(), price.getText(), dateStart.getText(), dateEnd.getText()};
            if (ValidateData.isAnyEmpty(providedData)) {
                AlertBox.showAlert("Wszystkie pola muszą być wypełnione!");
            } else {
                System.out.println("clicked save");
                dbManager.getdBManagerKarnet().editKarnet(karnet.getIDClient(), karnet.getIDActivity(), providedData[0], providedData[1], providedData[2], providedData[3], providedData[4]);
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        } catch (NullPointerException e) {
            AlertBox.showAlert("Pola ID Obiektu i ID Klienta nie mogą być puste!");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        save.setText("Zapisz");
        this.dbManager = SportsCenter.dBManager;
    }

    public void setKarnet(Karnet karnet) {
        this.karnet = karnet;
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
        idClient.getSelectionModel().select((Integer.toString(karnet.getIDClient())));
        idActivity.getSelectionModel().select(Integer.toString(karnet.getIDActivity()));
        price.setText(Float.toString(karnet.getPrice()));
        dateStart.setText(karnet.getDateStart());
        dateEnd.setText(karnet.getDateEnd());
    }
    
}
