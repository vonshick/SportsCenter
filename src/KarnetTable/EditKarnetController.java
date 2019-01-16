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
import sportscenter.DBManager;
import sportscenter.SportsCenter;
import sportscenter.ValidateData;

public class EditKarnetController implements Initializable {

    private Karnet karnet;
    private DBManager dbManager;
    
    @FXML
    private TextField idClient;
    @FXML
    private TextField idActivity;
    @FXML
    private TextField price;
    @FXML
    private TextField dateStart;
    @FXML
    private TextField dateEnd;
    @FXML
    private Button save;

    @FXML
    private void save(MouseEvent event) throws IOException, SQLException {
        String[] providedData = { idClient.getText(), idActivity.getText(), price.getText(), dateStart.getText(), dateEnd.getText() };
        if(ValidateData.isAnyEmpty(providedData)){
            AlertBox.showAlert("Wszystkie pola muszą być wypełnione!");
        } else {
            System.out.println("clicked save");
            dbManager.getdBManagerKarnet().editKarnet(karnet.getIDClient(), karnet.getIDActivity(), providedData[0], providedData[1], providedData[2], providedData[3], providedData[4]);
            ((Node) (event.getSource())).getScene().getWindow().hide();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        save.setText("Zapisz");
        this.dbManager = SportsCenter.dBManager;
    }

    public void setKarnet(Karnet karnet) {
        this.karnet = karnet;
        idClient.setText(Integer.toString(karnet.getIDClient()));
        idActivity.setText(Integer.toString(karnet.getIDActivity()));
        price.setText(Float.toString(karnet.getPrice()));
        dateStart.setText(karnet.getDateStart());
        dateEnd.setText(karnet.getDateEnd());
    }
    
}
