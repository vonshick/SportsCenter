package sportscenter;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Piter
 */
public class SQLObject {
    public void loadFromSql(ResultSet rs) throws SQLException { System.out.println("Default method");}
    public String getSth() { return "0";}
}
