package TrenerTable;

import sportscenter.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


public class DBManagerTrener {
    
    private DBManager dBManager;

    public DBManagerTrener(DBManager dBManager) {
        this.dBManager = dBManager;
    }
    
    public void editTrener(String oldPESEL, String newPESEL, String disciplin) throws SQLException {
        Statement stmt = SportsCenter.dBManager.getConnection().createStatement();
        String dropConstraintTrener = "ALTER TABLE trener DROP CONSTRAINT trener_pracownik_fk";
        String dropConstraintZajecia = "ALTER TABLE zajecia DROP CONSTRAINT zajecia_trener_fk";
        String addConstraintTrener = "ALTER TABLE trener\n" +
                    "    ADD CONSTRAINT trener_pracownik_fk FOREIGN KEY ( pesel )\n" +
                    "        REFERENCES pracownik ( pesel )";
        String addConstraintZajecia ="ALTER TABLE zajecia\n" +
                    "    ADD CONSTRAINT zajecia_trener_fk FOREIGN KEY ( trener_pesel )\n" +
                    "        REFERENCES trener ( pesel )";
        try {
            PreparedStatement pstmt = SportsCenter.dBManager.getConnection().prepareStatement("update trener set pesel = ?, dyscyplina = ? where pesel = ?");
            SportsCenter.dBManager.getConnection().setAutoCommit(false);
            stmt.executeQuery(dropConstraintTrener);
            stmt.executeQuery(dropConstraintZajecia);
            pstmt.setString(1, newPESEL);
            pstmt.setString(2, disciplin);
            pstmt.setString(3, oldPESEL);
            pstmt.executeQuery();
            
            pstmt = SportsCenter.dBManager.getConnection().prepareStatement("update pracownik set pesel = ? where pesel = ?");
            pstmt.setString(1, newPESEL);
            pstmt.setString(2, oldPESEL);
            pstmt.executeQuery();
            
            pstmt = SportsCenter.dBManager.getConnection().prepareStatement("update zajecia set trener_pesel = ? where trener_pesel = ?");
            pstmt.setString(1, newPESEL);
            pstmt.setString(2, oldPESEL);
            pstmt.executeQuery();
            
            stmt.executeQuery(addConstraintTrener);
            stmt.executeQuery(addConstraintZajecia);            
            SportsCenter.dBManager.getConnection().commit();
            SportsCenter.dBManager.getConnection().setAutoCommit(true);

            System.out.println("Trener updated!");
        } catch (SQLException e) {
            ValidateData.printSQLException(e, "PESEL");
            SportsCenter.dBManager.getConnection().rollback();
            stmt.executeQuery(addConstraintTrener);
            stmt.executeQuery(addConstraintZajecia);
            SportsCenter.dBManager.getConnection().setAutoCommit(true);
            System.out.println("Trener update error");
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
            ValidateData.printSQLException(e, "PESEL");
            SportsCenter.dBManager.getConnection().rollback();
            SportsCenter.dBManager.getConnection().setAutoCommit(true);
            System.out.println("Coach inserting error");
        }
    }
    
    public DBManager getdBManager() {
        return dBManager;
    }
    
}