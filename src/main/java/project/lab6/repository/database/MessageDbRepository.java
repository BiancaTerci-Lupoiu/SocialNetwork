package project.lab6.repository.database;

import project.lab6.domain.chat.Message;

import java.sql.*;
import java.time.LocalDateTime;

public class MessageDbRepository extends AbstractDbRepository<Long, Message> {

    public MessageDbRepository(String url, String username, String password) {
        super(url, username, password);
    }

    @Override
    protected String getFindAllSqlStatement() {
        return "select * from messages";
    }

    @Override
    public Message findOne(Long id) {
        if (id == null)
            throw new IllegalArgumentException("id must be not null!");

        String sql = "select * from messages where id=?";
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
    public boolean save(Message message) {
        if (message == null)
            throw new IllegalArgumentException("user must be not null!");

        String sql = "insert into messages(id_user_from, id_chat, text, date, id_message_replied) values (?,?,?,?,?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, message.getIdUserFrom());
            statement.setLong(2, message.getIdChat());
            statement.setString(3, message.getText());
            statement.setTimestamp(4, Timestamp.valueOf(message.getDate()));
            statement.setLong(5, message.getIdReplyMessage());

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

        Message result = findOne(id);
        if(result == null)
            return false;
        String sql = "delete from messages where id=?";

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
    public boolean update(Message message) {
        if (message == null)
            throw new IllegalArgumentException("entity must be not null!");

        String sql = "update messages set text=?, date=? where id=?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)

        ) {
            statement.setString(1, message.getText());
            statement.setTimestamp(2, Timestamp.valueOf(message.getDate()));
            statement.setLong(3, message.getId());

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
    protected Message getEntityFromSet(ResultSet set) throws SQLException {
        Long id = set.getLong("id");
        String text = set.getString("text");
        LocalDateTime date = set.getTimestamp("date").toLocalDateTime();
        Long idUserFrom = set.getLong("id_user_from");
        Long idChat = set.getLong("id_chat");
        Long idReplyMessage = set.getLong("id_reply_message");
        return new Message(id,text,date,idUserFrom,idChat,idReplyMessage);
    }
}
