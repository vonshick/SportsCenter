package SalaTable;

import SalaTable.*;
import PracownikTable.*;
import SalaTable.AddSalaController;
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

public class TableSalaWindowController implements Initializable {
    
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
        AddData.setText("Dodaj Salę");
        selectTableView.setItems(FXCollections.observableArrayList("karnety", "klienci", "obiekty sportowe", "pracownicy", "sale", "trenerzy", "uczestnicy", "wyposazenie", "zajecia", "zawody"));
        selectTableView.getSelectionModel().select("sale");

        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                
        TableColumn<Sala, String> hallColumn = new TableColumn<>("Sala");
        hallColumn.setCellValueFactory(new PropertyValueFactory<>("hallId"));
        
        TableColumn<Sala, Integer> buildingColumn = new TableColumn<>("Budynek");
        buildingColumn.setCellValueFactory(new PropertyValueFactory<>("buildingId"));

        
        tableView.getColumns().addAll(hallColumn, buildingColumn);
        showSala();
    }
    
    @FXML
    private void changeTableView() throws IOException {
        String selected = selectTableView.getSelectionModel().getSelectedItem().toString();
        if (selected != null && !selected.equals("sale")) {
            dbManager.changeScene(selected);
        }
    }

    @FXML
    private void openNewSalaWindow() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SalaTable/AddSala.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        AddSalaController controller = fxmlLoader.<AddSalaController>getController();
        Stage stage = new Stage();
        stage.setTitle("Add new sala");
        stage.setScene(new Scene(root));
        stage.showAndWait();
        showSala();
    }
    
    @FXML
    private void selectRowSala(MouseEvent event) throws IOException {
        if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            Sala sala = (Sala) tableView.getSelectionModel().getSelectedItem();
            if(sala != null) {
                System.out.println("Wybrano " + sala.getHallId());
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SalaTable/EditSala.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                EditSalaController controller = fxmlLoader.<EditSalaController>getController();
                controller.initializeEditWindow(sala);
                Stage stage = new Stage();
                stage.setTitle("Edytuj Sala");
                stage.setScene(new Scene(root));
                stage.showAndWait();
                showSala();
            }
        }
    }
    
    private void showSala() {
        ObservableList<SQLObject> sqlList = SportsCenter.dBManager.selectFromTable("sala");
        ObservableList<Sala> sala = FXCollections.observableArrayList();
        for (SQLObject sQLObject : sqlList) {
            sala.add((Sala) sQLObject);
        }
        tableView.getItems().clear();
        tableView.setItems(sala);
    }
    
}
