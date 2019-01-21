package SalaTable;

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

public class TableSalaWindowController implements Initializable {
    
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
        AddData.setText("Dodaj Salę");
        selectTableView.setItems(FXCollections.observableArrayList("karnety", "klienci", "obiekty sportowe", "pracownicy", "sale", "trenerzy", "uczestnicy", "wyposazenie", "zajecia", "zawody"));
        selectTableView.getSelectionModel().select("sale");

        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                
        TableColumn<Sala, String> hallColumn = new TableColumn<>("Sala");
        hallColumn.setCellValueFactory(new PropertyValueFactory<>("hallId"));
        
        TableColumn<Sala, Integer> buildingColumn = new TableColumn<>("Budynek");
        buildingColumn.setCellValueFactory(new PropertyValueFactory<>("buildingId"));

        
        tableView.getColumns().addAll(hallColumn, buildingColumn);
        showSala();
    }
    
    @FXML
    private void changeTableView() throws IOException {
        String selected = selectTableView.getSelectionModel().getSelectedItem().toString();
        if (selected != null && !selected.equals("sale")) {
            dbManager.changeScene(selected);
        }
    }

    @FXML
    private void openNewSalaWindow() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SalaTable/AddSala.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        AddSalaController controller = fxmlLoader.<AddSalaController>getController();
        Stage stage = new Stage();
        stage.setTitle("Dodaj Sale");
        stage.setScene(new Scene(root));
        stage.showAndWait();
        showSala();
    }
    
    @FXML
    private void selectRowSala(MouseEvent event) throws IOException {
        if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            Sala sala = (Sala) tableView.getSelectionModel().getSelectedItem();
            if(sala != null) {
                System.out.println("Wybrano " + sala.getHallId());
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SalaTable/EditSala.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                EditSalaController controller = fxmlLoader.<EditSalaController>getController();
                controller.initializeEditWindow(sala);
                Stage stage = new Stage();
                stage.setTitle("Edytuj Sala");
                stage.setScene(new Scene(root));
                stage.showAndWait();
                showSala();
            }
        }
    }
    
    @FXML
    private void searchSala() throws IOException {
        String input = searchTextBox.getText().toLowerCase();
        if (input.isEmpty()) {
            showSala();
            return;
        }
        ObservableList<SQLObject> sqlList = SportsCenter.dBManager.selectFromTable("sala");
        ObservableList<Sala> sale = FXCollections.observableArrayList();
        String[] patterns = input.split(",");
        for (int i = 0; i < patterns.length; i++) {
            patterns[i] = patterns[i].trim();
        }
        for (SQLObject sQLObject : sqlList) {
            Sala sala = (Sala) sQLObject;
            String[] rowColumns = sala.toString().toLowerCase().split(",");
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
                sale.add(sala);
            }
        }
        tableView.getItems().clear();
        tableView.setItems(sale);
    }
    
    private void showSala() {
        ObservableList<SQLObject> sqlList = SportsCenter.dBManager.selectFromTable("sala");
        ObservableList<Sala> sala = FXCollections.observableArrayList();
        for (SQLObject sQLObject : sqlList) {
            sala.add((Sala) sQLObject);
        }
        tableView.getItems().clear();
        tableView.setItems(sala);
    }
    
}
