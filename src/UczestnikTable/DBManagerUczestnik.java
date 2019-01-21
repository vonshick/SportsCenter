package UczestnikTable;

import java.io.IOException;
import sportscenter.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBManagerUczestnik {
    
    private DBManager dbManager;

    public DBManagerUczestnik(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public void editUczestnik(String OldPESEL, String newPESEL, String surname, String name, String competition, int status) {
        try {
            PreparedStatement pstmt ;
            pstmt = SportsCenter.dBManager.getConnection().prepareStatement("update uczestnik set PESEL = ?, nazwisko = ?, imie = ?, oplacony = ? where PESEL = ?");

            pstmt.setString(1, newPESEL);
            pstmt.setString(2, surname);
            pstmt.setString(3, name);
            pstmt.setInt(4, status);
            pstmt.setString(5, OldPESEL);
            pstmt.executeQuery();
            dbManager.getConnection().commit();
            System.out.println("Competitor update success");

        } catch (SQLException e) {
            System.out.println("Competitor update error");
        }
    }

    public ArrayList<String> generateCompetitionsList(){
        Statement stmt;
        ResultSet rs;
        ArrayList<String> competitions = new ArrayList<>();
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
   
    
    public void insertNewUczestnik(String PESEL, String surname, String name, String competition, int status) throws IOException {
        try{
            PreparedStatement pstmt = SportsCenter.dBManager.getConnection().prepareStatement("INSERT INTO uczestnik VALUES(?, ?, ?, ?)");
            pstmt.setString(1, PESEL);
            pstmt.setString(2, surname);
            pstmt.setString(3, name);
            pstmt.setInt(4, status);
            pstmt.executeUpdate();
            System.out.println("Competitor added!");
        }catch(SQLException e ){
            System.out.println("Competitor inserting error");
        }
    }
    
    public DBManager getdBManager() {
        return dbManager;
    }
    
}