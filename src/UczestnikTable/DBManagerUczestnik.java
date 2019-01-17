package UczestnikTable;

import UczestnikTable.*;
import java.io.IOException;
import sportscenter.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class DBManagerUczestnik {
    
    private DBManager dBManager;

    public DBManagerUczestnik(DBManager dBManager) {
        this.dBManager = dBManager;
    }

    public void editUczestnik(String PESEL, String surname, String name, String competition, char status) {
        //TO DO
//        try {
//            PreparedStatement pstmt ;
//            pstmt = SportsCenter.dBManager.getConnection().prepareStatement("update uczestnik set nr_sali = ?, obiekt_sportowy_id_obiektu = ? where nr_sali = ?, obiekt_sportowy_id_obiektu = ?");
//
//            pstmt.setString(1, newHallId);
//            pstmt.setInt(2, newBuildingId);
//            pstmt.setString(3, oldHallId);
//            pstmt.setInt(4, oldBuildingId);
//            pstmt.execute();
//            System.out.println("Hall update success");
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.println("Hall update error");
//        }
    }

    public ArrayList<String> generateCompetitionsList(){
        Statement stmt;
        ResultSet rs;
        ArrayList<String> competitions = new ArrayList<String>();
        try {
            stmt = SportsCenter.connection.getConn().createStatement();
            rs  = stmt.executeQuery("SELECT nazwa FROM zawody");
            while (rs.next()) {
                String competitionName = rs.getString(1);
                competitions.add(competitionName);
            }
        } catch (SQLException ex) {
        System.out.println("Error while filling competitions list" + ex.toString());
        }  
        return competitions;
    }
   
    
    public void insertNewUczestnik(String PESEL, String surname, String name, String competition, char status) throws IOException {
        try{
            PreparedStatement pstmt = SportsCenter.dBManager.getConnection().prepareStatement("INSERT INTO uczestnik VALUES(?, ?, ?, ?)");
            pstmt.setString(1, PESEL);
            pstmt.setString(2, surname);
            pstmt.setString(3, name);
            pstmt.setString(4, String.valueOf(status));
            pstmt.executeUpdate();
            System.out.println("Competitor added!");
        }catch(SQLException e ){
            System.out.println("Competitor inserting error");
        }
    }
    
    
    public DBManager getdBManager() {
        return dBManager;
    }
    
}