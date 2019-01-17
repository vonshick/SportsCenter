package SalaTable;

import java.sql.ResultSet;
import java.sql.SQLException;
import sportscenter.SQLObject;

public class Sala extends SQLObject {
    private String hallId;
    private int buildingId ;

    public Sala(){}
    
    public Sala(String hallId, int buildingId) {
        this.hallId = hallId;
        this.buildingId = buildingId;
    }

    
    public Sala(ResultSet rs) throws SQLException {
        this.hallId = rs.getString(1);
        this.buildingId = rs.getInt(2);
    }

    public String getHallId() {
        return hallId;
    }

    public void setHallId(String hallId) {
        this.hallId = hallId;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

 


    
    
}
