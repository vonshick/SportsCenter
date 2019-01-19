package TrenerTable;

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


public class TableTrenerWindowController implements Initializable {

    private DBManager dbManager;

    @FXML
    private TableView tableView;
    @FXML
    private ComboBox selectTableView;
    @FXML
    private TextField searchTextBox;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.dbManager = SportsCenter.dBManager;
        selectTableView.setItems(FXCollections.observableArrayList("karnety", "klienci", "obiekty sportowe", "pracownicy", "sale", "trenerzy", "uczestnicy", "wyposazenie", "zajecia", "zawody"));
        selectTableView.getSelectionModel().select("trenerzy");

        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        TableColumn<Trener, String> peselColumn = new TableColumn<>("PESEL");
        peselColumn.setCellValueFactory(new PropertyValueFactory<>("PESEL"));

        TableColumn<Trener, String> disciplinColumn = new TableColumn<>("Dyscyplina");
        disciplinColumn.setCellValueFactory(new PropertyValueFactory<>("disciplin"));

        tableView.getColumns().addAll(peselColumn, disciplinColumn);
        showTrenerzy();
    }

    @FXML
    private void changeTableView() throws IOException {
        String selected = selectTableView.getSelectionModel().getSelectedItem().toString();
        if (selected != null && !selected.equals("trenerzy")) {
            dbManager.changeScene(selected);
        }
    }

    @FXML
    private void selectRowTrener (MouseEvent event) throws IOException {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            Trener trener = (Trener) tableView.getSelectionModel().getSelectedItem();
            if (trener != null) {
                System.out.println("Wybrano " + trener.getPESEL());
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/TrenerTable/EditTrener.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                EditTrenerController controller = fxmlLoader.<EditTrenerController>getController();
                controller.setTrener(trener);
                Stage stage = new Stage();
                stage.setTitle("Edytuj Trenera");
                stage.setScene(new Scene(root));
                stage.showAndWait();
                showTrenerzy();
            }
        }
    }
    
    @FXML
    private void searchTrener() throws IOException {
        String input = searchTextBox.getText().toLowerCase();
        if (input.isEmpty()) {
            showTrenerzy();
            return;
        }
        ObservableList<SQLObject> sqlList = SportsCenter.dBManager.selectFromTable("trener");
        ObservableList<Trener> trenerzy = FXCollections.observableArrayList();
        String[] patterns = input.split(",");
        for (int i = 0; i < patterns.length; i++) {
            patterns[i] = patterns[i].trim();
        }
        for (SQLObject sQLObject : sqlList) {
            Trener trener = (Trener) sQLObject;
            String[] rowColumns = trener.toString().toLowerCase().split(",");
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
                trenerzy.add(trener);
            }
        }
        tableView.getItems().clear();
        tableView.setItems(trenerzy);
    }

    private void showTrenerzy() {
        ObservableList<SQLObject> sqlList = SportsCenter.dBManager.selectFromTable("trener");
        ObservableList<Trener> trenerzy = FXCollections.observableArrayList();
        for (SQLObject sQLObject : sqlList) {
            trenerzy.add((Trener) sQLObject);
        }
        tableView.getItems().clear();
        tableView.setItems(trenerzy);
    }   
    
}
