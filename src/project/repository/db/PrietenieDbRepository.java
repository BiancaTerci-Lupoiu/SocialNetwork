package project.repository.db;

import project.model.Prietenie;
import project.model.Tuple;
import project.model.validare.Validator;
import project.repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrietenieDbRepository implements Repository<Tuple<Integer, Integer>, Prietenie> {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<Prietenie> validator;

    public PrietenieDbRepository(String url, String username, String password, Validator<Prietenie> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Prietenie findOne(Tuple<Integer, Integer> id) {
        String sql="SELECT * from prieteni where id1=? and id2=?";
        Prietenie prietenie=null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id.getLeft());
            ps.setInt(2, id.getRight());

            try(ResultSet resultSet = ps.executeQuery())
            {
                if(resultSet.next())
                {
                    Integer id1=resultSet.getInt("id1");
                    Integer id2=resultSet.getInt("id2");
                    prietenie = new Prietenie(id1,id2);
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prietenie;
    }

    @Override
    public Iterable<Prietenie> findAll() {
        String sql="SELECT * from prieteni";
        List<Prietenie> prietenii = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Integer id1 = resultSet.getInt("id1");
                Integer id2 = resultSet.getInt("id2");
                var prietenie = new Prietenie(id1, id2);
                prietenii.add(prietenie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prietenii;
    }

    @Override
    public boolean save(Prietenie entity) {
        validator.valideaza(entity);
        String sql = "insert into prieteni (id1,id2) values (?,?)";
        int count = 0;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, entity.getId().getLeft());
            ps.setInt(2, entity.getId().getRight());
            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count != 0;
    }


    @Override
    public boolean delete(Tuple<Integer, Integer> id) {
        String sql = "DELETE FROM prieteni where id1=? AND id2=?";
        int count = 0;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id.getLeft());
            ps.setInt(2, id.getRight());
            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count != 0;
    }

    @Override
    public boolean update(Prietenie entity) {
        validator.valideaza(entity);
        return findOne(entity.getId()) != null;
    }
}
