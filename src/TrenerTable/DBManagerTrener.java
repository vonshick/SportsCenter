package TrenerTable;

import sportscenter.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;



/*


//TODO sprawdzenie wiezow pracownik - trener


*/

public class DBManagerTrener {
    
    private DBManager dBManager;

    public DBManagerTrener(DBManager dBManager) {
        this.dBManager = dBManager;
    }
    
    public void editTrener(String oldPESEL, String newPESEL, String disciplin) {
        try {
            PreparedStatement pstmt = SportsCenter.dBManager.getConnection().prepareStatement("update trener set pesel = ?, dyscyplina = ? where pesel = ?");
            pstmt.setString(1, newPESEL);
            pstmt.setString(2, disciplin);
            pstmt.setString(3, oldPESEL);
            pstmt.executeQuery();
            SportsCenter.dBManager.getConnection().commit();
            System.out.println("Trener updated!");
        } catch (SQLException e) {
            System.out.println("Trener update error");
        }
    }

    public void insertNewTrener(String PESEL, String sport) throws SQLException {
        System.out.println("TODO insert");
//        try {
//            PreparedStatement pstmt = SportsCenter.dBManager.getConnection().prepareStatement("INSERT INTO trener VALUES(?, ?)");
//            pstmt.setString(1, PESEL);
//            pstmt.setString(2, sport);
//            pstmt.executeUpdate();
//            System.out.println("Coach added");
//            SportsCenter.dBManager.getConnection().commit();
//            SportsCenter.dBManager.getConnection().setAutoCommit(true);
//        } catch (SQLException e) {
//            SportsCenter.dBManager.getConnection().rollback();
//            SportsCenter.dBManager.getConnection().setAutoCommit(true);
//            System.out.println("Coach inserting error");
//        }
    }
    
    public DBManager getdBManager() {
        return dBManager;
    }
    
}