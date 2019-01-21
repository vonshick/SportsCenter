package KarnetTable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import sportscenter.SQLObject;
import sportscenter.ValidateData;

public class Karnet extends SQLObject {
    private int IDClient;
    private int IDActivity;
    private float price;
    private String dateStart;
    private String dateEnd;

    public Karnet() {
    }

    public Karnet(ResultSet rs) throws SQLException {
        this.IDClient = rs.getInt(1);
        this.IDActivity = rs.getInt(2);
        this.price = rs.getFloat(3);
        try {
            this.dateStart = (ValidateData.stringToDate(rs.getString(4))).toString();
            this.dateEnd = (ValidateData.stringToDate(rs.getString(5))).toString();
        } catch (ParseException ex) {
            System.out.println("Parsing date failed, wrong format");
        }
    }
    
    @Override
    public String toString() {
        return this.IDClient + "," + this.IDActivity + "," + this.price + "," + this.dateStart + "," + this.dateEnd;
    }

    public int getIDClient() {
        return IDClient;
    }

    public void setIDClient(int IDClient) {
        this.IDClient = IDClient;
    }

    public int getIDActivity() {
        return IDActivity;
    }

    public void setIDActivity(int IDActivity) {
        this.IDActivity = IDActivity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }
    
}
