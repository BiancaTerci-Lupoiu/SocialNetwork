package project.lab6.repository.database.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Query {
    String getSqlString();

    void setStatementParameters(PreparedStatement statement) throws SQLException;
}
