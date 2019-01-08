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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class UserLogInController implements Initializable {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException{
        SportsCenter.connection.authenticateUser(username.getText(), password.getText());
        SportsCenter.manager = new DBManager(SportsCenter.connection.connect());
        ((Node)(event.getSource())).getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        Stage stage = new Stage();
        stage.setTitle("DBMS");
        stage.setScene(new Scene(root));
        stage.show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
