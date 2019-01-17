package PracownikTable;

import java.sql.ResultSet;
import java.sql.SQLException;
import sportscenter.SQLObject;

public class Pracownik extends SQLObject {
    private String PESEL;
    private String surname;
    private String name;
    private String profession;
    private Float salary;

    public Pracownik() {
    }
    
    public Pracownik(String PESEL, String surname, String name, String profession, Float salary) {
        this.PESEL = PESEL;
        this.surname = surname;
        this.name = name;
        this.profession = profession;
        this.salary = salary;
    }
    
    public Pracownik(ResultSet rs) throws SQLException {
        this.PESEL = rs.getString(1);
        this.surname = rs.getString(2);
        this.name = rs.getString(3);
        this.profession = rs.getString(4);
        this.salary = rs.getFloat(5);
    }

    @Override
    public String getSth() {
        return PESEL;
    }
    
    @Override
    public String toString() {
        return this.PESEL + "," + this.surname + "," + this.name + "," + this.profession + "," + this.salary;
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

    public String getProfession() {
        return profession;
    }

    public Float getSalary() {
        return salary;
    }

    public void setPESEL(String PESEL) {
        this.PESEL = PESEL;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public void setSalary(Float salary) {
        this.salary = salary;
    }
    
}
