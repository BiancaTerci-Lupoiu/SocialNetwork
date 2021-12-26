package project.lab6.repository.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BasicConnectionPool implements ConnectionPool {

    private final String url;
    private final String username;
    private final String password;

    private final List<Connection> connectionPool = new ArrayList<>();
    private final List<Connection> usedConnections = new ArrayList<>();

    public BasicConnectionPool(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if(connectionPool.size()==0)
        {
            Connection newConnection = createConnection();
            connectionPool.add(newConnection);
        }
        Connection connection = connectionPool
                .remove(connectionPool.size() - 1);
        if(!connection.isValid(1))
        {
            connection.close();
            connection = createConnection();
        }
        usedConnections.add(connection);
        return connection;
    }

    @Override
    public boolean releaseConnection(Connection connection) {
        if(connection == null)
            return false;
        connectionPool.add(connection);
        return usedConnections.remove(connection);
    }

    private Connection createConnection()
            throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    @Override
    public void close() throws SQLException {
        for(Connection connection: connectionPool)
            connection.close();
        if(usedConnections.size()!= 0)
            System.out.println("Warning! There are some connections still open!");
        for(Connection connection: usedConnections)
            connection.close();
    }
}
