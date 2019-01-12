package WyposazenieTable;

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

public class DBManagerWyposazenie {
    
    private DBManager dBManager;

    public DBManagerWyposazenie(DBManager dBManager) {
        this.dBManager = dBManager;
    }
//
//    public void editWyposażenie(String oldPESEL, String name, String surname, String newPESEL, String profession, String salary) {
//        try {
//            PreparedStatement pstmt = SportsCenter.dBManager.getConnection().prepareStatement("update pracownik set pesel = ?, nazwisko = ?, imie = ?, funkcja = ?, placa = ? where pesel = ?");
//            pstmt.setString(1, newPESEL);
//            pstmt.setString(2, surname);
//            pstmt.setString(3, name);
//            pstmt.setString(4, profession);
//            pstmt.setFloat(5, Float.parseFloat(salary));
//            pstmt.setString(6, oldPESEL);
//            pstmt.executeQuery();
//            SportsCenter.dBManager.getConnection().commit();
//            System.out.println("Employee updated!");
//        } catch (SQLException e) {
//            System.out.println("Employee update error");
//        }
//    }
    
//    CREATE TABLE wyposazenie (
//    id_wyposazenia               INTEGER NOT NULL,
//    nazwa                        VARCHAR2(50) NOT NULL,
//    dyscyplina                   VARCHAR2(50),
//    ilosc                        INTEGER,
//    obiekt_sportowy_id_obiektu   INTEGER,
//    sala_obiekt_sportowy_id_ob   INTEGER,
//    sala_nr_sali                 VARCHAR2(50)
//);

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
    
    public void insertNewWyposazenie(String name, String sport, String count, int building, String hall) throws IOException {
        try {
            PreparedStatement pstmt;
            if (hall.equals("")){
                pstmt = SportsCenter.dBManager.getConnection().prepareStatement("INSERT INTO wyposazenie"
                        + "(id_wyposazenia, nazwa, dyscyplina, ilosc, obiekt_sportowy_id_obiektu)"
                        + " VALUES(seq_id_wyposazenia.nextval, ?, ?, ?, ?)");
            } else {
                pstmt = SportsCenter.dBManager.getConnection().prepareStatement("INSERT INTO wyposazenie"
                        + "(id_wyposazenia, nazwa, dyscyplina, ilosc, sala_obiekt_sportowy_id_ob, sala_nr_sali)"
                        + " VALUES(seq_id_wyposazenia.nextval, ?, ?, ?, ?, ?)");
            }
            pstmt.setString(1, name);
            pstmt.setString(2, sport);
            pstmt.setInt(3, Integer.parseInt(count));
            pstmt.setInt(4, building);
            if (!hall.equals("")){
                pstmt.setString(5, hall);
            }
            pstmt.executeUpdate();
            System.out.println("Equipment added!");
        } catch (SQLException e) {
            System.out.println("Equipment inserting error");
            e.printStackTrace();
        }
    }
    
    
    public DBManager getdBManager() {
        return dBManager;
    }
    
}