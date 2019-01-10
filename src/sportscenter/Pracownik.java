package sportscenter;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Piter
 */
public class Pracownik extends SQLObject {
    public String PESEL;
    public String surname;
    public String name;
    public String profession;
    public Float salary;

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
    public void loadFromSql(ResultSet rs) throws SQLException {
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
    
}
