package project.lab6.repository.database;

import project.lab6.domain.Entity;
import project.lab6.repository.repointerface.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Abstract class to store E entities to database
 *
 * @param <ID> -type E must have an attribute of type ID
 * @param <E>  - type of entities saved in repository
 */
public abstract class AbstractDbRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {
    private final String url;
    private final String username;
    private final String password;

    /**
     * constructor
     * @param url
     * @param username
     * @param password
     */
    protected AbstractDbRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * @return a String with a sql statement that returns all the entities from database
     */
    protected abstract String getFindAllSqlStatement();

    /**
     * @return all entities
     */
    @Override
    public List<E> findAll() {
        String sql = getFindAllSqlStatement();
        List<E> entities = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                entities.add(getEntityFromSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }

    /**
     * @return the connection to the database
     * @throws SQLException if the connection failed
     */
    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * @param set the ResultSet of a query
     * @return the entity created based on that ResultSet
     * @throws SQLException if the entity could not be converted
     */
    protected abstract E getEntityFromSet(ResultSet set) throws SQLException;
}
