package project.lab6.repository.database;

import project.lab6.domain.Tuple;
import project.lab6.domain.chat.UserChatInfo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserChatInfoDbRepository extends AbstractDbRepository<Tuple<Long, Long>, UserChatInfo> {

    public UserChatInfoDbRepository(String url, String username, String password) {
        super(url, username, password);
    }

    /**
     *
     * @param id -the id of the entity to be returned
     *           id must not be null
     *           the 2 values are: idChat, idUser in this order
     * @return the UserChatInfo
     */
    @Override
    public UserChatInfo findOne(Tuple<Long, Long> id) {
        return null;
    }

    @Override
    public boolean save(UserChatInfo entity) {
        return false;
    }

    @Override
    public boolean delete(Tuple<Long, Long> longLongTuple) {
        return false;
    }

    @Override
    public boolean update(UserChatInfo entity) {
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
