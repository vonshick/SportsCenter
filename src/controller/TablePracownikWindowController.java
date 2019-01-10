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
        
//        PESEL;
//    public String surname;
//    public String name;
//    public String profession;
//    public Integer salary;
        
        TableColumn<Pracownik, String> peselColumn = new TableColumn<>("PESEL");
        peselColumn.setCellValueFactory(new PropertyValueFactory<>("PESEL"));

        TableColumn<Pracownik, String> surnameColumn = new TableColumn<>("surname");
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));

        TableColumn<Pracownik, String> nameColumn = new TableColumn<>("name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Pracownik, String> professionColumn = new TableColumn<>("profession");
        professionColumn.setCellValueFactory(new PropertyValueFactory<>("profession"));
        
        TableColumn<Pracownik, Float> salaryColumn = new TableColumn<>("salary");
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
        
        tableView.getColumns().addAll(peselColumn, surnameColumn, nameColumn, professionColumn, salaryColumn);
        showPracownicy();
    }
    
    @FXML
    private void changeTableView(MouseEvent event) throws IOException {
        dbManager.changeScene(event);
    }

    @FXML
    private void openNewPracownikWindow() throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddPracownik.fxml"));
//        Parent root = FXMLLoader.load(getClass().getResource("/view/AddSala.fxml")); //temporary test

        Stage stage = new Stage();
        stage.setTitle("Add new employee");
        stage.setScene(new Scene(root));
        stage.showAndWait();
        showPracownicy();
    }
    
    @FXML
    private void selectRowPracownik(MouseEvent event) throws IOException {
        if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            Pracownik pracownik = (Pracownik) tableView.getSelectionModel().getSelectedItem();
            if(pracownik != null) {
                System.out.println("Wybrano " + pracownik.getPESEL());
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/EditPracownik.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                EditPracownikController controller = fxmlLoader.<EditPracownikController>getController();
                controller.setPracownik(pracownik);
                controller.setDbManager(dbManager);
                Stage stage = new Stage();
                stage.setTitle("Edytuj Pracownika");
                stage.setScene(new Scene(root));
                stage.showAndWait();
                showPracownicy();
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
