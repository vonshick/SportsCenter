package SalaTable;

import java.io.IOException;
import sportscenter.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class DBManagerSala {
    
    private DBManager dBManager;

    public DBManagerSala(DBManager dBManager) {
        this.dBManager = dBManager;
    }

    public void editSala(String oldHallId, int oldBuildingId, String newHallId, int newBuildingId) throws SQLException {
        Statement stmt = SportsCenter.dBManager.getConnection().createStatement();
        String dropConstraintWyposazenie = "ALTER TABLE wyposazenie DROP CONSTRAINT wyposazenie_sala_fk";
        String dropConstraintZajecia = "ALTER TABLE zajecia DROP CONSTRAINT zajecia_sala_fk";
        String addConstraintWyposazenie = "ALTER TABLE wyposazenie\n" +
            "    ADD CONSTRAINT wyposazenie_sala_fk FOREIGN KEY ( sala_obiekt_sportowy_id_ob,\n" +
            "    sala_nr_sali )\n" +
            "        REFERENCES sala ( obiekt_sportowy_id_obiektu,\n" +
            "        nr_sali )";
        String addConstraintZajecia = "ALTER TABLE zajecia\n" +
            "    ADD CONSTRAINT zajecia_sala_fk FOREIGN KEY ( sala_obiekt_sportowy_id_ob,\n" +
            "    sala_nr_sali )\n" +
            "        REFERENCES sala ( obiekt_sportowy_id_obiektu,\n" +
            "        nr_sali )";

        try {
            //drop foreign key constraints on other tables
            SportsCenter.dBManager.getConnection().setAutoCommit(false);

            stmt.execute(dropConstraintWyposazenie);
            stmt.execute(dropConstraintZajecia);

            PreparedStatement pstmt ;
            pstmt = SportsCenter.dBManager.getConnection().prepareStatement("update sala set nr_sali = ?, obiekt_sportowy_id_obiektu = ? where nr_sali = ? and obiekt_sportowy_id_obiektu = ?");
            executePreparedStatement(pstmt, newHallId, newBuildingId, oldHallId, oldBuildingId);

            pstmt = SportsCenter.dBManager.getConnection().prepareStatement("update zajecia set sala_nr_sali = ?, sala_obiekt_sportowy_id_ob = ? where sala_nr_sali = ? and sala_obiekt_sportowy_id_ob = ?");
            executePreparedStatement(pstmt, newHallId, newBuildingId, oldHallId, oldBuildingId);

            pstmt = SportsCenter.dBManager.getConnection().prepareStatement("update wyposazenie set sala_nr_sali = ?, sala_obiekt_sportowy_id_ob = ? where sala_nr_sali = ? and sala_obiekt_sportowy_id_ob = ?");
            executePreparedStatement(pstmt, newHallId, newBuildingId, oldHallId, oldBuildingId);
            
            stmt.execute(addConstraintZajecia);
            stmt.execute(addConstraintWyposazenie);
            
            SportsCenter.dBManager.getConnection().commit();
            SportsCenter.dBManager.getConnection().setAutoCommit(true);

            System.out.println("Hall update success");

        } catch (SQLException e) {
            ValidateData.printSQLException(e, "Nazwa");
            System.out.println("Hall update error");
            SportsCenter.dBManager.getConnection().rollback();
            stmt.execute(addConstraintZajecia);
            stmt.execute(addConstraintWyposazenie);
            SportsCenter.dBManager.getConnection().commit();
            SportsCenter.dBManager.getConnection().setAutoCommit(true);
        }
    }
    
    private void executePreparedStatement(PreparedStatement pstmt, String newHallId, int newBuildingId, String oldHallId, int oldBuildingId) throws SQLException{
            pstmt.setString(1, newHallId);
            pstmt.setInt(2, newBuildingId);
            pstmt.setString(3, oldHallId);
            pstmt.setInt(4, oldBuildingId);
            pstmt.executeQuery();
    }

    public HashMap<String,Integer> generateBuildingsMap(){
        Statement stmt;
        ResultSet rs;
        HashMap<String, Integer> buildings = new HashMap<String, Integer>();
        try {
            stmt = SportsCenter.connection.getConn().createStatement();
            rs  = stmt.executeQuery("SELECT nazwa, id_obiektu FROM obiekt_sportowy");
            while (rs.next()) {
                String buildingName = rs.getString(1);
                buildings.put(buildingName, rs.getInt(2));
            }
        } catch (SQLException ex) {
        System.out.println("Error while filling buildings list" + ex.toString());
        }  
        return buildings;
    }
   
    
    public void insertNewSala(String hallId, int buildingId) throws IOException {
        try{
            PreparedStatement pstmt = SportsCenter.dBManager.getConnection().prepareStatement("INSERT INTO sala VALUES(?, ?)");
            pstmt.setString(1, hallId);
            pstmt.setInt(2, buildingId);
            pstmt.executeUpdate();
            System.out.println("Hall added!");
        }catch(SQLException e ){
            ValidateData.printSQLException(e, "Nazwa");
            System.out.println("Hall inserting error");
        }
    }
    
    public void deleteSala(int buildingId, String hallId) throws SQLException{
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
            
            PreparedStatement pstmt = SportsCenter.dBManager.getConnection().prepareStatement("DELETE FROM sala WHERE obiekt_sportowy_id_obiektu = ? AND nr_sali = ?");
            pstmt.setInt(1, buildingId);
            pstmt.setString(2, hallId);
            pstmt.executeUpdate();
            
            pstmt = SportsCenter.dBManager.getConnection().prepareStatement("DELETE FROM zajecia WHERE sala_obiekt_sportowy_id_ob = ? AND sala_nr_sali = ?");
            pstmt.setInt(1, buildingId);
            pstmt.setString(2, hallId);
            pstmt.executeUpdate();

            pstmt = SportsCenter.dBManager.getConnection().prepareStatement("DELETE FROM wyposazenie WHERE sala_obiekt_sportowy_id_ob = ? AND sala_nr_sali = ?");
            pstmt.setInt(1, buildingId);
            pstmt.setString(2, hallId);
            pstmt.executeUpdate();
            
            stmt.executeQuery(addArcWyposazenie);
            stmt.executeQuery(addArcZajecia);

            SportsCenter.dBManager.getConnection().commit();

            SportsCenter.dBManager.getConnection().setAutoCommit(true);

            
        } catch (SQLException ex) {
            SportsCenter.dBManager.getConnection().rollback();
            ex.printStackTrace();
            SportsCenter.dBManager.getConnection().setAutoCommit(true);
            stmt.executeQuery(addArcWyposazenie);
            stmt.executeQuery(addArcZajecia);
            ValidateData.printSQLException(ex, "");
            System.out.println("Sala deleting error");
        }
    }
    
    
    public DBManager getdBManager() {
        return dBManager;
    }
    
}