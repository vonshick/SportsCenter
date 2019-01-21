package KarnetTable;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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

public class TableKarnetWindowController implements Initializable {
    
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
    
    @FXML
    private void removeOldPasses() throws SQLException{
        Alert alert = new Alert(AlertType.CONFIRMATION, 
                "Czy na pewno chcesz usunąć wszystkie przeterminowane karnety?", ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult()==ButtonType.YES){
            dbManager.getdBManagerKarnet().removeOldPasses();
            System.out.println("JESTEM");
            showKarnety();
        }
    }
    @FXML
    private void searchKarnet() throws IOException {
        String input = searchTextBox.getText().toLowerCase();
        if (input.isEmpty()) {
            showKarnety();
            return;
        }
        ObservableList<SQLObject> sqlList = SportsCenter.dBManager.selectFromTable("karnet");
        ObservableList<Karnet> karnety = FXCollections.observableArrayList();
        String[] patterns = input.split(",");
        for (int i = 0; i < patterns.length; i++) {
            patterns[i] = patterns[i].trim();
        }
        for (SQLObject sQLObject : sqlList) {
            Karnet karnet = (Karnet) sQLObject;
            String[] rowColumns = karnet.toString().toLowerCase().split(",");
            boolean add = true;
            for (String pattern : patterns) {
                if (pattern.isEmpty()) {
                    break;
                }
                int searchMode;
                if (pattern.startsWith("%") && pattern.endsWith("%")) {
                    searchMode = 1;
                } else if (pattern.startsWith("%")) {
                    searchMode = 2;
                } else if (pattern.endsWith("%")) {
                    searchMode = 3;
                } else {
                    searchMode = 4;
                }
                pattern = pattern.replace("%", "");
                boolean foundInColumn = false;
                for (String rowColumn : rowColumns) {
                    switch (searchMode) {
                        case 1: {
                            if (rowColumn.contains(pattern)) {
                                foundInColumn = true;
                            }
                            break;
                        }
                        case 2: {
                            if (rowColumn.endsWith(pattern)) {
                                foundInColumn = true;
                            }
                            break;
                        }
                        case 3: {
                            if (rowColumn.startsWith(pattern)) {
                                foundInColumn = true;
                            }
                            break;
                        }
                        case 4: {
                            if (rowColumn.equals(pattern)) {
                                foundInColumn = true;
                            }
                            break;
                        }
                    }
                    if (foundInColumn) {
                        break;
                    }
                }
                if (!foundInColumn) {
                    add = false;
                    break;
                }
            }
            if (add) {
                karnety.add(karnet);
            }
        }
        tableView.getItems().clear();
        tableView.setItems(karnety);
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
