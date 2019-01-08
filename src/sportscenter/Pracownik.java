package sportscenter;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Piter
 */
public class Pracownik implements SQLLoad {
    public String pesel;
    public String nazwisko;
    public String imie;
    public String funkcja;

    public Pracownik(String pesel, String nazwisko, String imie, String funkcja) {
        this.pesel = pesel;
        this.nazwisko = nazwisko;
        this.imie = imie;
        this.funkcja = funkcja;
    }
    
    public Pracownik(ResultSet rs) throws SQLException {
        this.pesel = rs.getString(1);
        this.nazwisko = rs.getString(2);
        this.imie = rs.getString(3);
        this.funkcja = rs.getString(4);
    }

    @Override
    public void loadFromSql(ResultSet rs) throws SQLException {
        this.pesel = rs.getString(1);
        this.nazwisko = rs.getString(2);
        this.imie = rs.getString(3);
        this.funkcja = rs.getString(4);
    }
    
    public String getPesel() {
        return pesel;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public String getImie() {
        return imie;
    }

    public String getFunkcja() {
        return funkcja;
    }
    
}
