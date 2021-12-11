package project.lab6.repository.database;

import project.lab6.domain.Entity;
import project.lab6.domain.Friendship;
import project.lab6.domain.Status;
import project.lab6.repository.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDbRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {
    private final String url;
    private final String username;
    private final String password;

    protected AbstractDbRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    protected abstract String getFindAllSqlStatement();

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

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    protected abstract E getEntityFromSet(ResultSet set) throws SQLException;
}
