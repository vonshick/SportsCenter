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
import sportscenter.Pracownik;
import sportscenter.SQLObject;
import sportscenter.SportsCenter;

public class TablePracownikWindowController implements Initializable {
    
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
        Pracownicy.setDisable(true);
        ObiektySportowe.setDisable(false);
        AddData.setText("Dodaj Pracownika");

        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        TableColumn<Pracownik, String> peselColumn = new TableColumn<>("pesel");
        peselColumn.setCellValueFactory(new PropertyValueFactory<>("pesel"));

        TableColumn<Pracownik, String> nazwiskoColumn = new TableColumn<>("nazwisko");
        nazwiskoColumn.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));

        TableColumn<Pracownik, String> imieColumn = new TableColumn<>("imie");
        imieColumn.setCellValueFactory(new PropertyValueFactory<>("imie"));

        TableColumn<Pracownik, String> funkcjaColumn = new TableColumn<>("funkcja");
        funkcjaColumn.setCellValueFactory(new PropertyValueFactory<>("funkcja"));
        tableView.getColumns().addAll(peselColumn, nazwiskoColumn, imieColumn, funkcjaColumn);

        showPracownicy();
    }
    
    @FXML
    private void changeTableView(MouseEvent event) throws IOException {
        dbManager.changeScene(event);
    }

    @FXML
    private void openNewPracownikWindow() throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddPracownik.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Add new employee");
        stage.setScene(new Scene(root));
        stage.showAndWait();
        showPracownicy();
    }
    
    @FXML
    private void selectRowPracownik(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            Pracownik pracownik = (Pracownik) tableView.getSelectionModel().getSelectedItem();
            tableView.getSelectionModel().clearSelection();
            if(pracownik != null) {
                System.out.println("Wybrano " + pracownik.getPesel());
            }
        }
    }
    
    private void showPracownicy() {

        
        ObservableList<SQLObject> sqlList = SportsCenter.manager.selectAll("pracownik");
        ObservableList<Pracownik> pracownicy = FXCollections.observableArrayList();
        for (SQLObject sQLObject : sqlList) {
            pracownicy.add((Pracownik) sQLObject);
        }
        tableView.getItems().clear();
        tableView.setItems(pracownicy);
    }

    public void setDbManager(DBManager manager) {
        this.dbManager = manager;
    }
    
}
