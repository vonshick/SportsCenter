package UczestnikTable;

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

public class TableUczestnikWindowController implements Initializable {
    
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
        AddData.setText("Dodaj Uczestnika");
        selectTableView.setItems(FXCollections.observableArrayList("karnety", "klienci", "obiekty sportowe", "pracownicy", "sale", "trenerzy", "uczestnicy", "wyposazenie", "zajecia", "zawody"));
        selectTableView.getSelectionModel().select("uczestnicy");

        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                
        TableColumn<Uczestnik, String> peselColumn = new TableColumn<>("PESEL");
        peselColumn.setCellValueFactory(new PropertyValueFactory<>("PESEL"));   
        
        TableColumn<Uczestnik, String> surnameColumn = new TableColumn<>("Nazwisko");
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));       
        
        TableColumn<Uczestnik, String> nameColumn = new TableColumn<>("Imię");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));     
        
        TableColumn<Uczestnik, Integer> statusColumn = new TableColumn<>("Status");
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
    
    @FXML
    private void searchUczestnik() throws IOException {
        String input = searchTextBox.getText().toLowerCase();
        if (input.isEmpty()) {
            showUczestnik();
            return;
        }
        ObservableList<SQLObject> sqlList = SportsCenter.dBManager.selectFromTable("uczestnik");
        ObservableList<Uczestnik> uczestnicy = FXCollections.observableArrayList();
        String[] patterns = input.split(",");
        for (int i = 0; i < patterns.length; i++) {
            patterns[i] = patterns[i].trim();
        }
        for (SQLObject sQLObject : sqlList) {
            Uczestnik uczestnik = (Uczestnik) sQLObject;
            String[] rowColumns = uczestnik.toString().toLowerCase().split(",");
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
                uczestnicy.add(uczestnik);
            }
        }
        tableView.getItems().clear();
        tableView.setItems(uczestnicy);
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
