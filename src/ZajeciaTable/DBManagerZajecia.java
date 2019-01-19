package ZajeciaTable;

import ZajeciaTable.*;
import java.io.IOException;
import sportscenter.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;

public class DBManagerZajecia {
    
    private DBManager dBManager;

    public DBManagerZajecia(DBManager dBManager) {
        this.dBManager = dBManager;
    }
    
    //    CREATE TABLE zajecia (
//    id_zajec                     INTEGER NOT NULL,
//    dzien_tygodnia               VARCHAR2(20) NOT NULL,
//    godzina_rozp                 DATE NOT NULL,
//    godzina_zakon                DATE NOT NULL,
//    dyscyplina                   VARCHAR2(50) NOT NULL,
//    cena                         NUMBER(6,2) NOT NULL,
//    trener_pesel                 VARCHAR2(11),
//    obiekt_sportowy_id_obiektu   INTEGER,
//    sala_obiekt_sportowy_id_ob   INTEGER,
//    sala_nr_sali                 VARCHAR2(50)
//);

    public void insertNewZajecia(String dayOfWeek, String startHour, 
                            String startMinute, 
                            String endHour, String endMinute, String sport, 
                            Float price, String coachPESEL,
                            int buildingId, String hallId) {
        try {
            PreparedStatement pstmt ;
            pstmt = SportsCenter.dBManager.getConnection().prepareStatement("INSERT INTO zajecia VALUES("
                    + "seq_id_zajec.nextval, ? , "
                    + "to_timestamp('"+startHour+":"+startMinute+":00','HH24:MI:SS') , "
                    + "to_timestamp('"+endHour+":"+endMinute+":00','HH24:MI:SS'), "
                    + "?, ?, ?, ?, ?, ?)");            
            pstmt.setString(1, dayOfWeek);
            pstmt.setString(2, sport);
            pstmt.setFloat(3, price);
            if(hallId.equals("")){
                pstmt.setString(4, coachPESEL);
            }else{
                pstmt.setNull(4, java.sql.Types.VARCHAR);
            }
            if(hallId.equals("")){
                pstmt.setInt(5, buildingId);
                pstmt.setNull(6, java.sql.Types.INTEGER);
                pstmt.setNull(7, java.sql.Types.VARCHAR);
            }else{
                pstmt.setNull(5, java.sql.Types.INTEGER);
                pstmt.setInt(6, buildingId);
                pstmt.setString(7, hallId);
            }
            pstmt.executeQuery();
            SportsCenter.dBManager.getConnection().commit();
            System.out.println("Classes inserted!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Classes inserting error");
        }
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
    
    public HashMap<String,String> generateCoachessMap(){
        Statement stmt;
        ResultSet rs;
        HashMap<String, String> coaches = new HashMap<String, String>();
        try {
            stmt = SportsCenter.connection.getConn().createStatement();
            rs  = stmt.executeQuery("SELECT nazwisko, trener_pesel FROM v_zajecia");
            while (rs.next()) {
                String coachName = rs.getString(1);
                coaches.put(coachName, rs.getString(2));
            }
        } catch (SQLException ex) {
        System.out.println("Error while filling coaches list" + ex.toString());
        }  
        return coaches;   
    }
    
    public ArrayList<String> generateHallsList(String buildingName){
        Statement stmt;
        ResultSet rs;
        ArrayList<String> hallIds = new ArrayList<String>();
        try {
            stmt = SportsCenter.connection.getConn().createStatement();
            rs = stmt.executeQuery("SELECT nr_sali FROM sala "
                    + "WHERE obiekt_sportowy_id_obiektu = "
                    + "(SELECT id_obiektu FROM obiekt_sportowy WHERE nazwa = '"+buildingName+"')");
            ArrayList<String> choices = new ArrayList<String>();
            while (rs.next()) {
                 hallIds.add(rs.getString(1));
            }
        } catch (SQLException ex) {
            System.out.println("Bład wykonania polecenia" + ex.toString());
        }
        return hallIds;

    }
    
    public void editZajecia(int id, String dayOfWeek, String startHour, 
                            String startMinute, 
                            String endHour, String endMinute, String sport, 
                            Float price, String coachPESEL,
                            int buildingId, String hallId) throws IOException, SQLException {
        try {
            PreparedStatement pstmt;
            pstmt = SportsCenter.dBManager.getConnection().prepareStatement("update zajecia set "
                    + "dzien_tygodnia = ?, godzin_rozp = to_timestamp('"+startHour+":"+startMinute+":00','HH24:MI:SS'), "
                    + "godzina_zakon = to_timestamp('"+endHour+":"+endMinute+":00','HH24:MI:SS'), "
                    + "dyscyplina = ? "
                    + "cena = ?, trener_pesel = ?, "
                    + "obiekt_sportowy_id_obiektu = ?, "
                    + "sala_obiekt_sportowy_id_ob = ?, sala_nr_sali = ? "
                    + "where id_zajec = ?");
            pstmt.setString(1, dayOfWeek);
            pstmt.setString(2, sport);
            pstmt.setFloat(3, price);
            if(hallId.equals("")){
                pstmt.setString(4, coachPESEL);
            }else{
                pstmt.setNull(4, java.sql.Types.VARCHAR);
            }
            if(hallId.equals("")){
                pstmt.setInt(5, buildingId);
                pstmt.setNull(6, java.sql.Types.INTEGER);
                pstmt.setNull(7, java.sql.Types.VARCHAR);
            }else{
                pstmt.setNull(5, java.sql.Types.INTEGER);
                pstmt.setInt(6, buildingId);
                pstmt.setString(7, hallId);
            }
            pstmt.setInt(8, id);
            System.out.println("Classes edited!");
        } catch (SQLException e) {
            System.out.println("Classes editing error");
            e.printStackTrace();
        }
    }
    
    
    public DBManager getdBManager() {
        return dBManager;
    }
    
}