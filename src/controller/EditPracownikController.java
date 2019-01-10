package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import sportscenter.DBManager;
import sportscenter.Pracownik;
import sportscenter.SportsCenter;

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
    private void save(MouseEvent event) throws IOException, SQLException {
        if (ifSomeEmpty()) {
            showAlert("None of fields can be empty");
        } else if (ifIncorrectPESEL()) {
            showAlert("Incorrect PESEL format");
        } else {
            System.out.println("clicked save");
            dbManager.editPracownik(pracownik.getPESEL(), name.getText(), surname.getText(), PESEL.getText(), profession.getText(), salary.getText());
            ((Node) (event.getSource())).getScene().getWindow().hide();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        save.setText("Zapisz");
    }

    private boolean ifIncorrectPESEL() {
        return (!(PESEL.getText().matches("[0-9]+") && PESEL.getText().length() == 11));
    }

    private boolean ifSomeEmpty() {
        return (name.getText().equals("") || surname.getText() == null
                || PESEL.getText() == null || profession.getText() == null
                || salary.getText() == null);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
        alert.showAndWait();
    }



    public void setPracownik(Pracownik pracownik) {
        this.pracownik = pracownik;
        name.setText(pracownik.getName());
        surname.setText(pracownik.getSurname());
        PESEL.setText(pracownik.getPESEL());
        profession.setText(pracownik.getProfession());
        salary.setText(pracownik.getSalary().toString());
    }

    public void setDbManager(DBManager dbManager) {
        this.dbManager = dbManager;
    }
    
}
