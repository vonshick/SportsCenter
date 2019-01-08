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

public class MainWindowController implements Initializable {
    
    @FXML
    private TableView tableView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SportsCenter.manager.setMainWindowController(this);
        ObservableList<SQLObject> sqlList = SportsCenter.manager.selectAll("pracownik");
        showPracownicy(sqlList);
//        ObservableList<SQLObject> sqlList = SportsCenter.manager.selectAll("obiekt_sportowy");
//        showObiektySportowe(sqlList);
    }
    
    private void showPracownicy(ObservableList<SQLObject> sqlList) {
        TableColumn<Pracownik, String> peselColumn = new TableColumn<>("pesel");
        peselColumn.setCellValueFactory(new PropertyValueFactory<>("pesel"));
        
        TableColumn<Pracownik, String> nazwiskoColumn = new TableColumn<>("nazwisko");
        nazwiskoColumn.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));
        
        TableColumn<Pracownik, String> imieColumn = new TableColumn<>("imie");
        imieColumn.setCellValueFactory(new PropertyValueFactory<>("imie"));
        
        TableColumn<Pracownik, String> funkcjaColumn = new TableColumn<>("funkcja");
        funkcjaColumn.setCellValueFactory(new PropertyValueFactory<>("funkcja"));
        
        ObservableList<Pracownik> pracownicy = FXCollections.observableArrayList();
        for (SQLObject sQLObject : sqlList) {
            pracownicy.add((Pracownik) sQLObject);
        }
        tableView.getItems().clear();
        tableView.setItems(pracownicy);
        tableView.getColumns().addAll(peselColumn, nazwiskoColumn, imieColumn, funkcjaColumn);  
    }
    
    private void showObiektySportowe(ObservableList<SQLObject> sqlList) {
        TableColumn<ObiektSportowy, String> idColumn = new TableColumn<>("idObiektu");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idObiektu"));
        
        TableColumn<ObiektSportowy, String> lokalizacjaColumn = new TableColumn<>("lokalizacja");
        lokalizacjaColumn.setCellValueFactory(new PropertyValueFactory<>("lokalizacja"));
        
        TableColumn<ObiektSportowy, String> nazwaColumn = new TableColumn<>("nazwa");
        nazwaColumn.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
        
        TableColumn<ObiektSportowy, String> typObiektuColumn = new TableColumn<>("typObiektu");
        typObiektuColumn.setCellValueFactory(new PropertyValueFactory<>("typObiektu"));
        
        ObservableList<ObiektSportowy> obiekty = FXCollections.observableArrayList();
        for (SQLObject sQLObject : sqlList) {
            obiekty.add((ObiektSportowy) sQLObject);
        }
        tableView.getItems().clear();
        tableView.setItems(obiekty);
        tableView.getColumns().addAll(idColumn, lokalizacjaColumn, nazwaColumn, typObiektuColumn);

//        try {
//            tableView.setItems(SportsCenter.manager.selectAll(ObiektSportowy.class));
//        } catch (InstantiationException ex) {
//            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
//        }
// //       tableView.setItems(SportsCenter.manager.selectAllObiektySportowe());
    }
    
}
