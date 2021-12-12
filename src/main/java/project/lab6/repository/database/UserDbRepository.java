package project.lab6.repository.database;

import project.lab6.domain.User;
import project.lab6.domain.validators.ValidationException;
import project.lab6.domain.validators.Validator;
import project.lab6.repository.Repository;
import project.lab6.repository.RepositoryUser;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserDbRepository extends AbstractDbRepository<Long, User> implements RepositoryUser {
    private final Validator<User> validator;

    public UserDbRepository(String url, String username, String password, Validator<User> validator) {
        super(url, username, password);
        this.validator = validator;
    }

    /**
     * @param aLong -the id of the user to be returned
     *              aLong must not be null
     * @return the user with the specified id (aLong)
     * or null - if there is no user with the given id
     * @throws IllegalArgumentException if id(aLong) is null.
     */
    @Override
    public User findOne(Long aLong) {
        if (aLong == null)
            throw new IllegalArgumentException("id must be not null!");

        String sql = "select * from users where id=?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);

        ) {
            statement.setLong(1, aLong);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return getEntityFromSet(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected String getFindAllSqlStatement() {
        return "SELECT * FROM users";
    }

    /**
     * @param entity entity must be not null
     * @return true- if the given entity is saved
     * otherwise returns false (id already exists)
     * @throws ValidationException      if the entity is not valid
     * @throws IllegalArgumentException if the given entity is null.     *
     */
    @Override
    public boolean save(User entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);

        String sql = "insert into users(first_name, last_name, hash_password, email, salt) values (?,?,?,?,?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getHashPassword());
            statement.setString(4, entity.getEmail());
            statement.setString(5, entity.getSalt());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * removes the user with the specified id
     *
     * @param aLong aLong must be not null
     * @return @return true if the user is deleted or false if there is no user with the given id (aLong)
     * @throws IllegalArgumentException if the given id(aLong) is null.
     */
    @Override
    public boolean delete(Long aLong) {
        if (aLong == null)
            throw new IllegalArgumentException("id must be not null!");

        User result = findOne(aLong);
        if (result != null) {
            String sql = "delete from users where id=?";

            try (Connection connection = getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)
            ) {
                statement.setLong(1, aLong);

                statement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return true;
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
    public boolean update(User entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);

        String sql = "update users set first_name=?, last_name=? where id=?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)

        ) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setLong(3, entity.getId());

            int numberOfRowsAffected = statement.executeUpdate();
            if (numberOfRowsAffected != 1)
                return false;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public User findByEmail(String email) {
        String sql = "select * from users where email=?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);

        ) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return getEntityFromSet(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected User getEntityFromSet(ResultSet set) throws SQLException {
        Long id = set.getLong("id");
        String email = set.getString("email");
        String firstName = set.getString("first_name");
        String lastName = set.getString("last_name");
        String hashPassword = set.getString("hash_password");
        String salt = set.getString("salt");
        return new User(id, email, firstName, lastName, hashPassword, salt);
    }
}
