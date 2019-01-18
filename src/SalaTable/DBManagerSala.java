package SalaTable;

import java.io.IOException;
import sportscenter.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class DBManagerSala {
    
    private DBManager dBManager;

    public DBManagerSala(DBManager dBManager) {
        this.dBManager = dBManager;
    }

    public void editSala(String oldHallId, int oldBuildingId, String newHallId, int newBuildingId) {
        //TO DO
//        try {
//            PreparedStatement pstmt ;
//            pstmt = SportsCenter.dBManager.getConnection().prepareStatement("update sala set nr_sali = ?, obiekt_sportowy_id_obiektu = ? where nr_sali = ?, obiekt_sportowy_id_obiektu = ?");
//
//            pstmt.setString(1, newHallId);
//            pstmt.setInt(2, newBuildingId);
//            pstmt.setString(3, oldHallId);
//            pstmt.setInt(4, oldBuildingId);
//            pstmt.executeQuery();
//                SportsCenter.dBManager.getConnection().commit();
//            System.out.println("Hall update success");
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.println("Hall update error");
//        }
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
            System.out.println("Hall inserting error");
        }
    }
    
    
    public DBManager getdBManager() {
        return dBManager;
    }
    
}