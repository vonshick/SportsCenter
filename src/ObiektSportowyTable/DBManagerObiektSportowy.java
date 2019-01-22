package ObiektSportowyTable;

import sportscenter.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManagerObiektSportowy {
    
    private DBManager dBManager;

    public DBManagerObiektSportowy(DBManager dBManager) {
        this.dBManager = dBManager;
    }

    public void insertNewObiektSportowy(String name, String location, String type) throws SQLException {
        try {
            PreparedStatement pstmt = SportsCenter.dBManager.getConnection().prepareStatement("INSERT INTO obiekt_sportowy VALUES(seq_id_obiektu.nextval, ?, ?, ?)");
            pstmt.setString(1, location);
            pstmt.setString(2, name);
            pstmt.setString(3, type);
            pstmt.executeUpdate();
            SportsCenter.dBManager.getConnection().commit();
            System.out.println("Sports facility added!");
        } catch (SQLException e) {
            ValidateData.printSQLException(e, "Nazwa");
            System.out.println("Sports facility inserting error");
        }
    }
    
    public void deleteObiektSportowy(int id) throws SQLException{
        Statement stmt = SportsCenter.dBManager.getConnection().createStatement();
        String dropArcWyposazenie = "ALTER TABLE wyposazenie DROP CONSTRAINT arc_2";
        String dropArcZajecia = "ALTER TABLE zajecia DROP CONSTRAINT arc_1";
        String addArcWyposazenie = "ALTER TABLE wyposazenie\n" +
"    ADD CONSTRAINT arc_2 CHECK (\n" +
"        (\n" +
"            ( obiekt_sportowy_id_obiektu IS NOT NULL )\n" +
"            AND ( sala_obiekt_sportowy_id_ob IS NULL )\n" +
"            AND ( sala_nr_sali IS NULL )\n" +
"        )\n" +
"        OR (\n" +
"            ( sala_obiekt_sportowy_id_ob IS NOT NULL )\n" +
"            AND ( sala_nr_sali IS NOT NULL )\n" +
"            AND ( obiekt_sportowy_id_obiektu IS NULL )\n" +
"        )\n" +
"    )";
        String addArcZajecia="ALTER TABLE zajecia\n" +
"    ADD CONSTRAINT arc_1 CHECK (\n" +
"        (\n" +
"            ( obiekt_sportowy_id_obiektu IS NOT NULL )\n" +
"            AND ( sala_obiekt_sportowy_id_ob IS NULL )\n" +
"            AND ( sala_nr_sali IS NULL )\n" +
"        )\n" +
"        OR (\n" +
"            ( sala_obiekt_sportowy_id_ob IS NOT NULL )\n" +
"            AND ( sala_nr_sali IS NOT NULL )\n" +
"            AND ( obiekt_sportowy_id_obiektu IS NULL )\n" +
"        )\n" +
"    )";


        try {
            SportsCenter.dBManager.getConnection().setAutoCommit(false);
            
            stmt.executeQuery(dropArcWyposazenie);
            stmt.executeQuery(dropArcZajecia);
            
            PreparedStatement pstmt = SportsCenter.dBManager.getConnection().prepareStatement("DELETE FROM obiekt_sportowy WHERE id_obiektu = ?");
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            
            pstmt = SportsCenter.dBManager.getConnection().prepareStatement("DELETE FROM zajecia WHERE obiekt_sportowy_id_obiektu = ? OR sala_obiekt_sportowy_id_ob = ?");
            pstmt.setInt(1, id);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();

            pstmt = SportsCenter.dBManager.getConnection().prepareStatement("DELETE FROM wyposazenie WHERE obiekt_sportowy_id_obiektu = ? OR sala_obiekt_sportowy_id_ob = ?");
            pstmt.setInt(1, id);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            
            stmt.executeQuery(addArcWyposazenie);
            stmt.executeQuery(addArcZajecia);

            SportsCenter.dBManager.getConnection().commit();

            SportsCenter.dBManager.getConnection().setAutoCommit(true);

            
        } catch (SQLException ex) {
            SportsCenter.dBManager.getConnection().rollback();
            ex.printStackTrace();
            stmt.executeQuery(addArcWyposazenie);
            stmt.executeQuery(addArcZajecia);
            ValidateData.printSQLException(ex, "");
            System.out.println("Obiekt_sportowy deleting error");
        }
    }
    
    public void editObiektSportowy(String buildingId, String location, String name, String type) throws SQLException {
        try {
            PreparedStatement pstmt = SportsCenter.dBManager.getConnection().prepareStatement("UPDATE obiekt_sportowy SET nazwa = ?, lokalizacja = ? , typ_obiektu = ? WHERE id_obiektu = ?");
            pstmt.setString(1, name);
            pstmt.setString(2, location);
            pstmt.setString(3, type);
            pstmt.setString(4, buildingId);

            pstmt.executeUpdate();
            SportsCenter.dBManager.getConnection().commit();
            System.out.println("Sports facility added!");
        } catch (SQLException e) {
            ValidateData.printSQLException(e, "Nazwa");
            System.out.println("Sports facility inserting error");
        }
    }
    public DBManager getdBManager() {
        return dBManager;
    }
    
}