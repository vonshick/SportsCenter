package sportscenter;

import KarnetTable.DBManagerKarnet;
import KarnetTable.Karnet;
import KarnetTable.TableKarnetWindowController;
import KlientTable.DBManagerKlient;
import KlientTable.Klient;
import KlientTable.TableKlientWindowController;
import ObiektSportowyTable.DBManagerObiektSportowy;
import ObiektSportowyTable.ObiektSportowy;
import PracownikTable.Pracownik;
import PracownikTable.TablePracownikWindowController;
import ObiektSportowyTable.TableObiektSportowyWindowController;
import PracownikTable.DBManagerPracownik;
import SalaTable.DBManagerSala;
import SalaTable.Sala;
import SalaTable.TableSalaWindowController;
import TrenerTable.DBManagerTrener;
import TrenerTable.TableTrenerWindowController;
import TrenerTable.Trener;
import UczestnikTable.DBManagerUczestnik;
import UczestnikTable.TableUczestnikWindowController;
import UczestnikTable.Uczestnik;
import WyposazenieTable.DBManagerWyposazenie;
import WyposazenieTable.TableWyposazenieWindowController;
import WyposazenieTable.Wyposazenie;
import ZawodyTable.TableZawodyWindowController;
import ZawodyTable.Zawody;
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
import javafx.stage.Stage;

public class DBManager {
    private Connection connection;
    private Stage primaryStage;
    private DBManagerPracownik dBManagerPracownik;
    private DBManagerTrener dBManagerTrener;
    private DBManagerObiektSportowy dbManagerObiektSportowy;
    private DBManagerWyposazenie dbManagerWyposazenie;
    private DBManagerKlient dbManagerKlient;
    private DBManagerKarnet dBManagerKarnet;
    private DBManagerSala dbManagerSala;
    private DBManagerUczestnik dbManagerUczestnik;

    
//    private TablePracownikWindowController MainWindowController;

    public DBManager(Connection connection) {
        this.connection = connection;
        this.dBManagerPracownik = new DBManagerPracownik(this);
        this.dBManagerTrener = new DBManagerTrener(this);
        this.dbManagerObiektSportowy = new DBManagerObiektSportowy(this);
        this.dbManagerWyposazenie = new DBManagerWyposazenie(this);
        this.dbManagerKlient = new DBManagerKlient(this);
        this.dBManagerKarnet = new DBManagerKarnet(this);
        this.dbManagerSala = new DBManagerSala(this);
        this.dbManagerUczestnik = new DBManagerUczestnik(this);

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
            case "wyposazenie": {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/WyposazenieTable/TableWyposazenieWindow.fxml"));
                root = (Parent) fxmlLoader.load();
                TableWyposazenieWindowController controller = fxmlLoader.<TableWyposazenieWindowController>getController();
                primaryStage.setTitle("Tabela Wyposazenie");
                break;
            }
            case "klienci": {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/KlientTable/TableKlientWindow.fxml"));
                root = (Parent) fxmlLoader.load();
                TableKlientWindowController controller = fxmlLoader.<TableKlientWindowController>getController();
                primaryStage.setTitle("Tabela Klient");
                break;
            }
            case "karnety": {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/KarnetTable/TableKarnetWindow.fxml"));
                root = (Parent) fxmlLoader.load();
                TableKarnetWindowController controller = fxmlLoader.<TableKarnetWindowController>getController();
                primaryStage.setTitle("Tabela Karnet");
                break;
            }
            case "zawody": {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ZawodyTable/TableZawodyWindow.fxml"));
                root = (Parent) fxmlLoader.load();
                TableZawodyWindowController controller = fxmlLoader.<TableZawodyWindowController>getController();
                primaryStage.setTitle("Tabela Zawody");
                break;
            }
            case "sale": {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SalaTable/TableSalaWindow.fxml"));
                root = (Parent) fxmlLoader.load();
                TableSalaWindowController controller = fxmlLoader.<TableSalaWindowController>getController();
                primaryStage.setTitle("Tabela Sale");
                break;
            }
            case "uczestnicy": {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/UczestnikTable/TableUczestnikWindow.fxml"));
                root = (Parent) fxmlLoader.load();
                TableUczestnikWindowController controller = fxmlLoader.<TableUczestnikWindowController>getController();
                primaryStage.setTitle("Tabela Uczestnicy");
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
                        sqlList.add(new Karnet(rs));
                        break;
                    case "klient":
                        sqlList.add(new Klient(rs));
                        break;
                    case "sala":
                        sqlList.add(new Sala(rs));
                        break;
                    case "uczestnik":
                        sqlList.add(new Uczestnik(rs));
                        break;
                    case "wyposazenie":
                        sqlList.add(new Wyposazenie(rs));
                    case "zajecia":
                        break;
                    case "zawody":
                        sqlList.add(new Zawody(rs));
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

    public DBManagerKlient getDbManagerKlient() {
        return dbManagerKlient;
    }

    public DBManagerKarnet getdBManagerKarnet() {
        return dBManagerKarnet;
    }

    public DBManagerSala getDbManagerSala() {
        return dbManagerSala;
    }

    public DBManagerUczestnik getDbManagerUczestnik() {
        return dbManagerUczestnik;
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