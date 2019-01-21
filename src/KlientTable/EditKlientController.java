package KlientTable;

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
import sportscenter.DBManager;
import sportscenter.SportsCenter;
import sportscenter.ValidateData;

public class EditKlientController implements Initializable {

    private Klient klient;
    private DBManager dbManager;
    
    @FXML
    private TextField name;
    @FXML
    private TextField surname;
    @FXML
    private Button save;

    @FXML
    private void save(MouseEvent event) throws IOException, SQLException {
        String[] providedData = { name.getText(), surname.getText() };
        if(ValidateData.isAnyEmpty(providedData)){
            AlertBox.showAlert("Wszystkie pola muszą być wypełnione!");
        } else {
            System.out.println("clicked save");
            dbManager.getDbManagerKlient().editKlient(klient.getID(), providedData[0], providedData[1]);
            ((Node) (event.getSource())).getScene().getWindow().hide();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        save.setText("Zapisz");
        this.dbManager = SportsCenter.dBManager;
    }

    public void setKlient(Klient klient) {
        this.klient = klient;
        name.setText(klient.getName());
        surname.setText(klient.getSurname());
    }
    
}
