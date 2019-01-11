package sportscenter;

import ObiektSportowyTable.DBManagerObiektSportowy;
import ObiektSportowyTable.ObiektSportowy;
import PracownikTable.Pracownik;
import PracownikTable.TablePracownikWindowController;
import ObiektSportowyTable.TableObiektSportowyWindowController;
import PracownikTable.DBManagerPracownik;
import TrenerTable.DBManagerTrener;
import TrenerTable.TableTrenerWindowController;
import TrenerTable.Trener;
import WyposazenieTable.DBManagerWyposazenie;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

public class DBManager {
    private Connection connection;
    private Stage primaryStage;
    private DBManagerPracownik dBManagerPracownik;
    private DBManagerTrener dBManagerTrener;
    private DBManagerObiektSportowy dbManagerObiektSportowy;
    private DBManagerWyposazenie dbManagerWyposazenie;

    
//    private TablePracownikWindowController MainWindowController;

    public DBManager(Connection connection) {
        this.connection = connection;
        this.dBManagerPracownik = new DBManagerPracownik(this);
        this.dBManagerTrener = new DBManagerTrener(this);
        this.dbManagerObiektSportowy = new DBManagerObiektSportowy(this);
        this.dbManagerWyposazenie = new DBManagerWyposazenie(this);

    }
    
    public void changeScene(String table) throws IOException {
        Parent root;
        switch (table) {
            case "obiekty sportowe": {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ObiektSportowyTable/TableObiektSportowyWindow.fxml"));
                root = (Parent) fxmlLoader.load();
                TableObiektSportowyWindowController controller = fxmlLoader.<TableObiektSportowyWindowController>getController();
                primaryStage.setTitle("Tabela Obiekt Sportowy");
                break;
            }
            case "pracownicy": {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/PracownikTable/TablePracownikWindow.fxml"));
                root = (Parent) fxmlLoader.load();
                TablePracownikWindowController controller = fxmlLoader.<TablePracownikWindowController>getController();
                primaryStage.setTitle("Tabela Pracownik");
                break;
            }
            case "trenerzy": {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/TrenerTable/TableTrenerWindow.fxml"));
                root = (Parent) fxmlLoader.load();
                TableTrenerWindowController controller = fxmlLoader.<TableTrenerWindowController>getController();
                primaryStage.setTitle("Tabela Trener");
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
    
    /**
     * Returns all data from specified table
     * @param table name of table
     * @return all data in table as ObservableList of SQLObject
     */
    public ObservableList<SQLObject> selectFromTable(String table) {
        ObservableList<SQLObject> sqlList = FXCollections.observableArrayList();
        try {
            Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery("select * from " + table);
            rs.beforeFirst();
            while (rs.next()) {
                switch (table) {
                    case "pracownik":
                        sqlList.add(new Pracownik(rs));
                        break;
                    case "obiekt_sportowy":
                        sqlList.add(new ObiektSportowy(rs));
                        break;
                    case "trener":
                        sqlList.add(new Trener(rs));
                        break;
                    case "karnet":
                        break;
                    case "klient":
                        break;
                    case "sala":
                        break;
                    case "uczestnik":
                        break;
                    case "wyposazenie":
                        break;
                    case "zajecia":
                        break;
                    case "zawody":
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

    public DBManagerPracownik getdBManagerPracownik() {
        return dBManagerPracownik;
    }

    public DBManagerTrener getdBManagerTrener() {
        return dBManagerTrener;
    }

    public DBManagerObiektSportowy getDbManagerObiektSportowy() {
        return dbManagerObiektSportowy;
    }
    
    public DBManagerWyposazenie getDbManagerWyposazenie(){
        return dbManagerWyposazenie;
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