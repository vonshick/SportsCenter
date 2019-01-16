package KarnetTable;

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

public class TableKarnetWindowController implements Initializable {
    
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
        AddData.setText("Dodaj Karnet");
        selectTableView.setItems(FXCollections.observableArrayList("karnety", "klienci", "obiekty sportowe", "pracownicy", "sale", "trenerzy", "uczestnicy", "wyposazenie", "zajecia", "zawody"));
        selectTableView.getSelectionModel().select("karnety");

        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        TableColumn<Karnet, Integer> idClientColumn = new TableColumn<>("ID Klienta");
        idClientColumn.setCellValueFactory(new PropertyValueFactory<>("IDClient"));
        
        TableColumn<Karnet, Integer> idActivityColumnColumn = new TableColumn<>("ID Zajęć");
        idActivityColumnColumn.setCellValueFactory(new PropertyValueFactory<>("IDActivity"));
        
        TableColumn<Karnet, Float> priceColumn = new TableColumn<>("Cena");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Karnet, String> dateStartColumn = new TableColumn<>("Wazny od");
        dateStartColumn.setCellValueFactory(new PropertyValueFactory<>("dateStart"));

        TableColumn<Karnet, String> dateEndColumn = new TableColumn<>("Wazny do");
        dateEndColumn.setCellValueFactory(new PropertyValueFactory<>("dateEnd"));
        
        tableView.getColumns().addAll(idClientColumn, idActivityColumnColumn, priceColumn, dateStartColumn, dateEndColumn);
        showKarnety();
    }
    
    @FXML
    private void changeTableView() throws IOException {
        String selected = selectTableView.getSelectionModel().getSelectedItem().toString();
        if (selected != null && !selected.equals("karnety")) {
            dbManager.changeScene(selected);
        }
    }

    @FXML
    private void openNewKarnetWindow() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/KarnetTable/AddKarnet.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        AddKarnetController controller = fxmlLoader.<AddKarnetController>getController();
        Stage stage = new Stage();
        stage.setTitle("Dodaj Karnet");
        stage.setScene(new Scene(root));
        stage.showAndWait();
        showKarnety();
    }
    
    @FXML
    private void selectRowKarnet(MouseEvent event) throws IOException {
        if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            Karnet karnet = (Karnet) tableView.getSelectionModel().getSelectedItem();
            if(karnet != null) {
                System.out.println("Wybrano " + karnet.getSth());
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/KarnetTable/EditKarnet.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                EditKarnetController controller = fxmlLoader.<EditKarnetController>getController();
                controller.setKarnet(karnet);
                Stage stage = new Stage();
                stage.setTitle("Edytuj Karnet");
                stage.setScene(new Scene(root));
                stage.showAndWait();
                showKarnety();
            }
        }
    }
    
    private void showKarnety() {
        ObservableList<SQLObject> sqlList = SportsCenter.dBManager.selectFromTable("karnet");
        ObservableList<Karnet> karnety = FXCollections.observableArrayList();
        for (SQLObject sQLObject : sqlList) {
            karnety.add((Karnet) sQLObject);
        }
        tableView.getItems().clear();
        tableView.setItems(karnety);
    }
    
}
