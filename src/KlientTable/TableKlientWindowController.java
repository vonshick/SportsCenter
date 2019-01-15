package KlientTable;

import PracownikTable.*;
import WyposazenieTable.AddWyposazenieController;
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

public class TableKlientWindowController implements Initializable {
    
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
        AddData.setText("Dodaj Klienta");
        selectTableView.setItems(FXCollections.observableArrayList("karnety", "klienci", "obiekty sportowe", "pracownicy", "sale", "trenerzy", "uczestnicy", "wyposazenie", "zajecia", "zawody"));
        selectTableView.getSelectionModel().select("klienci");

        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        TableColumn<Klient, Integer> IDColumn = new TableColumn<>("ID Klienta");
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));

        TableColumn<Klient, String> surnameColumn = new TableColumn<>("Nazwisko");
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));

        TableColumn<Klient, String> nameColumn = new TableColumn<>("Imie");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        tableView.getColumns().addAll(IDColumn, surnameColumn, nameColumn);
        showKlienci();
    }
    
    @FXML
    private void changeTableView() throws IOException {
        String selected = selectTableView.getSelectionModel().getSelectedItem().toString();
        if (selected != null && !selected.equals("klienci")) {
            dbManager.changeScene(selected);
        }
    }

    @FXML
    private void openNewKlientWindow() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/KlientTable/AddKlient.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        AddKlientController controller = fxmlLoader.<AddKlientController>getController();
        Stage stage = new Stage();
        stage.setTitle("Dodaj Klienta");
        stage.setScene(new Scene(root));
        stage.showAndWait();
        showKlienci();
    }
    
    @FXML
    private void selectRowKlient(MouseEvent event) throws IOException {
        if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            Klient klient = (Klient) tableView.getSelectionModel().getSelectedItem();
            if(klient != null) {
                System.out.println("Wybrano " + klient.getSurname());
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/KlientTable/EditKlient.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                EditKlientController controller = fxmlLoader.<EditKlientController>getController();
                controller.setKlient(klient);
                Stage stage = new Stage();
                stage.setTitle("Edytuj Klienta");
                stage.setScene(new Scene(root));
                stage.showAndWait();
                showKlienci();
            }
        }
    }
    
    private void showKlienci() {
        ObservableList<SQLObject> sqlList = SportsCenter.dBManager.selectFromTable("klient");
        ObservableList<Klient> klienci = FXCollections.observableArrayList();
        for (SQLObject sQLObject : sqlList) {
            klienci.add((Klient) sQLObject);
        }
        tableView.getItems().clear();
        tableView.setItems(klienci);
    }
    
}
