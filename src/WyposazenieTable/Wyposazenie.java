package WyposazenieTable;

import PracownikTable.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import sportscenter.SQLObject;

public class Wyposazenie extends SQLObject {
    private String name;
    private String sport;
    private int count;
    private int buildingId;
    private String hallId;

    public Wyposazenie(){}
    
    public Wyposazenie(String name, String sport, int count, int buildingId, String hallId) {
        this.name = name;
        this.sport = sport;
        this.count = count;
        this.buildingId = buildingId;
        this.hallId = hallId;
    }
    
    public Wyposazenie(ResultSet rs) throws SQLException {
        this.name = rs.getString(1);
        this.sport = rs.getString(2);
        this.count = rs.getInt(3);
        this.buildingId = rs.getInt(4);
        try{
            this.hallId = rs.getString(5);
        } catch(Exception e){
            System.out.println("This ResultSet didn't contain hallId");
        }        
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    public String getHallId() {
        return hallId;
    }

    public void setHallId(String hallId) {
        this.hallId = hallId;
    }


    
    
}
