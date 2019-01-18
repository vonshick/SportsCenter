package ZajeciaTable;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import sportscenter.SQLObject;

public class Zajecia extends SQLObject {
    private int id;
    private String dayOfWeek;
    private Date startHour;
    private Date endtHour;
    private String sport;
    private Float price;
    private String coachPESEL;
    private int buildingId;
    private String hallId;

    
    public Zajecia(){}

    public Zajecia(int id, String dayOfWeek, Date startHour, Date endtHour, String sport, Float price, String coachPESEL, int buildingId, String hallId) {
        this.id = id;
        this.dayOfWeek = dayOfWeek;
        this.startHour = startHour;
        this.endtHour = endtHour;
        this.sport = sport;
        this.price = price;
        this.coachPESEL = coachPESEL;
        this.buildingId = buildingId;
        this.hallId = hallId;
    }
    
    public Zajecia(ResultSet rs) throws SQLException {
        this.id = rs.getInt(1);
        this.dayOfWeek = rs.getString(2);
        this.startHour = rs.getDate(3);
        this.endtHour = rs.getDate(4);
        this.sport = rs.getString(5);
        this.price = rs.getFloat(6);
        this.coachPESEL = rs.getString(7);
        this.buildingId = rs.getInt(8);
        this.hallId = rs.getString(9);
    }
    
    public int getId() {
        return id;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public Date getStartHour() {
        return startHour;
    }

    public Date getEndtHour() {
        return endtHour;
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
