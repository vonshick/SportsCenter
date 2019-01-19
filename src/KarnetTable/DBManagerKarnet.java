package KarnetTable;

import java.io.IOException;
import sportscenter.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBManagerKarnet {
    
    private DBManager dBManager;

    public DBManagerKarnet(DBManager dBManager) {
        this.dBManager = dBManager;
    }

    public void editKarnet(int idClientOld, int idActivityOld, String idClient, String idActivity, String price, String dateStart, String dateEnd) {
        try {
            //PreparedStatement pstmt = SportsCenter.dBManager.getConnection().prepareStatement("update karnet set KLIENT_ID_KLIENTA = ?, ZAJECIA_ID_ZAJEC = ?, cena = ?, data_rozp = ?, data_zakon = ? where KLIENT_ID_KLIENTA = ? AND ZAJECIA_ID_ZAJEC = ?");
            PreparedStatement pstmt = SportsCenter.dBManager.getConnection().prepareStatement("update karnet set cena = ?, data_rozp = ?, data_zakon = ? where KLIENT_ID_KLIENTA = ? AND ZAJECIA_ID_ZAJEC = ?");
            //pstmt.setInt(1, Integer.parseInt(idClient));
            //pstmt.setInt(2, Integer.parseInt(idActivity));
            pstmt.setFloat(1, Float.parseFloat(price));
            pstmt.setDate(2, java.sql.Date.valueOf(dateStart));
            pstmt.setDate(3, java.sql.Date.valueOf(dateEnd));
            pstmt.setInt(4, idClientOld);
            pstmt.setInt(5, idActivityOld);
            pstmt.executeQuery();
            SportsCenter.dBManager.getConnection().commit();
            System.out.println("Karnet updated!");
        } catch (SQLException e) {
            System.out.println("Karnet update error");
        }
    }
    
    public void insertNewKarnet(String idClient, String idActivity, String price, String dateStart, String dateEnd) throws IOException {
        try {
            PreparedStatement pstmt = SportsCenter.dBManager.getConnection().prepareStatement("INSERT INTO karnet VALUES(?, ?, ?, ?, ?)");
            pstmt.setInt(1, Integer.parseInt(idClient));
            pstmt.setInt(2, Integer.parseInt(idActivity));
            pstmt.setFloat(3, Float.parseFloat(price));
            pstmt.setString(4, dateStart);
            pstmt.setString(5, dateEnd);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Karnet inserting error");
        }
    }
    
    public void deleteKarnet(int idClient, int idActivity) {
        try {
            PreparedStatement pstmt = SportsCenter.dBManager.getConnection().prepareStatement("DELETE FROM karnet WHERE KLIENT_ID_KLIENTA = ? AND ZAJECIA_ID_ZAJEC = ?");
            pstmt.setInt(1, idClient);
            pstmt.setInt(2, idActivity);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBManagerKarnet.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Karnet deleting error");
        }
    }
    
    public DBManager getdBManager() {
        return dBManager;
    }
    
}