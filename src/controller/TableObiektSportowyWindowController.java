package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sportscenter.DBManager;
import sportscenter.ObiektSportowy;
import sportscenter.SQLObject;
import sportscenter.SportsCenter;

public class TableObiektSportowyWindowController implements Initializable {
    
    private DBManager dbManager;
    
    @FXML
    private TableView tableView;
    @FXML
    private Button Pracownicy;
    @FXML
    private Button ObiektySportowe;
    @FXML
    private Button AddData;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        SportsCenter.manager.setMainWindowController(this);
        Pracownicy.setDisable(false);
        ObiektySportowe.setDisable(true);
        AddData.setText("Dodaj Obiekt Sportwy");

        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        TableColumn<ObiektSportowy, String> idColumn = new TableColumn<>("idObiektu");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idObiektu"));

        TableColumn<ObiektSportowy, String> lokalizacjaColumn = new TableColumn<>("lokalizacja");
        lokalizacjaColumn.setCellValueFactory(new PropertyValueFactory<>("lokalizacja"));

        TableColumn<ObiektSportowy, String> nazwaColumn = new TableColumn<>("nazwa");
        nazwaColumn.setCellValueFactory(new PropertyValueFactory<>("nazwa"));

        TableColumn<ObiektSportowy, String> typObiektuColumn = new TableColumn<>("typObiektu");
        typObiektuColumn.setCellValueFactory(new PropertyValueFactory<>("typObiektu"));
        tableView.getColumns().addAll(idColumn, lokalizacjaColumn, nazwaColumn, typObiektuColumn);
        
        showObiektySportowe();
    }
    
    @FXML
    private void changeTableView(MouseEvent event) throws IOException {
        dbManager.changeScene(event);
    }
    
    @FXML
    private void selectRowObiektSporotwy(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            ObiektSportowy obiekt = (ObiektSportowy) tableView.getSelectionModel().getSelectedItem();
            tableView.getSelectionModel().clearSelection();
            System.out.println("Wybrano " + obiekt.getNazwa());
        }
    }
    
    @FXML
    private void openNewObiektSportowyWindow() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddObiektSportowy.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Add new sport facility");
        stage.setScene(new Scene(root));
        stage.showAndWait();
        showObiektySportowe();
    }
    
    private void showObiektySportowe() {


        ObservableList<SQLObject> sqlList = SportsCenter.manager.selectAll("obiekt_sportowy");
        ObservableList<ObiektSportowy> obiekty = FXCollections.observableArrayList();
        for (SQLObject sQLObject : sqlList) {
            obiekty.add((ObiektSportowy) sQLObject);
        }
        tableView.getItems().clear();
        tableView.setItems(obiekty);

//        try {
//            tableView.setItems(SportsCenter.manager.selectAll(ObiektSportowy.class));
//        } catch (InstantiationException ex) {
//            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
//        }
// //       tableView.setItems(SportsCenter.manager.selectAllObiektySportowe());
    }

    public void setDbManager(DBManager dbManager) {
        this.dbManager = dbManager;
    }
    
}
