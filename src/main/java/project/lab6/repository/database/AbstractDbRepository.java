package project.lab6.repository.database;

import project.lab6.domain.Entity;
import project.lab6.repository.database.query.Query;
import project.lab6.repository.database.query.SaveQuery;
import project.lab6.repository.repointerface.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDbRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {

    protected final ConnectionPool connectionPool;

    protected AbstractDbRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    protected abstract String getFindAllSqlStatement();

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

    protected Long getLongId(Connection connection, String tableName, String idCollumn) throws SQLException {
        String sqlGetId = String.format("select currval(pg_get_serial_sequence('%s','%s'))", tableName, idCollumn);
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

    protected abstract E getEntityFromSet(ResultSet set) throws SQLException;
}
