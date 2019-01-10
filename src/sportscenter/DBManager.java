package sportscenter;

import controller.TablePracownikWindowController;
import controller.TableObiektSportowyWindowController;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
public class DBManager {
    private Connection connection;
    private Stage primaryStage;
    private static Connection connections;
//    private TablePracownikWindowController MainWindowController;

    public DBManager(Connection connection) {
        this.connection = connection;
        connections = connection;
    }
    
    public void changeScene(MouseEvent event) throws IOException {
        Button btn = (Button) event.getSource();
        String btnId = btn.getId();
        Parent root;
        switch (btnId) {
            case "ObiektySportowe": {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/TableObiektSportowyWindow.fxml"));
                root = (Parent) fxmlLoader.load();
                TableObiektSportowyWindowController controller = fxmlLoader.<TableObiektSportowyWindowController>getController();
                controller.setDbManager(SportsCenter.manager);
                primaryStage.setTitle("Tabela Obiekty Sportowe");
                break;
            }
            case "Pracownicy": {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/TablePracownikWindow.fxml"));
                root = (Parent) fxmlLoader.load();
                TablePracownikWindowController controller = fxmlLoader.<TablePracownikWindowController>getController();
                controller.setDbManager(SportsCenter.manager);
                primaryStage.setTitle("Tabela Pracownicy");
                break;
            }
            default: {
                System.out.println("Bad ID, not changing view.");
                return;
            }
        }
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    
    public void editPracownik(String oldPESEL, String name, String surname, String newPESEL, String profession, String salary) {
        try {
            PreparedStatement pstmt = SportsCenter.manager.getConnection().prepareStatement("update pracownik set pesel = ?, nazwisko = ?, imie = ?, funkcja = ?, placa = ? where pesel = ?");
            pstmt.setString(1, newPESEL);
            pstmt.setString(2, surname);
            pstmt.setString(3, name);
            pstmt.setString(4, profession);
            pstmt.setFloat(5, Float.parseFloat(salary));
            pstmt.setString(6, oldPESEL);
            pstmt.executeQuery();
            SportsCenter.manager.getConnection().commit();
            System.out.println("Employee updated!");
        } catch (SQLException e) {
            System.out.println("Employee update error");
        }
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
//public <T extends SQLObject> ObservableList<T> selectAllGeneric(Class<T> classType, T obj) throws InstantiationException, IllegalAccessException {
//        ObservableList<T> queryResult = FXCollections.observableArrayList();
//        try {
//            Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
//                    ResultSet.CONCUR_READ_ONLY);
//            ResultSet rs = stmt.executeQuery("select * from pracownik");
//            rs.beforeFirst();
//            while (rs.next()) {
//                //System.out.println(rs.getString(1));
////                final ParameterizedClass<T> pc = new ParameterizedClass<T>() {};
////                final T obj = (T) pc.type.getRawType().newInstance();
//                obj.loadFromSql(rs);
//                System.out.println(obj.getSth());
//                queryResult.add(obj);
//            }
//            try {
//                stmt.close();
//                rs.close();
//            
//
//} catch (SQLException ex) {
//                Logger.getLogger(DBConnection.class
//.getName()).log(Level.SEVERE, null, ex);
//            }
//        } catch (SQLException ex) {
//            System.out.println("Bład wykonania polecenia" + ex.toString());
//        }
//        return queryResult;
//    }
//    public ObservableList<Pracownik> selectAllPracownicy() {
//        ObservableList<Pracownik> pracownicy = FXCollections.observableArrayList();
//        try {
//            Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
//                    ResultSet.CONCUR_READ_ONLY);
//            ResultSet rs = stmt.executeQuery("select * from pracownik");
//            rs.beforeFirst();
//            while (rs.next()) {
//                System.out.println(rs.getString(1));
//                pracownicy.add(new Pracownik(rs));
//            }
//            try {
//                stmt.close();
//                rs.close();
//            } catch (SQLException ex) {
//                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        } catch (SQLException ex) {
//            System.out.println("Bład wykonania polecenia" + ex.toString());
//        }
//        return pracownicy;
//    }
//
//    public ObservableList<ObiektSportowy> selectAllObiektySportowe() {
//        ObservableList<ObiektSportowy> obiekty = FXCollections.observableArrayList();
//        try {
//            Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
//                    ResultSet.CONCUR_READ_ONLY);
//            ResultSet rs = stmt.executeQuery("select * from obiekt_sportowy");
//            rs.beforeFirst();
//            while (rs.next()) {
//                obiekty.add(new ObiektSportowy(rs));
//            }
//            try {
//                stmt.close();
//                rs.close();
//            } catch (SQLException ex) {
//                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        } catch (SQLException ex) {
//            System.out.println("Bład wykonania polecenia" + ex.toString());
//        }
//        return obiekty;
//    }
    
//    public void setMainWindowController(TablePracownikWindowController MainWindowController) {
//        this.MainWindowController = MainWindowController;
//    }
    
//static abstract class ParameterizedClass<T> {
//
//    final Class<T> type;
//
//    public ParameterizedClass(Class<T> type) {
//        this.type = type;
//    }
//
//    public ObservableList<SQLObject> selectAllGenericClass() {
//        ObservableList<T> queryResult = FXCollections.observableArrayList();
//        try {
//            Statement stmt = connections.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
//                    ResultSet.CONCUR_READ_ONLY);
//            ResultSet rs = stmt.executeQuery("select * from pracownik");
//            rs.beforeFirst();
//            while (rs.next()) {
//                //System.out.println(rs.getString(1));
//                //                final ParameterizedClass<T> pc = new ParameterizedClass<T>() {};
//                //                final T obj = (T) pc.type.getRawType().newInstance();
//                type obj
//                .loadFromSql(rs);
//                System.out.println(obj.getSth());
//                queryResult.add(obj);
//            }
//            try {
//                stmt.close();
//                rs.close();
//            } catch (SQLException ex) {
//                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        } catch (SQLException ex) {
//            System.out.println("Bład wykonania polecenia" + ex.toString());
//        }
//        return queryResult;
//    }
//}