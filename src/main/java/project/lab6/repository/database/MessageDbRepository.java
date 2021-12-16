package project.lab6.repository.database;

import project.lab6.domain.chat.Message;
import project.lab6.repository.database.query.Query;
import project.lab6.repository.database.query.SaveQuery;

import java.sql.*;
import java.time.LocalDateTime;

public class MessageDbRepository extends AbstractDbRepository<Long, Message> {
    public MessageDbRepository(ConnectionPool connectionPool) {
        super(connectionPool);
    }

    @Override
    protected String getFindAllSqlStatement() {
        return "select * from messages";
    }

    @Override
    public Message findOne(Long id) {
        if (id == null) throw new IllegalArgumentException("id must be not null!");

        return genericFindOne(new Query() {
            @Override
            public String getSqlString() {
                return "select * from messages where id=?";
            }

            @Override
            public void setStatementParameters(PreparedStatement statement) throws SQLException {
                statement.setLong(1, id);
            }
        });
    }

    @Override
    public Message save(Message message) {
        if (message == null) throw new IllegalArgumentException("user must be not null!");
        return genericSave(message, new SaveQuery<>() {
            @Override
            public void setId(Message entity, Connection connection) throws SQLException {
                entity.setId(getLongId(connection, "messages", "id"));
            }

            @Override
            public String getSqlString() {
                return "insert into messages(id_user_from, id_chat, text, date, id_message_replied) values (?,?,?,?,?)";
            }

            @Override
            public void setStatementParameters(PreparedStatement statement) throws SQLException {
                statement.setLong(1, message.getIdUserFrom());
                statement.setLong(2, message.getIdChat());
                statement.setString(3, message.getText());
                statement.setTimestamp(4, Timestamp.valueOf(message.getDate()));
                if (message.getIdReplyMessage() != null) statement.setLong(5, message.getIdReplyMessage());
                else statement.setNull(5, Types.BIGINT);
            }
        });
    }

    @Override
    public boolean delete(Long id) {
        if (id == null) throw new IllegalArgumentException("id must be not null!");

        return genericDelete(new Query() {
            @Override
            public String getSqlString() {
                return "delete from messages where id=?";
            }

            @Override
            public void setStatementParameters(PreparedStatement statement) throws SQLException {
                statement.setLong(1, id);
            }
        });
    }

    @Override
    public boolean update(Message message) {
        if (message == null) throw new IllegalArgumentException("entity must be not null!");
        return genericUpdate(new Query() {
            @Override
            public String getSqlString() {
                return "update messages set text=?, date=? where id=?";
            }

            @Override
            public void setStatementParameters(PreparedStatement statement) throws SQLException {
                statement.setString(1, message.getText());
                statement.setTimestamp(2, Timestamp.valueOf(message.getDate()));
                statement.setLong(3, message.getId());
            }
        });
    }

    @Override
    protected Message getEntityFromSet(ResultSet set) throws SQLException {
        Long id = set.getLong("id");
        String text = set.getString("text");
        LocalDateTime date = set.getTimestamp("date").toLocalDateTime();
        Long idUserFrom = set.getLong("id_user_from");
        Long idChat = set.getLong("id_chat");
        Long idReplyMessage = set.getLong("id_message_replied");
        return new Message(id, text, date, idUserFrom, idChat, idReplyMessage);
    }
}
