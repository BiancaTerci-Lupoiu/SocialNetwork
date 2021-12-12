package project.lab6.repository.database;

import javafx.scene.paint.Color;
import project.lab6.domain.User;
import project.lab6.domain.chat.Chat;
import project.lab6.domain.validators.ValidationException;
import project.lab6.repository.repointerface.RepositoryChat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatDbRepository extends AbstractDbRepository<Long, Chat> implements RepositoryChat {

    public ChatDbRepository(String url, String username, String password) {
        super(url, username, password);
    }

    @Override
    public Chat findOne(Long id) {
        if (id == null)
            throw new IllegalArgumentException("id must be not null!");

        String sql = "select * from chat where id=?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);

        ) {
            statement.setLong(1, id);
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
    public boolean save(Chat chat) {
        if (chat == null)
            throw new IllegalArgumentException("user must be not null!");

        String sql = "insert into chat(name, color, is_private) VALUES (?,?,?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, chat.getName());
            statement.setString(2, chat.getColor().toString());
            statement.setBoolean(3, chat.isPrivateChat());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean delete(Long id) {
        if (id == null)
            throw new IllegalArgumentException("id must be not null!");

        Chat result = findOne(id);
        if(result == null)
            return false;
        String sql = "delete from chat where id=?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean update(Chat chat) {
        if (chat == null)
            throw new IllegalArgumentException("entity must be not null!");

        String sql = "update chat set name=?, color=?, is_private=? where id=?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)

        ) {
            statement.setString(1, chat.getName());
            statement.setString(2, chat.getColor().toString());
            statement.setBoolean(3, chat.isPrivateChat());
            statement.setLong(4, chat.getId());

            int numberOfRowsAffected = statement.executeUpdate();
            if (numberOfRowsAffected != 1)
                return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    protected String getFindAllSqlStatement() {
        return "select * from chat";
    }

    @Override
    protected Chat getEntityFromSet(ResultSet set) throws SQLException {
        Long id = set.getLong("id");
        String name = set.getString("name");
        Color color = Color.valueOf(set.getString("color"));
        boolean isPrivateChat = set.getBoolean("is_private_chat");
        return new Chat(id,name,color,isPrivateChat);
    }

    @Override
    public Chat getPrivateChatBetweenUsers(Long idUser1, Long idUser2) {
        String sql = "select (chat.id,chat.color,chat.name,chat.is_private,user_info.id_user) " +
                "from chat inner join " +
                "user_info on chat.id=user_info.id_chat " +
                "where chat.is_private=true";

        return null;
    }

    @Override
    public Chat saveAndReturnChat(Chat chat) {
        if(!save(chat))
            return null;
        String sqlGetId = "select currval(pg_get_serial_sequence('chat','id'))";
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sqlGetId))
        {
            try(var result = statement.executeQuery())
            {
                result.next();
                Long id = result.getLong(1);
                chat.setId(id);
                return chat;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
