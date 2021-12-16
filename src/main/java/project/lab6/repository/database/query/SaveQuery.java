package project.lab6.repository.database.query;

import java.sql.Connection;
import java.sql.SQLException;

public interface SaveQuery<E> extends Query{
    void setId(E entity, Connection connection) throws SQLException;
}
