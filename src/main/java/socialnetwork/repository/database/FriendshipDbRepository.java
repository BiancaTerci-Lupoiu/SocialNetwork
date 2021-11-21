package socialnetwork.repository.database;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Status;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FriendshipDbRepository implements Repository<Tuple<Long, Long>, Friendship> {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<Friendship> validator;

    public FriendshipDbRepository(String url, String username, String password, Validator<Friendship> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    /**
     * @param id -the id of the friendship to be returned
     *           id must not be null
     * @return the friendship with the specified id
     * or null - if there is no friendship with the given id
     * @throws IllegalArgumentException if id is null.
     */
    @Override
    public Friendship findOne(Tuple<Long, Long> id) {
        if (id == null)
            throw new IllegalArgumentException("id must be not null!");
        String sql = "select * from friendships where id_user1=? and id_user2=?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            Long idUser1 = id.getLeft();
            Long idUser2 = id.getRight();

            statement.setLong(1, idUser1);
            statement.setLong(2, idUser2);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Long idUser1Found = resultSet.getLong("id_user1");
                    Long idUser2Found = resultSet.getLong("id_user2");
                    LocalDate dateFound = resultSet.getDate("date").toLocalDate();
                    Status status = Status.getStatus(resultSet.getInt("status"));
                    return new Friendship(idUser1Found, idUser2Found, dateFound, status);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return all friendships
     */
    @Override
    public Iterable<Friendship> findAll() {
        List<Friendship> friendships = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from friendships");
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                Long idUser1 = resultSet.getLong("id_user1");
                Long idUser2 = resultSet.getLong("id_user2");
                LocalDate date = resultSet.getDate("date").toLocalDate();
                Status status = Status.getStatus(resultSet.getInt("status"));
                Friendship friendship = new Friendship(idUser1, idUser2, date, status);
                friendships.add(friendship);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    /**
     * @param entity entity must be not null
     * @return true- if the given entity is saved
     * otherwise returns false (id already exists)
     * @throws ValidationException      if the entity is not valid
     * @throws IllegalArgumentException if the given entity is null.     *
     */
    @Override
    public boolean save(Friendship entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null!");

        validator.validate(entity);
        if(findOne(entity.getId()) != null)
            return false;

        String sql = "insert into friendships (id_user1,id_user2,date,status) values (?,?,?,?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, entity.getId().getLeft());
            statement.setLong(2, entity.getId().getRight());
            statement.setDate(3, Date.valueOf(entity.getDate()));
            statement.setInt(4, entity.getStatus().toInt());

            if (statement.executeUpdate() == 1)
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * removes the friendship with the specified id
     *
     * @param id id must be not null
     * @return @return true if the entity is deleted or false if there is no entity with the given id
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public boolean delete(Tuple<Long, Long> id) {
        if (id == null)
            throw new IllegalArgumentException("id must be not null!");
        String sql = "delete from friendships where id_user1=? and id_user2=?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            Long idUser1 = id.getLeft();
            Long idUser2 = id.getRight();

            statement.setLong(1, idUser1);
            statement.setLong(2, idUser2);

            if (statement.executeUpdate() == 1)
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param entity entity must not be null
     * @return true - if the entity is updated,
     * otherwise  returns false  - (e.g id does not exist).
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidationException      if the entity is not valid.
     */
    @Override
    public boolean update(Friendship entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);
        String sql = "update friendships set date=?,status=? where id_user1=? and id_user2=?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            Long idUser1 = entity.getId().getLeft();
            Long idUser2 = entity.getId().getRight();
            Date date = Date.valueOf(entity.getDate());
            Status status = entity.getStatus();

            statement.setDate(1, date);
            statement.setInt(2, status.toInt());
            statement.setLong(3, idUser1);
            statement.setLong(4, idUser2);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 1)
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
