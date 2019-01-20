package TrenerTable;

import sportscenter.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBManagerTrener {
    
    private DBManager dBManager;

    public DBManagerTrener(DBManager dBManager) {
        this.dBManager = dBManager;
    }
    
    public void deleteTrener(String PESEL) {
        try {
            PreparedStatement pstmt = SportsCenter.dBManager.getConnection().prepareStatement("DELETE FROM trener WHERE PESEL = ?");
            pstmt.setString(1, PESEL);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ValidateData.printSQLException(ex, PESEL);
        }
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
            ValidateData.printSQLException(e, newPESEL);
        }
    }

    public void insertNewTrener(String PESEL, String sport) throws SQLException {
        try {
            PreparedStatement pstmt = SportsCenter.dBManager.getConnection().prepareStatement("INSERT INTO trener VALUES(?, ?)");
            pstmt.setString(1, PESEL);
            pstmt.setString(2, sport);
            pstmt.executeUpdate();
            System.out.println("Coach added");
            SportsCenter.dBManager.getConnection().commit();
            SportsCenter.dBManager.getConnection().setAutoCommit(true);
        } catch (SQLException e) {
            SportsCenter.dBManager.getConnection().rollback();
            SportsCenter.dBManager.getConnection().setAutoCommit(true);
            ValidateData.printSQLException(e, PESEL);
            System.out.println("Coach inserting error");
        }
    }
    
    public DBManager getdBManager() {
        return dBManager;
    }
    
}