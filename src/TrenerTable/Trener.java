package TrenerTable;

import java.sql.ResultSet;
import java.sql.SQLException;
import sportscenter.SQLObject;

public class Trener extends SQLObject {
    private String PESEL;
    private String disciplin;

    public Trener() {
    }
    
    public Trener(ResultSet rs) throws SQLException {
        this.PESEL = rs.getString(1);
        this.disciplin = rs.getString(2);
    }

    @Override
    public String toString() {
        return this.PESEL + "," + this.disciplin;
    }
    
    public String getPESEL() {
        return PESEL;
    }

    public String getDisciplin() {
        return disciplin;
    }
    
    public void setPESEL(String PESEL) {
        this.PESEL = PESEL;
    }

    public void setDisciplin(String disciplin) {
        this.disciplin = disciplin;
    }
    
}
