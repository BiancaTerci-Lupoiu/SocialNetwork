package socialnetwork.repository.database;

import socialnetwork.domain.User;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;


public class UserDbRepository implements Repository<Long, User> {
    private String url;
    private String username;
    private String password;
    private Validator<User> validator;

    public UserDbRepository(String url, String username, String password, Validator<User> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    /**
     * @param aLong -the id of the user to be returned
     *              aLong must not be null
     * @return the entity with the specified id (aLong)
     * or null - if there is no entity with the given id
     * @throws IllegalArgumentException if id(aLong) is null.
     */
    @Override
    public User findOne(Long aLong) {
        if (aLong == null)
            throw new IllegalArgumentException("id must be not null!");

        String sql = "select * from users where id=?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);

        ) {
            statement.setLong(1, aLong);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    User user = new User(firstName, lastName);
                    user.setId(id);
                    return user;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @return all users
     */
    @Override
    public Iterable<User> findAll() {
        Map<Long, User> users = new HashMap<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");
             // un tabel din bd
             ResultSet resultSet = statement.executeQuery()
        ) {
            // mutam cursorul in resultset. Initial e positionat inainte de prima linie
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                User user = new User(firstName, lastName);
                user.setId(id);
                users.put(user.getId(), user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users.values();
    }

    /**
     * @param entity entity must be not null
     * @return null- if the given user is saved
     * otherwise returns the user (id already exists)
     * @throws ValidationException      if the user is not valid
     * @throws IllegalArgumentException if the given user is null.     *
     */
    @Override
    public User save(User entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);

        String sql = "insert into users (first_name,last_name) values(?,?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * removes the user with the specified id
     *
     * @param aLong aLong must be not null
     * @return the removed user or null if there is no user with the given id(aLong)
     * @throws IllegalArgumentException if the given id(aLong) is null.
     */
    @Override
    public User delete(Long aLong) {
        if (aLong == null)
            throw new IllegalArgumentException("id must be not null!");

        User result = findOne(aLong);
        if (result != null) {
            String sql = "delete from users where id=?";

            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement statement = connection.prepareStatement(sql)
            ) {
                statement.setLong(1, aLong);

                statement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * @param entity entity must not be null
     * @return null - if the user is updated,
     * otherwise  returns the user  - (e.g id does not exist).
     * @throws IllegalArgumentException if the given user is null.
     * @throws ValidationException      if the user is not valid.
     */
    @Override
    public User update(User entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);

        String sql = "update users set first_name=?, last_name=? where id=?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)

        ) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setLong(3, entity.getId());

            Integer numberOfRowsAffected = statement.executeUpdate();
            if (numberOfRowsAffected != 1)
                return entity;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
