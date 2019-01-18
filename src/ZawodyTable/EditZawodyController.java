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
import java.text.ParseException;
import sportscenter.DBManager;
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
    private TextField IDSportObject;
    @FXML
    private Button save;

    @FXML
    private void save(MouseEvent event) throws IOException, SQLException {
        String[] providedData = { name.getText(), date.getText(), disciplin.getText(), price.getText(), IDSportObject.getText() };
        if(ValidateData.isAnyEmpty(providedData)){
            AlertBox.showAlert("None of fields can be empty");
        }else if (!ValidateData.isDate(providedData[1])){
            AlertBox.showAlert("Incorrect date format");
        } else {
            System.out.println("clicked save");
            try {
                dbManager.getdBManagerZawody().editZawody(zawody.getName(), providedData[0], providedData[1], providedData[2], providedData[3], providedData[4]);
            } catch (ParseException ex) {
                System.out.println("Parsing date failed, wrong format");
            }
            ((Node) (event.getSource())).getScene().getWindow().hide();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        save.setText("Zapisz");
        this.dbManager = SportsCenter.dBManager;
    }

    public void setZawody(Zawody zawody) {
        this.zawody = zawody;
        name.setText(zawody.getName());
        date.setText(zawody.getDate());
        disciplin.setText(zawody.getDisciplin());
        price.setText(zawody.getPrice().toString());
        IDSportObject.setText(zawody.getIDSportObject().toString());
    }
    
}
