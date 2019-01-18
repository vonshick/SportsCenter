package PracownikTable;

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

public class TablePracownikWindowController implements Initializable {
    
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
        AddData.setText("Dodaj Pracownika");
        selectTableView.setItems(FXCollections.observableArrayList("karnety", "klienci", "obiekty sportowe", "pracownicy", "sale", "trenerzy", "uczestnicy", "wyposazenie", "zajecia", "zawody"));
        selectTableView.getSelectionModel().select("pracownicy");

        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        TableColumn<Pracownik, String> peselColumn = new TableColumn<>("PESEL");
        peselColumn.setCellValueFactory(new PropertyValueFactory<>("PESEL"));

        TableColumn<Pracownik, String> surnameColumn = new TableColumn<>("Nazwisko");
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));

        TableColumn<Pracownik, String> nameColumn = new TableColumn<>("Imię");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Pracownik, String> professionColumn = new TableColumn<>("Stanowisko");
        professionColumn.setCellValueFactory(new PropertyValueFactory<>("profession"));
        
        TableColumn<Pracownik, Float> salaryColumn = new TableColumn<>("Płaca");
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
        
        tableView.getColumns().addAll(peselColumn, surnameColumn, nameColumn, professionColumn, salaryColumn);
        showPracownicy();
    }
    
    @FXML
    private void changeTableView() throws IOException {
        String selected = selectTableView.getSelectionModel().getSelectedItem().toString();
        if (selected != null && !selected.equals("pracownicy")) {
            dbManager.changeScene(selected);
        }
    }

    @FXML
    private void openNewPracownikWindow() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/PracownikTable/AddPracownik.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        AddPracownikController controller = fxmlLoader.<AddPracownikController>getController();
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
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/PracownikTable/EditPracownik.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                EditPracownikController controller = fxmlLoader.<EditPracownikController>getController();
                controller.setPracownik(pracownik);
                Stage stage = new Stage();
                stage.setTitle("Edytuj Pracownika");
                stage.setScene(new Scene(root));
                stage.showAndWait();
                showPracownicy();
            }
        }
    }
    
    @FXML
    private void searchPracownik() throws IOException {
        String input = searchTextBox.getText().toLowerCase();
        if (input.isEmpty()) {
            showPracownicy();
            return;
        }
        ObservableList<SQLObject> sqlList = SportsCenter.dBManager.selectFromTable("pracownik");
        ObservableList<Pracownik> pracownicy = FXCollections.observableArrayList();
        String[] patterns = input.split(",");
        for (int i = 0; i < patterns.length; i++) {
            patterns[i] = patterns[i].trim();
        }
        for (SQLObject sQLObject : sqlList) {
            Pracownik pracownik = (Pracownik) sQLObject;
            String[] rowColumns = pracownik.toString().toLowerCase().split(",");
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
                pracownicy.add(pracownik);
            }
        }
        tableView.getItems().clear();
        tableView.setItems(pracownicy);
    }
    
    private void showPracownicy() {
        ObservableList<SQLObject> sqlList = SportsCenter.dBManager.selectFromTable("pracownik");
        ObservableList<Pracownik> pracownicy = FXCollections.observableArrayList();
        for (SQLObject sQLObject : sqlList) {
            pracownicy.add((Pracownik) sQLObject);
        }
        tableView.getItems().clear();
        tableView.setItems(pracownicy);
    }
    
}
