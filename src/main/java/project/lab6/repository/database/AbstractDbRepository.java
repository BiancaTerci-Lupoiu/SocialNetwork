package project.lab6.repository.database;

import project.lab6.domain.Entity;
import project.lab6.repository.database.query.Query;
import project.lab6.repository.database.query.SaveQuery;
import project.lab6.repository.repointerface.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class to store E entities to database
 *
 * @param <ID> -type E must have an attribute of type ID
 * @param <E>  - type of entities saved in repository
 */
public abstract class AbstractDbRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {

    protected final ConnectionPool connectionPool;

    /**
     * @param connectionPool
     */
    protected AbstractDbRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
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
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            try (PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    entities.add(getEntityFromSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connectionPool.releaseConnection(connection);
        }
        return entities;
    }

    protected E genericFindOne(Query query) {
        String sql = query.getSqlString();
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                query.setStatementParameters(statement);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return getEntityFromSet(resultSet);
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) connectionPool.releaseConnection(connection);
        }
        return null;
    }

    protected boolean genericUpdate(Query query) {
        return genericDeleteAndUpdate(query);
    }

    protected E genericSave(E entity, SaveQuery<E> query) {
        String sql = query.getSqlString();
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                query.setStatementParameters(statement);
                statement.executeUpdate();
                query.setId(entity, connection);
                return entity;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.releaseConnection(connection);
        }
        return null;
    }

    protected Long getLongId(Connection connection, String tableName, String idColumn) throws SQLException {
        String sqlGetId = String.format("select currval(pg_get_serial_sequence('%s','%s'))", tableName, idColumn);
        try (PreparedStatement statementGetId = connection.prepareStatement(sqlGetId)) {
            try (var result = statementGetId.executeQuery()) {
                if (!result.next())
                    return null;
                Long id = result.getLong(1);
                return id;
            }
        }
    }

    protected boolean genericDelete(Query query) {
        return genericDeleteAndUpdate(query);
    }

    private boolean genericDeleteAndUpdate(Query query)
    {
        String sql = query.getSqlString();
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                query.setStatementParameters(statement);
                if(statement.executeUpdate() == 1)
                    return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.releaseConnection(connection);
        }
        return false;
    }

    /**
     * @param set the ResultSet of a query
     * @return the entity created based on that ResultSet
     * @throws SQLException if the entity could not be converted
     */
    protected abstract E getEntityFromSet(ResultSet set) throws SQLException;
}
