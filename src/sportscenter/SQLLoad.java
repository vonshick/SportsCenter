package sportscenter;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Piter
 */
public interface SQLLoad {
    public void loadFromSql(ResultSet rs) throws SQLException;
}
