package ObiektSportowyTable;

import java.sql.ResultSet;
import java.sql.SQLException;
import sportscenter.SQLObject;

public class ObiektSportowy extends SQLObject {
    private String idObiektu;
    private String lokalizacja;
    private String nazwa;
    private String typObiektu;

    public ObiektSportowy(String idObiektu, String lokalizacja, String nazwa, String typObiektu) {
        this.idObiektu = idObiektu;
        this.lokalizacja = lokalizacja;
        this.nazwa = nazwa;
        this.typObiektu = typObiektu;
    }
    
    public ObiektSportowy(ResultSet rs) throws SQLException {
        this.idObiektu = rs.getString(1);
        this.lokalizacja = rs.getString(2);
        this.nazwa = rs.getString(3);
        this.typObiektu = rs.getString(4);
    }
    
    @Override
    public String toString() {
        return this.idObiektu + "," + this.lokalizacja + "," + this.nazwa + "," + this.typObiektu;
    }

    public String getIdObiektu() {
        return idObiektu;
    }

    public String getLokalizacja() {
        return lokalizacja;
    }

    public String getNazwa() {
        return nazwa;
    }

    public String getTypObiektu() {
        return typObiektu;
    }

    public void setIdObiektu(String idObiektu) {
        this.idObiektu = idObiektu;
    }

    public void setLokalizacja(String lokalizacja) {
        this.lokalizacja = lokalizacja;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public void setTypObiektu(String typObiektu) {
        this.typObiektu = typObiektu;
    }
    
}
