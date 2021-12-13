package project.lab6.factory;

import javafx.fxml.FXMLLoader;
import project.lab6.SocialNetworkApplication;
import project.lab6.config.ApplicationContext;
import project.lab6.domain.Friendship;
import project.lab6.domain.Tuple;
import project.lab6.domain.TupleWithIdChatUser;
import project.lab6.domain.User;
import project.lab6.domain.chat.Chat;
import project.lab6.domain.chat.Message;
import project.lab6.domain.chat.UserChatInfo;
import project.lab6.domain.validators.*;
import project.lab6.repository.database.*;
import project.lab6.repository.repointerface.Repository;
import project.lab6.repository.repointerface.RepositoryChat;
import project.lab6.repository.repointerface.RepositoryUser;
import project.lab6.service.ServiceFriends;
import project.lab6.service.ServiceMessages;
import project.lab6.setter_interface.SetterIdLoggedUser;
import project.lab6.setter_interface.SetterServiceFriends;

/**
 * Factory class to construct the skeleton of the application
 */
public class Factory {
    private static Factory instance = null;

    private String url;
    private String username;
    private String password;

    private Validator<User> userValidator = null;
    private Validator<Friendship> friendshipValidator = null;
    private Validator<Chat> chatValidator = null;
    private Validator<UserChatInfo> userChatInfoValidator = null;
    private Validator<Message> messageValidator = null;

    private RepositoryUser userRepository = null;
    private Repository<Tuple<Long,Long>, Friendship> friendshipRepository = null;
    private RepositoryChat repositoryChat = null;
    private Repository<TupleWithIdChatUser, UserChatInfo> userChatInfoRepository = null;
    private Repository<Long, Message> messageRepository = null;


    private ServiceFriends serviceFriends = null;
    private ServiceMessages serviceMessages = null;

    private Long idLoggedUser = null;

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
     * sets the idLoggedUser to the idLoggedUser value
     * @param idLoggedUser
     */
    public void setIdLoggedUser(Long idLoggedUser)
    {
        this.idLoggedUser = idLoggedUser;
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
    public Validator<Friendship> getFriendshipValidator()
    {
        if(friendshipValidator == null)
            friendshipValidator = new FriendshipValidator();
        return friendshipValidator;
    }

    public Validator<Chat> getChatValidator() {
        if(chatValidator == null)
            chatValidator = new ChatValidator();
        return chatValidator;
    }

    public Validator<UserChatInfo> getUserChatInfoValidator() {
        if(userChatInfoValidator == null)
            userChatInfoValidator = new UserChatInfoValidator();
        return userChatInfoValidator;
    }

    public Validator<Message> getMessageValidator() {
        if(messageValidator == null)
            messageValidator = new MessageValidator();
        return messageValidator;
    }

    /**
     * @return the users repository
     */
    public RepositoryUser getUserRepository() {
        if (userRepository == null)
            userRepository = new UserDbRepository(url, username, password, getUserValidator());
        return userRepository;
    }

    /**
     * @return the friendships repository
     */
    public Repository<Tuple<Long,Long>, Friendship> getFriendshipRepository()
    {
        if(friendshipRepository == null)
            friendshipRepository = new FriendshipDbRepository(url,username,password, getFriendshipValidator());
        return friendshipRepository;
    }

    public RepositoryChat getRepositoryChat() {
        if(repositoryChat == null)
            return new ChatDbRepository(url,username,password);
        return repositoryChat;
    }

    public Repository<TupleWithIdChatUser, UserChatInfo> getUserChatInfoRepository() {
        if(userChatInfoRepository == null)
            userChatInfoRepository = new UserChatInfoDbRepository(url,username,password);
        return userChatInfoRepository;
    }

    public Repository<Long, Message> getMessageRepository() {
        if(messageRepository == null)
            messageRepository = new MessageDbRepository(url,username,password);
        return messageRepository;
    }

    /**
     * @return the ServiceFriends
     */
    public ServiceFriends getServiceFriends()
    {
        if(serviceFriends == null)
            serviceFriends = new ServiceFriends(getUserRepository(),
                    getFriendshipRepository());
        return serviceFriends;
    }

    public ServiceMessages getServiceMessages()
    {
        if(serviceMessages == null)
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

    /**
     *
     * @param viewPath the path to an FXML file
     * @return the specific fxmlLoader to the viewPath path
     */
    public FXMLLoader getLoader(String viewPath)
    {
        FXMLLoader fxmlLoader = new FXMLLoader(SocialNetworkApplication.class.getResource(viewPath));
        fxmlLoader.setControllerFactory(controllerClass ->
        {
            Object object = null;
            try {
                object = controllerClass.getDeclaredConstructor().newInstance();
            } catch (Exception ex)
            {
                throw new RuntimeException(ex);
            }
            if(object instanceof SetterServiceFriends serviceSetter)
                serviceSetter.setServiceFriends(getServiceFriends());
            if(object instanceof SetterIdLoggedUser loggedUserSetter)
            {
                if(idLoggedUser == null)
                    throw new RuntimeException("Nu a fost setat id-ul userului");
                loggedUserSetter.setIdLoggedUser(idLoggedUser);
            }
            return object;
        });

        return fxmlLoader;
    }

}