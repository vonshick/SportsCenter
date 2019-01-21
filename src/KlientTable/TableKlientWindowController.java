package KlientTable;

import KarnetTable.Karnet;
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

public class TableKlientWindowController implements Initializable {
    
    private DBManager dbManager;
    @FXML
    private TableView tableView;
    @FXML
    private Button AddData;
    @FXML
    private ComboBox selectTableView;
    @FXML
    private TextField searchTextBox;
    @FXML 
    private Button delete;
    
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
     private void deleteKlient() throws SQLException{
        Klient klient  = (Klient) tableView.getSelectionModel().getSelectedItem();
        dbManager.getDbManagerKlient().deleteKlient(klient.getID());
        delete.setDisable(true);
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
        if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
            delete.setDisable(false);
        }
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
    
    @FXML
    private void searchKlient() throws IOException {
        String input = searchTextBox.getText().toLowerCase();
        if (input.isEmpty()) {
            showKlienci();
            return;
        }
        ObservableList<SQLObject> sqlList = SportsCenter.dBManager.selectFromTable("klient");
        ObservableList<Klient> klienci = FXCollections.observableArrayList();
        String[] patterns = input.split(",");
        for (int i = 0; i < patterns.length; i++) {
            patterns[i] = patterns[i].trim();
        }
        for (SQLObject sQLObject : sqlList) {
            Klient klient = (Klient) sQLObject;
            String[] rowColumns = klient.toString().toLowerCase().split(",");
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
                klienci.add(klient);
            }
        }
        tableView.getItems().clear();
        tableView.setItems(klienci);
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
