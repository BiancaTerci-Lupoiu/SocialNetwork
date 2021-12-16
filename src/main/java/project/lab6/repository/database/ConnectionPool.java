package project.lab6.repository.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionPool extends AutoCloseable{
    Connection getConnection() throws SQLException;
    boolean releaseConnection(Connection connection);
}
