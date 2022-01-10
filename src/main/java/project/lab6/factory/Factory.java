package project.lab6.factory;

import javafx.fxml.FXMLLoader;
import project.lab6.config.ApplicationContext;
import project.lab6.controllers.Controller;
import project.lab6.domain.Tuple;
import project.lab6.domain.TupleWithIdChatUser;
import project.lab6.domain.entities.Friendship;
import project.lab6.domain.entities.User;
import project.lab6.domain.entities.chat.Chat;
import project.lab6.domain.entities.chat.Message;
import project.lab6.domain.entities.chat.UserChatInfo;
import project.lab6.domain.validators.*;
import project.lab6.repository.database.*;
import project.lab6.repository.repointerface.Repository;
import project.lab6.repository.repointerface.RepositoryChat;
import project.lab6.repository.repointerface.RepositoryUser;
import project.lab6.service.ServiceFriends;
import project.lab6.service.ServiceMessages;
import project.lab6.utils.CustomLoader;

/**
 * Factory class to construct the skeleton of the application
 */
public class Factory implements AutoCloseable {
    private static Factory instance = null;

    private final String url;
    private final String username;
    private final String password;

    private Validator<User> userValidator = null;
    private Validator<Friendship> friendshipValidator = null;
    private Validator<Chat> chatValidator = null;
    private Validator<UserChatInfo> userChatInfoValidator = null;
    private Validator<Message> messageValidator = null;

    private ConnectionPool connectionPool = null;

    private RepositoryUser userRepository = null;
    private Repository<Tuple<Long, Long>, Friendship> friendshipRepository = null;
    private RepositoryChat repositoryChat = null;
    private Repository<TupleWithIdChatUser, UserChatInfo> userChatInfoRepository = null;
    private Repository<Long, Message> messageRepository = null;


    private ServiceFriends serviceFriends = null;
    private ServiceMessages serviceMessages = null;

    /**
     * constructor
     * we initialize the information about the database
     */
    private Factory() {
        url = ApplicationContext.getPROPERTIES().getProperty("database.url");
        username = ApplicationContext.getPROPERTIES().getProperty("database.username");
        password = ApplicationContext.getPROPERTIES().getProperty("database.password");
    }

    /**
     * @return the instance of the class
     */
    public static Factory getInstance() {
        if (instance == null)
            instance = new Factory();
        return instance;
    }

    /**
     * @return the user validator
     */
    public Validator<User> getUserValidator() {
        if (userValidator == null)
            userValidator = new UserValidator();
        return userValidator;
    }

    /**
     * @return the friendship validator
     */
    public Validator<Friendship> getFriendshipValidator() {
        if (friendshipValidator == null)
            friendshipValidator = new FriendshipValidator();
        return friendshipValidator;
    }

    public Validator<Chat> getChatValidator() {
        if (chatValidator == null)
            chatValidator = new ChatValidator();
        return chatValidator;
    }

    public Validator<UserChatInfo> getUserChatInfoValidator() {
        if (userChatInfoValidator == null) userChatInfoValidator = new UserChatInfoValidator();
        return userChatInfoValidator;
    }

    public Validator<Message> getMessageValidator() {
        if (messageValidator == null) messageValidator = new MessageValidator();
        return messageValidator;
    }

    public ConnectionPool getConnectionPool() {
        if (connectionPool == null) connectionPool = new BasicConnectionPool(url, username, password);
        return connectionPool;
    }

    /**
     * @return the users repository
     */
    public RepositoryUser getUserRepository() {
        if (userRepository == null) userRepository = new UserDbRepository(getConnectionPool(), getUserValidator());
        return userRepository;
    }

    /**
     * @return the friendships repository
     */
    public Repository<Tuple<Long, Long>, Friendship> getFriendshipRepository() {
        if (friendshipRepository == null)
            friendshipRepository = new FriendshipDbRepository(getConnectionPool(), getFriendshipValidator());
        return friendshipRepository;
    }

    public RepositoryChat getRepositoryChat() {
        if (repositoryChat == null) repositoryChat = new ChatDbRepository(getConnectionPool());
        return repositoryChat;
    }

    public Repository<TupleWithIdChatUser, UserChatInfo> getUserChatInfoRepository() {
        if (userChatInfoRepository == null) userChatInfoRepository = new UserChatInfoDbRepository(getConnectionPool());
        return userChatInfoRepository;
    }

    public Repository<Long, Message> getMessageRepository() {
        if (messageRepository == null) messageRepository = new MessageDbRepository(getConnectionPool());
        return messageRepository;
    }

    /**
     * @return the ServiceFriends
     */
    public ServiceFriends getServiceFriends() {
        if (serviceFriends == null)
            serviceFriends = new ServiceFriends(getUserRepository(),
                    getFriendshipRepository());
        return serviceFriends;
    }

    public ServiceMessages getServiceMessages() {
        if (serviceMessages == null)
            serviceMessages = new ServiceMessages(
                    getUserRepository(),
                    getRepositoryChat(),
                    getChatValidator(),
                    getMessageRepository(),
                    getMessageValidator(),
                    getUserChatInfoRepository(),
                    getUserChatInfoValidator());
        return serviceMessages;
    }

    public FXMLLoader getLoader(Controller controller) {
        return new CustomLoader(this, controller);
    }

    @Override
    public void close() throws Exception {
        if (connectionPool != null) connectionPool.close();
    }
}