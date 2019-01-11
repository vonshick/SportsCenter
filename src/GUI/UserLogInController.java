package GUI;



import PracownikTable.TablePracownikWindowController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sportscenter.DBManager;
import sportscenter.SportsCenter;

public class UserLogInController implements Initializable {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button button;
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException{
        SportsCenter.connection.authenticateUser(username.getText(), password.getText());
        SportsCenter.dBManager = new DBManager(SportsCenter.connection.connect());
        ((Node)(event.getSource())).getScene().getWindow().hide();
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/PracownikTable/TablePracownikWindow.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        TablePracownikWindowController controller = fxmlLoader.<TablePracownikWindowController>getController();
        
        Stage stage = new Stage();
        SportsCenter.dBManager.setPrimaryStage(stage);
        stage.setTitle("Tabela Pracownicy");
        stage.setScene(new Scene(root));
        stage.show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
}
