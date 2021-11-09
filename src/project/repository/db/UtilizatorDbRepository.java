package project.repository.db;

import project.model.Utilizator;
import project.model.validare.Validator;
import project.repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UtilizatorDbRepository implements Repository<Integer, Utilizator> {
    private final String url;
    private final String username;
    private final String password;
    public UtilizatorDbRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }
    @Override
    public Utilizator findOne(Integer id) {
        String sql = "select * from utilizatori where id=?";
        Utilizator utilizator = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            try(var resultSet = ps.executeQuery())
            {
            if(resultSet.next())
            {
                String name = resultSet.getString("nume");
                utilizator = new Utilizator(id, name);
            }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilizator;
    }

    @Override
    public Iterable<Utilizator> findAll() {
        List<Utilizator> users = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from utilizatori");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String name = resultSet.getString("nume");
                Utilizator utilizator = new Utilizator(id, name);
                users.add(utilizator);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public boolean save(Utilizator entity) {
        String sql = "insert into utilizatori (nume) values (?)";
        int count=0;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getName());
            count=ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count!=0;
    }


    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM utilizatori where id=?";
        int count=0;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            count=ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count!=0;
    }

    @Override
    public boolean update(Utilizator entity) {
        String sql = """
                UPDATE utilizatori
                SET nume=?
                WHERE id=?""";
        int row_count=0;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getName());
            ps.setInt(2, entity.getId());
            row_count=ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return row_count!=0;
    }
}
