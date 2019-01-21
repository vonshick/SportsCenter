package KlientTable;

import java.io.IOException;
import sportscenter.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBManagerKlient {
    
    private DBManager dBManager;

    public DBManagerKlient(DBManager dBManager) {
        this.dBManager = dBManager;
    }

    public void deleteKlient(int ID) throws SQLException{
        try {
            PreparedStatement pstmt = SportsCenter.dBManager.getConnection().prepareStatement("DELETE FROM klient where id_klienta = ?");
            pstmt.setInt(1, ID);
            pstmt.executeQuery();
        } catch (SQLException e) {
            ValidateData.printSQLException(e, "");
            System.out.println("Employee update error");
        }
    }
    
    public void editKlient(int ID, String name, String surname) {
        try {
            PreparedStatement pstmt = SportsCenter.dBManager.getConnection().prepareStatement("update klient set nazwisko = ?, imie = ? where id_klienta = ?");
            pstmt.setString(1, surname);
            pstmt.setString(2, name);
            pstmt.setInt(3, ID);
            pstmt.executeQuery();
            SportsCenter.dBManager.getConnection().commit();
            System.out.println("Employee updated!");
        } catch (SQLException e) {
            ValidateData.printSQLException(e, "");
            System.out.println("Employee update error");
        }
    }
    
    public void insertNewKlient(String name, String surname) throws IOException {
        try {
            PreparedStatement pstmt = SportsCenter.dBManager.getConnection().prepareStatement("INSERT INTO klient VALUES(seq_id_klienta.nextval, ?, ?)");
            pstmt.setString(1, surname);
            pstmt.setString(2, name);
            pstmt.executeUpdate();
            System.out.println("Client added!");
        } catch (SQLException e) {
            ValidateData.printSQLException(e, "");
            System.out.println("Client inserting error");
        }
    }
    
    public DBManager getdBManager() {
        return dBManager;
    }
    
}