package PracownikTable;

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
import javafx.event.ActionEvent;
import sportscenter.DBManager;
import sportscenter.SportsCenter;
import sportscenter.ValidateData;

public class EditPracownikController implements Initializable {

    private Pracownik pracownik;
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
    private Button save;
    @FXML
    private Button delete;
    
    @FXML
    private void delete(MouseEvent event) throws IOException, SQLException {
        dbManager.getdBManagerPracownik().deletePracownik(pracownik.getPESEL());
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
    
    @FXML
    private void save(ActionEvent event) throws IOException, SQLException {
        String[] providedData = { name.getText(), surname.getText(), PESEL.getText(), profession.getText(), salary.getText() };
        if(ValidateData.isAnyEmpty(providedData)){
            AlertBox.showAlert("Żadne pole nie może być puste!");
        }else if (ValidateData.isIncorrectPESEL(PESEL.getText())){
            AlertBox.showAlert("Niepoprawny PESEL!");
        } else {
            try{
                Float salaryValue = Float.parseFloat(providedData[4]);
                dbManager.getdBManagerPracownik().editPracownik(pracownik.getPESEL(), name.getText(), surname.getText(), PESEL.getText(), profession.getText(), salaryValue);
                if (!providedData[3].toLowerCase().equals(pracownik.getProfession().toLowerCase())
                        && ((providedData[3].toLowerCase().equals("trener") && !pracownik.getProfession().toLowerCase().equals("trenerka"))
                        || (providedData[3].toLowerCase().equals("trenerka") && !pracownik.getProfession().toLowerCase().equals("trener")))) {
                    dbManager.getdBManagerPracownik().openAddTrenerWindow(event, providedData[2]);
                }
                ((Node) (event.getSource())).getScene().getWindow().hide();
            } catch(Exception e){
                AlertBox.showAlert("Cena musi być liczbą!");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        save.setText("Zapisz");
        this.dbManager = SportsCenter.dBManager;
    }

    public void setPracownik(Pracownik pracownik) {
        this.pracownik = pracownik;
        name.setText(pracownik.getName());
        surname.setText(pracownik.getSurname());
        PESEL.setText(pracownik.getPESEL());
        profession.setText(pracownik.getProfession());
        salary.setText(pracownik.getSalary().toString());
    }
    
}
