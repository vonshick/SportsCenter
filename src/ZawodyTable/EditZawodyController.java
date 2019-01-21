package ZawodyTable;

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
import ObiektSportowyTable.ObiektSportowy;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import sportscenter.DBManager;
import sportscenter.SQLObject;
import sportscenter.SportsCenter;
import sportscenter.ValidateData;

public class EditZawodyController implements Initializable {

    private Zawody zawody;
    private DBManager dbManager;
    
    @FXML
    private TextField name;
    @FXML
    private TextField date;
    @FXML
    private TextField disciplin;
    @FXML
    private TextField price;
    @FXML
    private ComboBox IDSportObject;
    @FXML
    private Button save;
    @FXML
    private Button delete;
    
    @FXML
    private void delete(MouseEvent event) throws IOException, SQLException {
        dbManager.getdBManagerZawody().deleteZawody(zawody.getName());
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    private void save(MouseEvent event) throws IOException, SQLException {
        try {
            String[] providedData = {name.getText(), date.getText(), disciplin.getText(), price.getText(), IDSportObject.getSelectionModel().getSelectedItem().toString()};
            if (ValidateData.isAnyEmpty(providedData)) {
                AlertBox.showAlert("None of fields can be empty");
            } else if (!ValidateData.isDate(providedData[1])) {
                AlertBox.showAlert("Incorrect date format");
            } else if (!ValidateData.isNumber(providedData[4])) {
                AlertBox.showAlert("ID musi być liczbą!");
            } else {
                System.out.println("clicked save");
                try {
                    dbManager.getdBManagerZawody().editZawody(zawody.getName(), providedData[0], ValidateData.stringToDate(providedData[1]), providedData[2], Float.parseFloat(providedData[3]), Integer.parseInt(providedData[4]));
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                } catch (NumberFormatException ex) {
                    AlertBox.showAlert("Cena musi być liczbą!");
                } catch (ParseException ex) {
                    AlertBox.showAlert("Zły format daty!");
                }
            }
        } catch (NullPointerException e) {
            AlertBox.showAlert("Pole ID Obiektu nie może być puste!");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        save.setText("Zapisz");
        this.dbManager = SportsCenter.dBManager;
    }

    public void setZawody(Zawody zawody) {
        this.zawody = zawody;
        GUI.AutoCompleteComboBoxListener<String> autoComplete = new GUI.AutoCompleteComboBoxListener<>(IDSportObject);
        ObservableList<SQLObject> sqlList = SportsCenter.dBManager.selectFromTable("obiekt_sportowy");
        List<String> idObject = new ArrayList<>();
        for (SQLObject sQLObject : sqlList) {
            idObject.add(((ObiektSportowy) sQLObject).getIdObiektu());
        }
        IDSportObject.getItems().addAll(idObject);
        name.setText(zawody.getName());
        date.setText(zawody.getDate());
        disciplin.setText(zawody.getDisciplin());
        price.setText(zawody.getPrice().toString());
        IDSportObject.getSelectionModel().select(zawody.getIDSportObject().toString());
    }
    
}
