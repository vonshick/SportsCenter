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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
