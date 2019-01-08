/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportscenter;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Piter
 */
public class MainWindowController implements Initializable {
    
    @FXML
    private TableView tableView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SportsCenter.manager.setMainWindowController(this);
        //showPracownicy();
        showObiektySportowe();
    }
    
    private void showPracownicy() {
        TableColumn<Pracownik, String> peselColumn = new TableColumn<>("pesel");
        peselColumn.setCellValueFactory(new PropertyValueFactory<>("pesel"));
        
        TableColumn<Pracownik, String> nazwiskoColumn = new TableColumn<>("nazwisko");
        nazwiskoColumn.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));
        
        TableColumn<Pracownik, String> imieColumn = new TableColumn<>("imie");
        imieColumn.setCellValueFactory(new PropertyValueFactory<>("imie"));
        
        TableColumn<Pracownik, String> funkcjaColumn = new TableColumn<>("funkcja");
        funkcjaColumn.setCellValueFactory(new PropertyValueFactory<>("funkcja"));
        
//        try {
//            tableView.setItems(SportsCenter.manager.selectAll(Pracownik.class));
//        } catch (InstantiationException ex) {
//            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
//        }
        ObservableList<SQLObject> sqlList = SportsCenter.manager.selectAll("pracownik");
        ObservableList<Pracownik> pracownicy = FXCollections.observableArrayList();
        for (SQLObject sQLObject : sqlList) {
            pracownicy.add((Pracownik)sQLObject);
        }
        tableView.setItems(pracownicy);
        tableView.getColumns().addAll(peselColumn, nazwiskoColumn, imieColumn, funkcjaColumn);
    }
    
    private void showObiektySportowe() {
        TableColumn<ObiektSportowy, Integer> idColumn = new TableColumn<>("idObiektu");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idObiektu"));
        
        TableColumn<ObiektSportowy, String> lokalizacjaColumn = new TableColumn<>("lokalizacja");
        lokalizacjaColumn.setCellValueFactory(new PropertyValueFactory<>("lokalizacja"));
        
        TableColumn<ObiektSportowy, String> nazwaColumn = new TableColumn<>("nazwa");
        nazwaColumn.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
        
        TableColumn<ObiektSportowy, String> typObiektuColumn = new TableColumn<>("typObiektu");
        typObiektuColumn.setCellValueFactory(new PropertyValueFactory<>("typObiektu"));

//        try {
//            tableView.setItems(SportsCenter.manager.selectAll(ObiektSportowy.class));
//        } catch (InstantiationException ex) {
//            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
//        }
// //       tableView.setItems(SportsCenter.manager.selectAllObiektySportowe());
        
        ObservableList<SQLObject> sqlList = SportsCenter.manager.selectAll("obiekt_sportowy");
        ObservableList<ObiektSportowy> obiekty = FXCollections.observableArrayList();
        for (SQLObject sQLObject : sqlList) {
            obiekty.add((ObiektSportowy) sQLObject);
        }
        tableView.setItems(obiekty);
        tableView.getColumns().addAll(idColumn, lokalizacjaColumn, nazwaColumn, typObiektuColumn);
    }
    
}
