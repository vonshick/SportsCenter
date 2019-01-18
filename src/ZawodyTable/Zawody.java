package ZawodyTable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import sportscenter.SQLObject;
import sportscenter.ValidateData;

public class Zawody extends SQLObject {
    private String name;
    private String date;
    private String disciplin;
    private Float price;
    private Integer IDSportObject;

    public Zawody() {
    }
    
    public Zawody(ResultSet rs) throws SQLException {
        this.name = rs.getString(1);
        try {
            this.date = (ValidateData.stringToDate(rs.getString(2))).toString();
        } catch (ParseException ex) {
            System.out.println("Parsing date failed, wrong format");
        }
        this.disciplin = rs.getString(3);
        this.price = rs.getFloat(4);
        this.IDSportObject = rs.getInt(5);
    }

    @Override
    public String getSth() {
        return name;
    }
    
    @Override
    public String toString() {
        return this.name + "," + this.date + "," + this.disciplin + "," + this.price + "," + this.IDSportObject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDisciplin() {
        return disciplin;
    }

    public void setDisciplin(String disciplin) {
        this.disciplin = disciplin;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getIDSportObject() {
        return IDSportObject;
    }

    public void setIDSportObject(Integer IDSportObject) {
        this.IDSportObject = IDSportObject;
    }
    
}
