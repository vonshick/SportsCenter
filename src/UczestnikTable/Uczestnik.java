package UczestnikTable;

import java.sql.ResultSet;
import java.sql.SQLException;
import sportscenter.SQLObject;

public class Uczestnik extends SQLObject {
    int id;
    private String PESEL;
    private String surname;
    private String name;
    private int status;
    private String competition;
    
    public Uczestnik(int id, String PESEL, String nazwisko, String imie, int oplacony, String competition) {
        this.id = id;
        this.PESEL = PESEL;
        this.surname = nazwisko;
        this.name = imie;
        this.status = oplacony;
        this.competition = competition;
    }

    public Uczestnik(){}
    
    
    public Uczestnik(ResultSet rs) throws SQLException {
        this.id = rs.getInt(1);
        this.PESEL = rs.getString(2);
        this.surname = rs.getString(3);
        this.name = rs.getString(4);
        this.status = rs.getInt(5);
        this.competition = rs.getString(6);
    }
    
    @Override
    public String toString() {
        return this.id + "," + this.PESEL + "," + this.surname + "," + this.name + "," + this.status + "," + this.competition;
    }

    public int getId(){
        return id;
    }
    
    public String getPESEL() {
        return PESEL;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public int getStatus() {
        return status;
    }

    public String getCompetition(){
        return competition;
    }



 


    
    
}
