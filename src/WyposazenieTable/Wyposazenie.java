package WyposazenieTable;

import java.sql.ResultSet;
import java.sql.SQLException;
import sportscenter.SQLObject;

public class Wyposazenie extends SQLObject {
    private int id;
    private String name;
    private String sport;
    private int count;
    private int buildingId;
    private String hallId;

    public Wyposazenie(){}
    
    public Wyposazenie(int id, String name, String sport, int count, int buildingId, String hallId) {
        this.id = id;
        this.name = name;
        this.sport = sport;
        this.count = count;
        this.buildingId = buildingId;
        this.hallId = hallId;
    }
    
    public Wyposazenie(ResultSet rs) throws SQLException {
        this.id = rs.getInt(1);
        this.name = rs.getString(2);
        this.sport = rs.getString(3);
        this.count = rs.getInt(4);
        if(rs.getString(7) == null){
            this.buildingId = rs.getInt(5);
            System.out.println(buildingId);
        }else{
            this.buildingId = rs.getInt(6);
            System.out.println(buildingId);
        }
        this.hallId = rs.getString(7);
        System.out.println("idWyposazenia: "+id);
        System.out.println("name "+name);
        System.out.println("sport "+sport);
        System.out.println("count "+count);
        System.out.println("buildingId "+buildingId);
        System.out.println("hallId: "+hallId);
    }
    
    @Override
    public String toString() {
        return this.id + "," + this.name + "," + this.sport + "," + this.count + "," + this.buildingId + "," + this.hallId;
    }

    public int getId() {
        return id;
    }

    public void setId(int idWyposazenia) {
        this.id = idWyposazenia;
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
