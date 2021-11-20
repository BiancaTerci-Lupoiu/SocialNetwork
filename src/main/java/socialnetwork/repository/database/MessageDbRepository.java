package socialnetwork.repository.database;

import socialnetwork.domain.Message;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;
import socialnetwork.domain.validators.ValidationException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class MessageDbRepository implements Repository<Long, Message> {

    private String url;
    private String username;
    private String password;
    private Validator<Message> validator;

    public MessageDbRepository(String url, String username, String password, Validator<Message> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }


    /**
     * @param id -the id of the entity to be returned
     *              id must not be null
     * @return the entity with the specified id
     * or null - if there is no entity with the given id
     * @throws IllegalArgumentException if id is null.
     */
    @Override
    public Message findOne(Long id) {
        if(id==null)
            throw new IllegalArgumentException("id must be not null!");

        String sql="select id,text,id_user_from,id_reply_message,date,id_user_to " +
                "from messages inner join messages_to on id=id_message where id=?";
        try(Connection connection=DriverManager.getConnection(url,username,password);
            PreparedStatement statement= connection.prepareStatement(sql);

        ){
            statement.setLong(1,id);
            try(ResultSet resultSet=statement.executeQuery()){
                if(resultSet.next()){
                    String text=resultSet.getString("text");
                    Long idUserFrom = resultSet.getLong("id_user_from");
                    Long idReplyMessage = resultSet.getLong("id_reply_message");
                    LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                    Long idUserTo = resultSet.getLong("id_user_to");
                    Message message = new Message(text, date, idUserFrom);
                    message.setId(id);
                    message.setIdReplyMessage(idReplyMessage);
                    message.addIdUserTo(idUserTo);
                    while(resultSet.next())
                        message.addIdUserTo(resultSet.getLong("id_user_to"));

                    return message;
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return all entities
     */
    @Override
    public Iterable<Message> findAll() {
        Map<Long, Message> allMessages = new HashMap<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementMessages = connection.prepareStatement("select id,text,id_user_from,id_reply_message,date,id_user_to from messages inner join messages_to on id=id_message");
             ResultSet resultSet = statementMessages.executeQuery();

        ) {

            while (resultSet.next()) {
                Long idMessage = resultSet.getLong("id");
                String text = resultSet.getString("text");
                Long idUserFrom = resultSet.getLong("id_user_from");
                Long idReplyMessage = resultSet.getLong("id_reply_message");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                Long idUserTo = resultSet.getLong("id_user_to");

                if (allMessages.get(idMessage) == null) {
                    Message message = new Message(text, date, idUserFrom);
                    message.setId(idMessage);
                    message.setIdReplyMessage(idReplyMessage);
                    message.addIdUserTo(idUserTo);
                    allMessages.put(idMessage, message);
                } else {
                    allMessages.get(idMessage).addIdUserTo(idUserTo);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allMessages.values();
    }

    /**
     * @param entity entity must be not null
     * @return true- if the given entity is saved
     * otherwise returns false (id already exists)
     * @throws ValidationException      if the entity is not valid
     * @throws IllegalArgumentException if the given entity is null.     *
     */
    @Override
    public boolean save(Message entity) {
        if(entity==null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);

        String sqlMessages="insert into messages (text,id_user_from,id_reply_message,date) values (?,?,?,?) ";
        String sqlMessagesTo="insert into messages_to (id_message,id_user_to) values (?,?)";
        try(Connection connection=DriverManager.getConnection(url,username,password);
            PreparedStatement statementMessages=connection.prepareStatement(sqlMessages);
            PreparedStatement statementMessagesTo=connection.prepareStatement(sqlMessagesTo)

        ){
            statementMessages.setString(1, entity.getText());
            statementMessages.setLong(2,entity.getIdUserFrom());
            statementMessages.setLong(3,entity.getIdReplyMessage());
            statementMessages.setTimestamp(4,Timestamp.valueOf(entity.getDate()));

            statementMessages.executeUpdate();
            for(Long idUserTo: entity.getIdUsersTo())
            {
                statementMessagesTo.setLong(1,entity.getId());
                statementMessagesTo.setLong(2,idUserTo);
                statementMessagesTo.executeUpdate();
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return true;
    }

    /**
     * removes the entity with the specified id
     *
     * @param id id must be not null
     * @return true if the entity is deleted or false if there is no entity with the given id
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public boolean delete(Long id) {
        if(id==null)
            throw new IllegalArgumentException("id must be not null!");

        Message messageFound=findOne(id);
        if(messageFound!=null){
            String sqlMessageTo="delete from messages_to where id_message=?";
            String sqlMessage="delete from messages where id=?";
            try(Connection connection=DriverManager.getConnection(url,username,password);
                PreparedStatement statementMessagesTo=connection.prepareStatement(sqlMessageTo);
                PreparedStatement statementMessages=connection.prepareStatement(sqlMessage)
            ){
                statementMessagesTo.setLong(1,id);
                statementMessagesTo.executeUpdate();
                statementMessages.setLong(1,id);
                statementMessages.executeUpdate();

            }catch(SQLException e){
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    /**
     * @param entity entity must not be null
     * @return true - if the entity is updated,
     * otherwise  returns false  - (e.g id does not exist).
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidationException      if the entity is not valid.
     */
    @Override
    public boolean update(Message entity) {
        return false;
    }
}