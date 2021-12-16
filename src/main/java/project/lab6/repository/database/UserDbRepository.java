package project.lab6.repository.database;

import project.lab6.domain.User;
import project.lab6.domain.validators.ValidationException;
import project.lab6.domain.validators.Validator;
import project.lab6.repository.database.query.Query;
import project.lab6.repository.database.query.SaveQuery;
import project.lab6.repository.repointerface.RepositoryUser;

import java.sql.*;


public class UserDbRepository extends AbstractDbRepository<Long, User> implements RepositoryUser {
    private final Validator<User> validator;

    public UserDbRepository(ConnectionPool connectionPool, Validator<User> validator) {
        super(connectionPool);
        this.validator = validator;
    }

    /**
     * @param id -the id of the user to be returned
     *           id must not be null
     * @return the user with the specified id (id)
     * or null - if there is no user with the given id
     * @throws IllegalArgumentException if id(id) is null.
     */
    @Override
    public User findOne(Long id) {
        return genericFindOne(new Query() {
            @Override
            public String getSqlString() {
                return "select * from users where id=?";
            }

            @Override
            public void setStatementParameters(PreparedStatement statement) throws SQLException {
                statement.setLong(1, id);
            }
        });
    }

    /**
     * @return a String with a sql statement that returns all the users from database
     */
    @Override
    protected String getFindAllSqlStatement() {
        return "SELECT * FROM users";
    }

    /**
     * @param user user must be not null
     * @return true- if the given user is saved
     * otherwise returns false (id already exists or email already exists)
     * @throws ValidationException      if the user is not valid
     * @throws IllegalArgumentException if the given user is null.     *
     */
    @Override
    public User save(User user) {
        if (user == null) throw new IllegalArgumentException("user must be not null!");
        validator.validate(user);
        if (findByEmail(user.getEmail()) != null) return null;
        return genericSave(user, new SaveQuery<User>() {
            @Override
            public void setId(User entity, Connection connection) throws SQLException {
                entity.setId(getLongId(connection, "users", "id"));
            }

            @Override
            public String getSqlString() {
                return "insert into users(first_name, last_name, hash_password, email, salt) values (?,?,?,?,?)";
            }

            @Override
            public void setStatementParameters(PreparedStatement statement) throws SQLException {
                statement.setString(1, user.getFirstName());
                statement.setString(2, user.getLastName());
                statement.setString(3, user.getHashPassword());
                statement.setString(4, user.getEmail());
                statement.setString(5, user.getSalt());
            }
        });
    }

    /**
     * removes the user with the specified id
     *
     * @param id id must be not null
     * @return @return true if the user is deleted or false if there is no user with the given id (id)
     * @throws IllegalArgumentException if the given id(id) is null.
     */
    @Override
    public boolean delete(Long id) {
        if (id == null) throw new IllegalArgumentException("id must be not null!");

        return genericDelete(new Query() {
            @Override
            public String getSqlString() {
                return "delete from users where id=?";
            }

            @Override
            public void setStatementParameters(PreparedStatement statement) throws SQLException {
                statement.setLong(1, id);
            }
        });
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
        if (entity == null) throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);

        return genericUpdate(new Query() {
            @Override
            public String getSqlString() {
                return "update users set first_name=?, last_name=? where id=?";
            }

            @Override
            public void setStatementParameters(PreparedStatement statement) throws SQLException {
                statement.setString(1, entity.getFirstName());
                statement.setString(2, entity.getLastName());
                statement.setLong(3, entity.getId());
            }
        });
    }

    /**
     * @param email of the user we search for
     * @return the user with the specified email or
     * null otherwise
     */
    @Override
    public User findByEmail(String email) {
        return genericFindOne(new Query() {
            @Override
            public String getSqlString() {
                return "select * from users where email=?";
            }

            @Override
            public void setStatementParameters(PreparedStatement statement) throws SQLException {
                statement.setString(1, email);
            }
        });
    }

    /**
     * @param set the ResultSet of a query
     * @return the user created based on the ResultSet
     * @throws SQLException if the user could not be converted
     */
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
