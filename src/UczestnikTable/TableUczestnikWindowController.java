package UczestnikTable;

import UczestnikTable.*;
import UczestnikTable.*;
import PracownikTable.*;
import UczestnikTable.AddUczestnikController;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sportscenter.DBManager;
import sportscenter.SQLObject;
import sportscenter.SportsCenter;

public class TableUczestnikWindowController implements Initializable {
    
    private DBManager dbManager;
    
    @FXML
    private TableView tableView;
    @FXML
    private Button AddData;
    @FXML
    private ComboBox selectTableView;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.dbManager = SportsCenter.dBManager;
        AddData.setText("Dodaj Uczestnika");
        selectTableView.setItems(FXCollections.observableArrayList("karnety", "klienci", "obiekty sportowe", "pracownicy", "sale", "trenerzy", "uczestnicy", "wyposazenie", "zajecia", "zawody"));
        selectTableView.getSelectionModel().select("sale");

        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                
        TableColumn<Uczestnik, String> peselColumn = new TableColumn<>("PESEL");
        peselColumn.setCellValueFactory(new PropertyValueFactory<>("PESEL"));   
        
        TableColumn<Uczestnik, String> surnameColumn = new TableColumn<>("Nazwisko");
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));       
        
        TableColumn<Uczestnik, String> nameColumn = new TableColumn<>("Imię");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));     
        
        TableColumn<Uczestnik, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        tableView.getColumns().addAll(peselColumn, surnameColumn, nameColumn, statusColumn);
        showUczestnik();
    }
    
    @FXML
    private void changeTableView() throws IOException {
        String selected = selectTableView.getSelectionModel().getSelectedItem().toString();
        if (selected != null && !selected.equals("uczestnicy")) {
            dbManager.changeScene(selected);
        }
    }

    @FXML
    private void openNewUczestnikWindow() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/UczestnikTable/AddUczestnik.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        AddUczestnikController controller = fxmlLoader.<AddUczestnikController>getController();
        Stage stage = new Stage();
        stage.setTitle("Add new uczestnik");
        stage.setScene(new Scene(root));
        stage.showAndWait();
        showUczestnik();
    }
    
    @FXML
    private void selectRowUczestnik(MouseEvent event) throws IOException {
        if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            Uczestnik uczestnik = (Uczestnik) tableView.getSelectionModel().getSelectedItem();
            if(uczestnik != null) {
                System.out.println("Wybrano " + uczestnik.getSurname());
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/UczestnikTable/EditUczestnik.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                EditUczestnikController controller = fxmlLoader.<EditUczestnikController>getController();
                controller.initializeEditWindow(uczestnik);
                Stage stage = new Stage();
                stage.setTitle("Edytuj Uczestnika");
                stage.setScene(new Scene(root));
                stage.showAndWait();
                showUczestnik();
            }
        }
    }
    
    private void showUczestnik() {
        ObservableList<SQLObject> sqlList = SportsCenter.dBManager.selectFromTable("uczestnik");
        ObservableList<Uczestnik> uczestnik = FXCollections.observableArrayList();
        for (SQLObject sQLObject : sqlList) {
            uczestnik.add((Uczestnik) sQLObject);
        }
        tableView.getItems().clear();
        tableView.setItems(uczestnik);
    }
    
}
