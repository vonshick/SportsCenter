package ZawodyTable;

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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sportscenter.DBManager;
import sportscenter.SQLObject;
import sportscenter.SportsCenter;

public class TableZawodyWindowController implements Initializable {
    
    private DBManager dbManager;
    
    @FXML
    private TableView tableView;
    @FXML
    private Button AddData;
    @FXML
    private ComboBox selectTableView;
    @FXML
    private TextField searchTextBox;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.dbManager = SportsCenter.dBManager;
        AddData.setText("Dodaj Zawody");
        selectTableView.setItems(FXCollections.observableArrayList("karnety", "klienci", "obiekty sportowe", "pracownicy", "sale", "trenerzy", "uczestnicy", "wyposazenie", "zajecia", "zawody"));
        selectTableView.getSelectionModel().select("zawody");

        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        TableColumn<Zawody, String> name = new TableColumn<>("Nazwa");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        TableColumn<Zawody, String> date = new TableColumn<>("Data");
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        
        TableColumn<Zawody, String> disciplin = new TableColumn<>("Dyscyplina");
        disciplin.setCellValueFactory(new PropertyValueFactory<>("disciplin"));

        TableColumn<Zawody, Float> price = new TableColumn<>("Cena");
        price.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Zawody, Integer> IDSportObject = new TableColumn<>("ID Obiektu Sportowego");
        IDSportObject.setCellValueFactory(new PropertyValueFactory<>("IDSportObject"));
        
        tableView.getColumns().addAll(name, date, disciplin, price, IDSportObject);
        showZawody();
    }
    
    @FXML
    private void changeTableView() throws IOException {
        String selected = selectTableView.getSelectionModel().getSelectedItem().toString();
        if (selected != null && !selected.equals("zawody")) {
            dbManager.changeScene(selected);
        }
    }

    @FXML
    private void openNewZawodyWindow() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ZawodyTable/AddZawody.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        AddZawodyController controller = fxmlLoader.<AddZawodyController>getController();
        Stage stage = new Stage();
        stage.setTitle("Dodaj Zawody");
        stage.setScene(new Scene(root));
        stage.showAndWait();
        showZawody();
    }
    
    @FXML
    private void selectRowZawody(MouseEvent event) throws IOException {
        if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            Zawody zawody = (Zawody) tableView.getSelectionModel().getSelectedItem();
            if(zawody != null) {
                System.out.println("Wybrano " + zawody.getSth());
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ZawodyTable/EditZawody.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                EditZawodyController controller = fxmlLoader.<EditZawodyController>getController();
                controller.setZawody(zawody);
                Stage stage = new Stage();
                stage.setTitle("Edytuj Zawody");
                stage.setScene(new Scene(root));
                stage.showAndWait();
                showZawody();
            }
        }
    }
    
    @FXML
    private void searchZawody() throws IOException {
        String input = searchTextBox.getText().toLowerCase();
        ObservableList<SQLObject> sqlList = SportsCenter.dBManager.selectFromTable("zawody");
        ObservableList<Zawody> zawodyList = FXCollections.observableArrayList();
        String[] parts = input.split(",");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }
        for (SQLObject sQLObject : sqlList) {
            Zawody zawody = (Zawody) sQLObject;
            String rowToString = zawody.toString().toLowerCase();
            boolean add = true;
            for (int i = 0; i < parts.length; i++) {
                if (!rowToString.contains(parts[i])) {
                    add = false;
                    break;
                }
            }
            if (add) {
                zawodyList.add(zawody);
            }
        }
        tableView.getItems().clear();
        tableView.setItems(zawodyList);
    }
    
    private void showZawody() {
        ObservableList<SQLObject> sqlList = SportsCenter.dBManager.selectFromTable("zawody");
        ObservableList<Zawody> zawody = FXCollections.observableArrayList();
        for (SQLObject sQLObject : sqlList) {
            zawody.add((Zawody) sQLObject);
        }
        tableView.getItems().clear();
        tableView.setItems(zawody);
    }
    
}
