package project.lab6.repository.database;

import javafx.scene.paint.Color;
import project.lab6.domain.entities.chat.Chat;
import project.lab6.repository.database.query.Query;
import project.lab6.repository.database.query.SaveQuery;
import project.lab6.repository.paging.Page;
import project.lab6.repository.paging.Pageable;
import project.lab6.repository.repointerface.RepositoryChat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ChatDbRepository extends AbstractDbRepository<Long, Chat> implements RepositoryChat {

    public ChatDbRepository(ConnectionPool connectionPool) {
        super(connectionPool);
    }

    @Override
    public Chat findOne(Long id) {
        if (id == null)
            throw new IllegalArgumentException("id must be not null!");
        return genericFindOne(new Query() {
            @Override
            public String getSqlString() {
                return "select * from chats where id=?";
            }

            @Override
            public void setStatementParameters(PreparedStatement statement) throws SQLException {
                statement.setLong(1, id);
            }
        });
    }

    @Override
    public Chat save(Chat chat) {
        if (chat == null)
            throw new IllegalArgumentException("user must be not null!");

        return genericSave(chat, new SaveQuery<>() {
            @Override
            public void setId(Chat entity, Connection connection) throws SQLException {
                entity.setId(getLongId(connection, "chats", "id"));
            }

            @Override
            public String getSqlString() {
                return "insert into chats(name, color, is_private) VALUES (?,?,?)";
            }

            @Override
            public void setStatementParameters(PreparedStatement statement) throws SQLException {
                statement.setString(1, chat.getName());
                statement.setString(2, chat.getColor().toString());
                statement.setBoolean(3, chat.isPrivateChat());
            }
        });
    }

    @Override
    public boolean delete(Long id) {
        if (id == null)
            throw new IllegalArgumentException("id must be not null!");

        return genericDelete(new Query() {
            @Override
            public String getSqlString() {
                return "delete from chats where id=?";
            }

            @Override
            public void setStatementParameters(PreparedStatement statement) throws SQLException {
                statement.setLong(1, id);
            }
        });
    }

    @Override
    public boolean update(Chat chat) {
        if (chat == null)
            throw new IllegalArgumentException("entity must be not null!");

        return genericUpdate(new Query() {
            @Override
            public String getSqlString() {
                return "update chats set name=?, color=?, is_private=? where id=?";
            }

            @Override
            public void setStatementParameters(PreparedStatement statement) throws SQLException {
                statement.setString(1, chat.getName());
                statement.setString(2, chat.getColor().toString());
                statement.setBoolean(3, chat.isPrivateChat());
                statement.setLong(4, chat.getId());
            }
        });
    }

    @Override
    public List<Chat> findAll() {
        return genericFindAll("select * from chats");
    }

    @Override
    public Page<Chat> findAll(Pageable pageable) {
        return genericFindAllPaged(pageable, "select * from chats ORDER BY id LIMIT ? OFFSET ?");
    }

    @Override
    protected Chat getEntityFromSet(ResultSet set) throws SQLException {
        Long id = set.getLong("id");
        String name = set.getString("name");
        Color color = Color.valueOf(set.getString("color"));
        boolean isPrivateChat = set.getBoolean("is_private");
        return new Chat(id, name, color, isPrivateChat);
    }

    @Override
    public Chat getPrivateChatBetweenUsers(Long idUser1, Long idUser2) {
        return genericFindOne(new Query() {
            @Override
            public String getSqlString() {
                return "select c.id,c.color,c.name,c.is_private " +
                        "from chats c inner join " +
                        "user_infos u1 on c.id=u1.id_chat " +
                        "inner join user_infos u2 on c.id = u2.id_chat " +
                        "where c.is_private=true and u1.id_user=? and u2.id_user=?";
            }

            @Override
            public void setStatementParameters(PreparedStatement statement) throws SQLException {
                statement.setLong(1, idUser1);
                statement.setLong(2, idUser2);
            }
        });
    }
}
