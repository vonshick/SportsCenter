package ZajeciaTable;

import TrenerTable.Trener;
import WyposazenieTable.Wyposazenie;
import ZajeciaTable.Zajecia;
import ZajeciaTable.*;
import ZajeciaTable.AddZajeciaController;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
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

public class TableZajeciaWindowController implements Initializable {
    
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
        AddData.setText("Dodaj zajęcia");
        selectTableView.setItems(FXCollections.observableArrayList("karnety", "klienci", "obiekty sportowe", "pracownicy", "sale", "trenerzy", "uczestnicy", "wyposazenie", "zajecia", "zawody"));
        selectTableView.getSelectionModel().select("zajecia");

        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        TableColumn<Zajecia, Integer> idColumn = new TableColumn<>("id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<Zajecia, String> dayColumn = new TableColumn<>("Dzien");
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("dayOfWeek"));

        TableColumn<Zajecia, Date> startColumn = new TableColumn<>("Start");
        startColumn.setCellValueFactory(new PropertyValueFactory<>("startHour"));

        TableColumn<Zajecia, Date> endColumn = new TableColumn<>("Koniec");
        endColumn.setCellValueFactory(new PropertyValueFactory<>("endHour"));

        TableColumn<Zajecia, String> sportColumn = new TableColumn<>("sport");
        sportColumn.setCellValueFactory(new PropertyValueFactory<>("sport"));

        TableColumn<Zajecia, Float> priceColumn = new TableColumn<>("Cena");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
//        
//        TableColumn<Zajecia, String> coachPESELColumn = new TableColumn<>("Trener PESEL");
//        coachPESELColumn.setCellValueFactory(new PropertyValueFactory<>("coachPESEL"));
//        
        TableColumn<Zajecia, String> coachNameColumn = new TableColumn<>("Trener");
        coachNameColumn.setCellValueFactory(new PropertyValueFactory<>("coachName"));
 
        TableColumn<Zajecia, Integer> buildingColumn = new TableColumn<>("Budynek");
        buildingColumn.setCellValueFactory(new PropertyValueFactory<>("buildingId"));

        TableColumn<Zajecia, String> hallColumn = new TableColumn<>("Sala");
        hallColumn.setCellValueFactory(new PropertyValueFactory<>("hallId"));
        
        tableView.getColumns().addAll(idColumn, dayColumn,startColumn, endColumn, sportColumn, priceColumn, coachNameColumn, buildingColumn, hallColumn);
        showZajecia();
    }
    
    @FXML
    private void changeTableView() throws IOException {
        String selected = selectTableView.getSelectionModel().getSelectedItem().toString();
        if (selected != null && !selected.equals("zajecia")) {
            dbManager.changeScene(selected);
        }
    }

    @FXML
    private void openNewZajeciaWindow() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ZajeciaTable/AddZajecia.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        AddZajeciaController controller = fxmlLoader.<AddZajeciaController>getController();
        Stage stage = new Stage();
        stage.setTitle("Dodaj Zajecia");
        stage.setScene(new Scene(root));
        stage.showAndWait();
        showZajecia();
    }
    
    @FXML
    private void selectRowZajecia(MouseEvent event) throws IOException {
        if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            Zajecia zajecia = (Zajecia) tableView.getSelectionModel().getSelectedItem();
            if(zajecia != null) {
//                System.out.println("Wybrano " + zajecia.getName());
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ZajeciaTable/EditZajecia.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                EditZajeciaController controller = fxmlLoader.<EditZajeciaController>getController();
                controller.initializeEditWindow(zajecia);
                Stage stage = new Stage();
                stage.setTitle("Edytuj Zajecia");
                stage.setScene(new Scene(root));
                stage.showAndWait();
                showZajecia();
            }
        }
    }
    
    @FXML
    private void searchZajecia() throws IOException {
        String input = searchTextBox.getText().toLowerCase();
        if (input.isEmpty()) {
            showZajecia();
            return;
        }
        ObservableList<SQLObject> sqlList = SportsCenter.dBManager.selectFromTable("v_zajecia");
        ObservableList<Zajecia> zajecia = FXCollections.observableArrayList();
        String[] patterns = input.split(",");
        for (int i = 0; i < patterns.length; i++) {
            patterns[i] = patterns[i].trim();
        }
        for (SQLObject sQLObject : sqlList) {
            Zajecia zajeciaData = (Zajecia) sQLObject;
            String[] rowColumns = zajeciaData.toString().toLowerCase().split(",");
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
                    System.out.println(rowColumn);
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
                zajecia.add(zajeciaData);
            }
        }
        tableView.getItems().clear();
        tableView.setItems(zajecia);
    }

    
    private void showZajecia() {
        ObservableList<SQLObject> sqlList = SportsCenter.dBManager.selectFromTable("v_zajecia");
        ObservableList<Zajecia> zajecia = FXCollections.observableArrayList();
        for (SQLObject sQLObject : sqlList) {
            zajecia.add((Zajecia) sQLObject);
        }
        tableView.getItems().clear();
        tableView.setItems(zajecia);
    }
    
}
