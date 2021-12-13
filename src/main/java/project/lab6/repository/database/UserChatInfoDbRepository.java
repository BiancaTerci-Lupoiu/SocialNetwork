package project.lab6.repository.database;

import project.lab6.domain.Tuple;
import project.lab6.domain.TupleWithIdChatUser;
import project.lab6.domain.User;
import project.lab6.domain.chat.UserChatInfo;
import project.lab6.domain.validators.ValidationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserChatInfoDbRepository extends AbstractDbRepository<TupleWithIdChatUser, UserChatInfo> {

    public UserChatInfoDbRepository(String url, String username, String password) {
        super(url, username, password);
    }

    @Override
    public UserChatInfo findOne(TupleWithIdChatUser id) {
        if (id == null)
            throw new IllegalArgumentException("id must be not null!");

        String sql = "select * from user_info where id_chat=? and id_user=?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setLong(1, id.getIdChat());
            statement.setLong(2, id.getIdUser());
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
    public boolean save(UserChatInfo userInfo) {
        if (userInfo == null)
            throw new IllegalArgumentException("user must be not null!");

        String sql = "insert into user_info(id_user, id_chat, nickname) VALUES (?,?,?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, userInfo.getIdUser());
            statement.setLong(2, userInfo.getIdChat());
            statement.setString(3, userInfo.getNickname());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(TupleWithIdChatUser id) {
        if (id == null)
            throw new IllegalArgumentException("id must be not null!");

        UserChatInfo result = findOne(id);
        if(result == null)
            return false;
        String sql = "delete from user_info where id_chat=? and id_user=?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, id.getIdChat());
            statement.setLong(2, id.getIdUser());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean update(UserChatInfo userInfo) {
        if (userInfo == null)
            throw new IllegalArgumentException("entity must be not null!");

        String sql = "update user_info set nickname=? where id_chat=? and id_user=?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)

        ) {
            statement.setString(1, userInfo.getNickname());
            statement.setLong(2, userInfo.getIdChat());
            statement.setLong(3, userInfo.getIdUser());

            int numberOfRowsAffected = statement.executeUpdate();
            if (numberOfRowsAffected == 1)
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected String getFindAllSqlStatement() {
        return "select * from user_info";
    }

    @Override
    protected UserChatInfo getEntityFromSet(ResultSet set) throws SQLException {
        Long idChat = set.getLong("id_chat");
        Long idUser = set.getLong("id_user");
        String nickname = set.getString("nickname");
        return new UserChatInfo(idChat,idUser,nickname);
    }
}
