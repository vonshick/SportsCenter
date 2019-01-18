package ObiektSportowyTable;

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

public class TableObiektSportowyWindowController implements Initializable {
    
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
//        SportsCenter.manager.setMainWindowController(this);
        this.dbManager = SportsCenter.dBManager;
        AddData.setText("Dodaj Obiekt Sportwy");
        selectTableView.setItems(FXCollections.observableArrayList("karnety", "klienci", "obiekty sportowe", "pracownicy", "sale", "trenerzy", "uczestnicy", "wyposazenie", "zajecia", "zawody"));
        selectTableView.getSelectionModel().select("obiekty sportowe");

        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        TableColumn<ObiektSportowy, String> idColumn = new TableColumn<>("idObiektu");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idObiektu"));

        TableColumn<ObiektSportowy, String> lokalizacjaColumn = new TableColumn<>("lokalizacja");
        lokalizacjaColumn.setCellValueFactory(new PropertyValueFactory<>("lokalizacja"));

        TableColumn<ObiektSportowy, String> nazwaColumn = new TableColumn<>("nazwa");
        nazwaColumn.setCellValueFactory(new PropertyValueFactory<>("nazwa"));

        TableColumn<ObiektSportowy, String> typObiektuColumn = new TableColumn<>("typObiektu");
        typObiektuColumn.setCellValueFactory(new PropertyValueFactory<>("typObiektu"));
        tableView.getColumns().addAll(idColumn, lokalizacjaColumn, nazwaColumn, typObiektuColumn);
        
        showObiektySportowe();
    }
    
    @FXML
    private void changeTableView() throws IOException {
        String selected = selectTableView.getSelectionModel().getSelectedItem().toString();
        if(selected != null && !selected.equals("obiekty sportowe")) {
            dbManager.changeScene(selected);
        }
    }
    
    @FXML
    private void selectRowObiektSporotwy(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            ObiektSportowy obiekt = (ObiektSportowy) tableView.getSelectionModel().getSelectedItem();
            tableView.getSelectionModel().clearSelection();
            System.out.println("Wybrano " + obiekt.getNazwa());
        }
    }
    
    @FXML
    private void openNewObiektSportowyWindow() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ObiektSportowyTable/AddObiektSportowy.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Add new sport facility");
        stage.setScene(new Scene(root));
        stage.showAndWait();
        showObiektySportowe();
    }
    
    @FXML
    private void searchObiektSportowy() throws IOException {
        String input = searchTextBox.getText().toLowerCase();
        if (input.isEmpty()) {
            showObiektySportowe();
            return;
        }
        ObservableList<SQLObject> sqlList = SportsCenter.dBManager.selectFromTable("obiekt_sportowy");
        ObservableList<ObiektSportowy> obiekty = FXCollections.observableArrayList();
        String[] patterns = input.split(",");
        for (int i = 0; i < patterns.length; i++) {
            patterns[i] = patterns[i].trim();
        }
        for (SQLObject sQLObject : sqlList) {
            ObiektSportowy obiektSportowy = (ObiektSportowy) sQLObject;
            String[] rowColumns = obiektSportowy.toString().toLowerCase().split(",");
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
                obiekty.add(obiektSportowy);
            }
        }
        tableView.getItems().clear();
        tableView.setItems(obiekty);
    }
    
    private void showObiektySportowe() {
        ObservableList<SQLObject> sqlList = SportsCenter.dBManager.selectFromTable("obiekt_sportowy");
        ObservableList<ObiektSportowy> obiekty = FXCollections.observableArrayList();
        for (SQLObject sQLObject : sqlList) {
            obiekty.add((ObiektSportowy) sQLObject);
        }
        tableView.getItems().clear();
        tableView.setItems(obiekty);
    }

}
