package ZawodyTable;

import sportscenter.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBManagerZawody {
    
    private DBManager dBManager;

    public DBManagerZawody(DBManager dBManager) {
        this.dBManager = dBManager;
    }
    
    public void editZawody(String oldName, String name, java.sql.Date date, String disciplin, Float price, Integer IDSportObject)  {
        try {
            PreparedStatement pstmt = SportsCenter.dBManager.getConnection().prepareStatement("update zawody set NAZWA = ?, DATA = ?, DYSCYPLINA = ?, OPLATA_STARTOWA = ?, OBIEKT_SPORTOWY_ID_OBIEKTU = ? where NAZWA = ?");
            pstmt.setString(1, name);
            pstmt.setDate(2, date);
            pstmt.setString(3, disciplin);
            pstmt.setFloat(4, price);
            pstmt.setInt(5, IDSportObject);
            pstmt.setString(6, oldName);
            pstmt.executeQuery();
            SportsCenter.dBManager.getConnection().commit();
            System.out.println("Competition updated!");
        } catch (SQLException e) {
            ValidateData.printSQLException(e, "Nazwa");
            System.out.println("Competition update error");
        }
    }
    
    public void insertNewZawody(String name, java.sql.Date date, String disciplin, Float price, Integer IDSportObject) {
        try {
            PreparedStatement pstmt = SportsCenter.dBManager.getConnection().prepareStatement("INSERT INTO zawody VALUES(?, ?, ?, ?, ?)");
            pstmt.setString(1, name);
            pstmt.setDate(2, date);
            pstmt.setString(3, disciplin);
            pstmt.setFloat(4, price);
            pstmt.setInt(5, IDSportObject);
            pstmt.executeUpdate();
            System.out.println("Zawody added!");
        } catch (SQLException e) {
            ValidateData.printSQLException(e, "Nazwa");
            System.out.println("Zawody inserting error");
        }
    }
    
    public DBManager getdBManager() {
        return dBManager;
    }
    
}