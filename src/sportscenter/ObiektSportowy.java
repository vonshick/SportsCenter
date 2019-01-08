package sportscenter;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Piter
 */
public class ObiektSportowy implements SQLLoad {
    public Integer idObiektu;
    public String lokalizacja;
    public String nazwa;
    public String typObiektu;

    public ObiektSportowy(int idObiektu, String lokalizacja, String nazwa, String typObiektu) {
        this.idObiektu = idObiektu;
        this.lokalizacja = lokalizacja;
        this.nazwa = nazwa;
        this.typObiektu = typObiektu;
    }
    
    public ObiektSportowy(ResultSet rs) throws SQLException {
        this.idObiektu = rs.getInt(1);
        this.lokalizacja = rs.getString(2);
        this.nazwa = rs.getString(3);
        this.typObiektu = rs.getString(4);
    }
    
    @Override
    public void loadFromSql(ResultSet rs) throws SQLException {
        this.idObiektu = rs.getInt(1);
        this.lokalizacja = rs.getString(2);
        this.nazwa = rs.getString(3);
        this.typObiektu = rs.getString(4);
    }

    public Integer getIdObiektu() {
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
    
}
