package ObiektSportowyTable;

import sportscenter.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBManagerObiektSportowy {
    
    private DBManager dBManager;

    public DBManagerObiektSportowy(DBManager dBManager) {
        this.dBManager = dBManager;
    }

    public void insertNewObiektSportowy(String name, String location, String type) throws SQLException {
        try {
            PreparedStatement pstmt = SportsCenter.dBManager.getConnection().prepareStatement("INSERT INTO obiekt_sportowy VALUES(seq_id_obiektu.nextval, ?, ?, ?)");
            pstmt.setString(1, name);
            pstmt.setString(2, location);
            pstmt.setString(3, type);
            pstmt.executeUpdate();
            System.out.println("Sports facility added!");
        } catch (SQLException e) {
            System.out.println("Sports facility inserting error");
        }
    }

    public DBManager getdBManager() {
        return dBManager;
    }
    
}