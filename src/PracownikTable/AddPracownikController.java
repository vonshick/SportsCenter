package PracownikTable;

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
import sportscenter.DBManager;
import sportscenter.SportsCenter;
import sportscenter.ValidateData;

public class AddPracownikController implements Initializable {
    
    private DBManager dbManager;
    
    @FXML
    private TextField name;
    @FXML
    private TextField surname;
    @FXML
    private TextField PESEL;
    @FXML
    private TextField profession;
    @FXML
    private TextField salary;
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException, SQLException {
        String[] providedData = { name.getText(), surname.getText(), PESEL.getText(), profession.getText(), salary.getText() };
        if(ValidateData.isAnyEmpty(providedData)){
            AlertBox.showAlert("Żadne pole nie może być puste!");
        }else if (ValidateData.isIncorrectPESEL(PESEL.getText())){
            AlertBox.showAlert("Niepoprawny PESEL!");
        } else {
            try{
                Float salaryValue = Float.parseFloat(providedData[4]);
                dbManager.getdBManagerPracownik().insertNewPracownik(providedData[0], providedData[1], providedData[2], providedData[3],salaryValue, event);
                ((Node)(event.getSource())).getScene().getWindow().hide();
            }catch(Exception e){
                AlertBox.showAlert("Wynagrodzenie musi być liczbą!");
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.dbManager = SportsCenter.dBManager;
    }    
}

