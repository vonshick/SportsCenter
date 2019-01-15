package KlientTable;

import java.sql.ResultSet;
import java.sql.SQLException;
import sportscenter.SQLObject;

public class Klient extends SQLObject {
    private int ID;
    private String surname;
    private String name;

    public Klient() {
    }
    
    public Klient(ResultSet rs) throws SQLException {
        this.ID = rs.getInt(1);
        this.surname = rs.getString(2);
        this.name = rs.getString(3);
    }

    @Override
    public String getSth() {
        return surname;
    }
    
    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public int getIDKlienta() {
        return ID;
    }
    
    public void setIDKlienta(int IDKlienta) {
        this.ID = IDKlienta;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setName(String name) {
        this.name = name;
    }
}
