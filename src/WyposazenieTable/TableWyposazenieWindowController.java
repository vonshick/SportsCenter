package WyposazenieTable;

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

public class TableWyposazenieWindowController implements Initializable {
    
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
        AddData.setText("Dodaj Wyposażenie");
        selectTableView.setItems(FXCollections.observableArrayList("karnety", "klienci", "obiekty sportowe", "pracownicy", "sale", "trenerzy", "uczestnicy", "wyposazenie", "zajecia", "zawody"));
        selectTableView.getSelectionModel().select("wyposazenie");

        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        TableColumn<Wyposazenie, String> idColumn = new TableColumn<>("id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Wyposazenie, String> nameColumn = new TableColumn<>("name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Wyposazenie, String> sportColumn = new TableColumn<>("sport");
        sportColumn.setCellValueFactory(new PropertyValueFactory<>("sport"));

        TableColumn<Wyposazenie, Integer> countColumn = new TableColumn<>("count");
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
        
        TableColumn<Wyposazenie, Integer> buildingColumn = new TableColumn<>("building");
        buildingColumn.setCellValueFactory(new PropertyValueFactory<>("buildingId"));

        TableColumn<Wyposazenie, String> hallColumn = new TableColumn<>("hall");
        hallColumn.setCellValueFactory(new PropertyValueFactory<>("hallId"));
        
        tableView.getColumns().addAll(idColumn, nameColumn, sportColumn, countColumn, buildingColumn, hallColumn);
        showWyposazenie();
    }
    
    @FXML
    private void changeTableView() throws IOException {
        String selected = selectTableView.getSelectionModel().getSelectedItem().toString();
        if (selected != null && !selected.equals("wyposazenie")) {
            dbManager.changeScene(selected);
        }
    }

    @FXML
    private void openNewWyposazenieWindow() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/WyposazenieTable/AddWyposazenie.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        AddWyposazenieController controller = fxmlLoader.<AddWyposazenieController>getController();
        Stage stage = new Stage();
        stage.setTitle("Add new equipment");
        stage.setScene(new Scene(root));
        stage.showAndWait();
        showWyposazenie();
    }
    
    @FXML
    private void selectRowWyposazenie(MouseEvent event) throws IOException {
        if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            Wyposazenie wyposazenie = (Wyposazenie) tableView.getSelectionModel().getSelectedItem();
            if(wyposazenie != null) {
                System.out.println("Wybrano " + wyposazenie.getName());
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/WyposazenieTable/EditWyposazenie.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                EditWyposazenieController controller = fxmlLoader.<EditWyposazenieController>getController();
                controller.initializeEditWindow(wyposazenie);
                Stage stage = new Stage();
                stage.setTitle("Edytuj Wyposazenie");
                stage.setScene(new Scene(root));
                stage.showAndWait();
                showWyposazenie();
            }
        }
    }
    
    @FXML
    private void searchWyposazenie() throws IOException {
        String input = searchTextBox.getText().toLowerCase();
        if (input.isEmpty()) {
            showWyposazenie();
            return;
        }
        ObservableList<SQLObject> sqlList = SportsCenter.dBManager.selectFromTable("wyposazenie");
        ObservableList<Wyposazenie> wyposazenie = FXCollections.observableArrayList();
        String[] patterns = input.split(",");
        for (int i = 0; i < patterns.length; i++) {
            patterns[i] = patterns[i].trim();
        }
        for (SQLObject sQLObject : sqlList) {
            Wyposazenie sprzet = (Wyposazenie) sQLObject;
            String[] rowColumns = sprzet.toString().toLowerCase().split(",");
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
                wyposazenie.add(sprzet);
            }
        }
        tableView.getItems().clear();
        tableView.setItems(wyposazenie);
    }

    
    private void showWyposazenie() {
        ObservableList<SQLObject> sqlList = SportsCenter.dBManager.selectFromTable("wyposazenie");
        ObservableList<Wyposazenie> wyposazenie = FXCollections.observableArrayList();
        for (SQLObject sQLObject : sqlList) {
            wyposazenie.add((Wyposazenie) sQLObject);
        }
        tableView.getItems().clear();
        tableView.setItems(wyposazenie);
    }
    
}
