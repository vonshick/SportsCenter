package TrenerTable;

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
import PracownikTable.Pracownik;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import sportscenter.DBManager;
import sportscenter.SQLObject;
import sportscenter.SportsCenter;
import sportscenter.ValidateData;

public class EditTrenerController implements Initializable {

    private Trener trener;
    private DBManager dbManager;
    
    @FXML
    private TextField PESEL;
    @FXML
    private TextField disciplin;
    @FXML
    private Button save;
    @FXML
    private Button delete;

    @FXML
    private void delete(MouseEvent event) throws IOException, SQLException {
        dbManager.getdBManagerTrener().deleteTrener(trener.getPESEL());
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
    
    @FXML
    private void save(MouseEvent event) throws IOException, SQLException {
        if (ifSomeEmpty()) {
            AlertBox.showAlert("None of fields can be empty");
        } else if (ValidateData.isIncorrectPESEL(PESEL.getText())) {
            AlertBox.showAlert("Incorrect PESEL format");
        } else {
            System.out.println("clicked save");
            dbManager.getdBManagerTrener().editTrener(trener.getPESEL(), PESEL.getText(), disciplin.getText());
            ((Node) (event.getSource())).getScene().getWindow().hide();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        save.setText("Zapisz");
        this.dbManager = SportsCenter.dBManager;
    }

    private boolean ifSomeEmpty() {
        try {
            return (disciplin.getText().equals("") || PESEL.getText().equals(""));
        } catch (NullPointerException ex) {
            return true;
        }
    }

    public void setTrener(Trener trener) {
        this.trener = trener;
        PESEL.setText(trener.getPESEL());
        disciplin.setText(trener.getDisciplin());
    }
    
}
