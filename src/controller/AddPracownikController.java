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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import sportscenter.SportsCenter;

/**
 *
 * @author kuba
 */
public class AddPracownikController implements Initializable {
    
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
    private Button button;
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException, SQLException {
        insertIntoDB(name.getText(), surname.getText(), PESEL.getText(), profession.getText(), salary.getText());
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    private void insertIntoDB(String name, String surname, String PESEL, String profession, String salary) throws SQLException{
        try{
            PreparedStatement pstmt = SportsCenter.manager.getConnection().prepareStatement("INSERT INTO pracownik VALUES(?, ?, ?, ?, ?)");
            pstmt.setString(1, PESEL);
            pstmt.setString(2, surname);
            pstmt.setString(3, name);
            pstmt.setString(4, profession);
            pstmt.setFloat(5, Float.parseFloat(salary));
            pstmt.executeUpdate();
            System.out.println("Dodano pracownika!");
        }catch(SQLException e ){
            System.out.println("Employee inserting error");
            e.printStackTrace();
        }
    }
    
}

