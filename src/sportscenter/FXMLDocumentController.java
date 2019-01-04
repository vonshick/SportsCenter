/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportscenter;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 *
 * @author kuba
 */
public class FXMLDocumentController implements Initializable {
    
//    @FXML
    private TextField address;
    private TextField port;
    private TextField sid;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
//        SportsCenter.dbManager = new DBConnection(address.getText(), port.getText(), sid.getText());
//        SportsCenter.dbManager.initialize(address.getText(), port.getText(), sid.getText());
        SportsCenter.dbManager = new DBConnection("localhost", "1521", "xe");

        SportsCenter.dbManager.start();

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
