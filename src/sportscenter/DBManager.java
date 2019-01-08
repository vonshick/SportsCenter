package sportscenter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
//import com.google.common.reflect.TypeToken;

/**
 *
 * @author Piter
 */
public class DBManager {
    private Connection connection;
    private static Connection connections;
    private MainWindowController MainWindowController;

    public DBManager(Connection connection) {
        this.connection = connection;
        connections = connection;
    }
    

    
    public <T extends SQLObject> ObservableList<T> selectAllGeneric(Class<T> classType, T obj) throws InstantiationException, IllegalAccessException {
        ObservableList<T> queryResult = FXCollections.observableArrayList();
        try {
            Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery("select * from pracownik");
            rs.beforeFirst();
            while (rs.next()) {
                //System.out.println(rs.getString(1));
//                final ParameterizedClass<T> pc = new ParameterizedClass<T>() {};
//                final T obj = (T) pc.type.getRawType().newInstance();
                obj.loadFromSql(rs);
                System.out.println(obj.getSth());
                queryResult.add(obj);
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
        return queryResult;
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
    
    public void setMainWindowController(MainWindowController MainWindowController) {
        this.MainWindowController = MainWindowController;
    }
    
}
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