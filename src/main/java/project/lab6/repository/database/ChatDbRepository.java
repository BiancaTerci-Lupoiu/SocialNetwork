package project.lab6.repository.database;

import javafx.scene.paint.Color;
import project.lab6.domain.chat.Chat;
import project.lab6.domain.chat.UserChatInfo;
import project.lab6.repository.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatDbRepository extends AbstractDbRepository<Long, Chat> {

    public ChatDbRepository(String url, String username, String password) {
        super(url, username, password);
    }

    @Override
    public Chat findOne(Long aLong) {
        return null;
    }

    @Override
    public boolean save(Chat entity) {
        return false;
    }

    @Override
    public boolean delete(Long aLong) {
        return false;
    }

    @Override
    public boolean update(Chat entity) {
        return false;
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
}
