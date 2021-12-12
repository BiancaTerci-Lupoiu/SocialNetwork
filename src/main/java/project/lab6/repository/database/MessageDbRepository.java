package project.lab6.repository.database;

import project.lab6.domain.chat.Message;

import java.sql.ResultSet;
import java.sql.SQLException;
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
        return null;
    }


    @Override
    public boolean save(Message entity) {
        return false;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public boolean update(Message entity) {
        return false;
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
