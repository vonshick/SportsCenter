package sportscenter;

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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author kuba
 */
public class ServerSetUpController implements Initializable {
    
    @FXML
    private TextField address;
    @FXML
    private TextField port;
    @FXML
    private TextField sid;
    @FXML
    private Button button;
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        SportsCenter.connection = new DBConnection(address.getText(), port.getText(), sid.getText());        
        openUserLogInWindow(event);
    }
    private void openUserLogInWindow(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("UserLogIn.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Log in");
        stage.setScene(new Scene(root));
        stage.show();
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
