package ZajeciaTable;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import sportscenter.SQLObject;

public class Zajecia extends SQLObject {
    private int id;
    private String dayOfWeek;
    private String startHour;
    private String endHour;
    private String sport;
    private Float price;
    private String coachPESEL;
    private String coachName;
    private int buildingId;
    private String hallId;

    
    public Zajecia(){}

    public Zajecia(int id, String dayOfWeek, String startHour, String endtHour, String sport, Float price, String coachPESEL, String coachName, int buildingId, String hallId) {
        this.id = id;
        this.dayOfWeek = dayOfWeek;
        this.startHour = startHour;
        this.endHour = endtHour;
        this.sport = sport;
        this.price = price;
        this.coachName = coachName;
        this.coachPESEL = coachPESEL;
        this.buildingId = buildingId;
        this.hallId = hallId;
    }
    
    public Zajecia(ResultSet rs) throws SQLException {
        this.id = rs.getInt(1);
        this.dayOfWeek = rs.getString(2);
        this.startHour = rs.getString(3);
        this.endHour = rs.getString(4);
        this.sport = rs.getString(5);
        this.price = rs.getFloat(6);
        this.coachPESEL = rs.getString(7);
        this.coachName = rs.getString(8);
        this.buildingId = rs.getInt(9);
        this.hallId = rs.getString(10);
    }
    
    public int getId() {
        return id;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getStartHour() {
        return startHour;
    }

    public String getEndHour() {
        return endHour;
    }

    public String getCoachName() {
        return coachName;
    }

    
    
    public String getSport() {
        return sport;
    }

    public Float getPrice() {
        return price;
    }

    public String getCoachPESEL() {
        return coachPESEL;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public String getHallId() {
        return hallId;
    }
    
    
}
