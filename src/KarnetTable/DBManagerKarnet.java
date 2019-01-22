package KarnetTable;

import java.io.IOException;
import java.sql.CallableStatement;
import sportscenter.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class DBManagerKarnet {
    
    private DBManager dBManager;

    public DBManagerKarnet(DBManager dBManager) {
        this.dBManager = dBManager;
    }

    public void editKarnet(int idClientOld, int idActivityOld, String idClient, String idActivity, Float price, String dateStart, String dateEnd) {
        try {
            PreparedStatement pstmt = SportsCenter.dBManager.getConnection().prepareStatement("update karnet set cena = ?, data_rozp = ?, data_zakon = ? where KLIENT_ID_KLIENTA = ? AND ZAJECIA_ID_ZAJEC = ?");
            pstmt.setFloat(1, price);
            pstmt.setDate(2, java.sql.Date.valueOf(dateStart));
            pstmt.setDate(3, java.sql.Date.valueOf(dateEnd));
            pstmt.setInt(4, idClientOld);
            pstmt.setInt(5, idActivityOld);
            pstmt.executeQuery();
            SportsCenter.dBManager.getConnection().commit();
            System.out.println("Karnet updated!");
        } catch (SQLException e) {
            ValidateData.printSQLException(e, "id_klienta lub id_aktywnosci");
            System.out.println("Karnet update error");
        }
    }
    
    public void removeOldPasses() throws SQLException{
        try{
            CallableStatement cstmt = SportsCenter.dBManager.getConnection().prepareCall("{call usun_stare_karnety}");
            cstmt.executeQuery();
            SportsCenter.dBManager.getConnection().commit();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, 
                "Usunięto przeterminowane karnety", ButtonType.OK);
            alert.showAndWait();
        } catch (SQLException ex){
            SportsCenter.dBManager.getConnection().rollback();
            ValidateData.printSQLException(ex, "");
        }
    }
    
    public void insertNewKarnet(String idClient, String idActivity, Float price, String dateStart, String dateEnd) throws IOException {
        try {
            PreparedStatement pstmt = SportsCenter.dBManager.getConnection().prepareStatement("INSERT INTO karnet "
                    + "VALUES(?, ?, ?, TO_DATE('"+dateStart+"','YYYY-MM-DD'), TO_DATE('"+dateEnd+"','YYYY-MM-DD'))");
            pstmt.setInt(1, Integer.parseInt(idClient));
            pstmt.setInt(2, Integer.parseInt(idActivity));
            pstmt.setFloat(3, price);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            ValidateData.printSQLException(e, "id_klienta lub id_aktywnosci");
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
            System.out.println("Karnet deleting error");
        }
    }
    
    public DBManager getdBManager() {
        return dBManager;
    }
    
}