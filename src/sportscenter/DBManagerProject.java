package sportscenter;

import ObiektSportowyTable.ObiektSportowy;
import PracownikTable.Pracownik;
import PracownikTable.TablePracownikWindowController;
import ObiektSportowyTable.TableObiektSportowyWindowController;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
//import com.google.common.reflect.TypeToken;

/**
 *
 * @author Piter
 */
public class DBManagerProject <ControllerType>{
    private Connection connection;
    private Stage primaryStage;
    private static Connection connections;
    private String relationName;
    private Map<String, String> fxmlNames;
//    private TablePracownikWindowController MainWindowController;

    public DBManagerProject(Connection connection) {

        initializeFXMLNames();
        this.connection = connection;
        connections = connection;
    }
    
    private void initializeFXMLNames(){
        fxmlNames = new HashMap<String, String>();
        fxmlNames.put("Pracownicy", "/view/TablePracownicyWindow.fxml");
        fxmlNames.put("ObiektySportowe", "/view/TableObiektSportowyWindow.fxml");
    }
    
    public void changeScene(MouseEvent event) throws IOException {
        Button btn = (Button) event.getSource();
//        String btnId = btn.getId();
        String tableFXMLName = fxmlNames.get(btn.getId());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(tableFXMLName));
        Parent root = (Parent) fxmlLoader.load();
        ControllerType controller = fxmlLoader.<ControllerType>getController();
//        controller.setDbManager(SportsCenter.manager);
        primaryStage.setTitle("Tabela "+divideStringByUppercase(btn.getId()));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    
    private String divideStringByUppercase(String tableName){
        String[] wordsArray = tableName.split("(?=\\p{Upper})");
        String properName="";
        if (wordsArray.length != 1){
            for (String s: wordsArray){
                properName=properName+" "+s;
            }
        } else{
            properName = wordsArray[0];
        }
        return properName;
    }
    
    public ObservableList<SQLObject> selectAll(String table) {
        ObservableList<SQLObject> sqlList = FXCollections.observableArrayList();
        try {
            Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery("select * from " + table);
            rs.beforeFirst();
            while (rs.next()) {
                System.out.println(rs.getString(1));
//                T sqlData sqlData = new T(rs);
                
                switch (table) {
                    case "pracownik":
                       
                        Pracownik pracownik = new Pracownik(rs);
                        sqlList.add(pracownik);
                        break;
                    case "obiekt_sportowy":
                        ObiektSportowy obiekt = new ObiektSportowy(rs);
                        sqlList.add(obiekt);
                        break;
                    default:
                        throw new AssertionError();
                }
            }
            try {
                stmt.close();
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            System.out.println("Bład wykonania polecenia" + ex.toString());
        }
        return sqlList;
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
