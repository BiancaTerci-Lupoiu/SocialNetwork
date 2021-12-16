package project.lab6.repository.database;

import project.lab6.domain.TupleWithIdChatUser;
import project.lab6.domain.chat.UserChatInfo;
import project.lab6.repository.database.query.Query;
import project.lab6.repository.database.query.SaveQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserChatInfoDbRepository extends AbstractDbRepository<TupleWithIdChatUser, UserChatInfo> {
    public UserChatInfoDbRepository(ConnectionPool connectionPool) {
        super(connectionPool);
    }

    @Override
    public UserChatInfo findOne(TupleWithIdChatUser id) {
        if (id == null) throw new IllegalArgumentException("id must be not null!");

        return genericFindOne(new Query() {
            @Override
            public String getSqlString() {
                return "select * from user_infos where id_chat=? and id_user=?";
            }

            @Override
            public void setStatementParameters(PreparedStatement statement) throws SQLException {
                statement.setLong(1, id.getIdChat());
                statement.setLong(2, id.getIdUser());
            }
        });
    }

    @Override
    public UserChatInfo save(UserChatInfo userInfo) {
        if (userInfo == null) throw new IllegalArgumentException("user must be not null!");

        return genericSave(userInfo, new SaveQuery<UserChatInfo>() {
            @Override
            public void setId(UserChatInfo entity, Connection connection) {
            }

            @Override
            public String getSqlString() {
                return "insert into user_infos(id_user, id_chat, nickname) VALUES (?,?,?)";
            }

            @Override
            public void setStatementParameters(PreparedStatement statement) throws SQLException {
                statement.setLong(1, userInfo.getIdUser());
                statement.setLong(2, userInfo.getIdChat());
                statement.setString(3, userInfo.getNickname());
            }
        });
    }

    @Override
    public boolean delete(TupleWithIdChatUser id) {
        if (id == null) throw new IllegalArgumentException("id must be not null!");

        return genericDelete(new Query() {
            @Override
            public String getSqlString() {
                return "delete from user_infos where id_chat=? and id_user=?";
            }

            @Override
            public void setStatementParameters(PreparedStatement statement) throws SQLException {
                statement.setLong(1, id.getIdChat());
                statement.setLong(2, id.getIdUser());
            }
        });
    }

    @Override
    public boolean update(UserChatInfo userInfo) {
        if (userInfo == null) throw new IllegalArgumentException("entity must be not null!");

        return genericUpdate(new Query() {
            @Override
            public String getSqlString() {
                return "update user_infos set nickname=? where id_chat=? and id_user=?";
            }

            @Override
            public void setStatementParameters(PreparedStatement statement) throws SQLException {
                statement.setString(1, userInfo.getNickname());
                statement.setLong(2, userInfo.getIdChat());
                statement.setLong(3, userInfo.getIdUser());
            }
        });
    }

    @Override
    protected String getFindAllSqlStatement() {
        return "select * from user_infos";
    }

    @Override
    protected UserChatInfo getEntityFromSet(ResultSet set) throws SQLException {
        Long idChat = set.getLong("id_chat");
        Long idUser = set.getLong("id_user");
        String nickname = set.getString("nickname");
        return new UserChatInfo(idChat, idUser, nickname);
    }
}
