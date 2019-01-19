package ZajeciaTable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import sportscenter.SQLObject;
import sportscenter.ValidateData;

public class Zajecia extends SQLObject {
    private int ID;
//    private int IDActivity;
//    private float price;
//    private String dateStart;
//    private String dateEnd;

    public Zajecia(ResultSet rs) throws SQLException {
        this.ID = rs.getInt(1);
//        this.IDActivity = rs.getInt(2);
//        this.price = rs.getFloat(3);
//        try {
//            this.dateStart = (ValidateData.stringToDate(rs.getString(4))).toString();
//            this.dateEnd = (ValidateData.stringToDate(rs.getString(5))).toString();
//        } catch (ParseException ex) {
//            System.out.println("Parsing date failed, wrong format");
//        }
    }
    
    @Override
    public String toString() {
        return this.ID + "";
//        return this.IDZajec + "," + this.IDActivity + "," + this.price + "," + this.dateStart + "," + this.dateEnd;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

}
