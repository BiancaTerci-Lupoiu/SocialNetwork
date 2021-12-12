package project.lab6.factory;

import javafx.fxml.FXMLLoader;
import project.lab6.HelloApplication;
import project.lab6.config.ApplicationContext;
import project.lab6.domain.Friendship;
import project.lab6.domain.Tuple;
import project.lab6.domain.User;
import project.lab6.domain.validators.FriendshipValidator;
import project.lab6.domain.validators.UserValidator;
import project.lab6.domain.validators.Validator;
import project.lab6.repository.Repository;
import project.lab6.repository.RepositoryUser;
import project.lab6.repository.database.FriendshipDbRepository;
import project.lab6.repository.database.UserDbRepository;
import project.lab6.service.Service;
import project.lab6.setter_interface.SetterIdLoggedUser;
import project.lab6.setter_interface.SetterService;

import java.lang.reflect.InvocationTargetException;

public class Factory {
    private static Factory instance = null;

    private String url;
    private String username;
    private String password;

    private Validator<User> userValidator = null;
    private Validator<Friendship> friendshipValidator = null;

    private RepositoryUser userRepository = null;
    private Repository<Tuple<Long,Long>, Friendship> friendshipRepository = null;

    private Service service = null;

    private Long idLoggedUser = null;

    private Factory() {
        url = ApplicationContext.getPROPERTIES().getProperty("database.url");
        username = ApplicationContext.getPROPERTIES().getProperty("database.username");
        password = ApplicationContext.getPROPERTIES().getProperty("database.password");
    }

    public static Factory getInstance() {
        if (instance == null)
            instance = new Factory();
        return instance;
    }

    public void setIdLoggedUser(Long idLoggedUser)
    {
        this.idLoggedUser = idLoggedUser;
    }

    public Validator<User> getUserValidator() {
        if (userValidator == null)
            userValidator = new UserValidator();
        return userValidator;
    }

    public Validator<Friendship> getFriendshipValidator()
    {
        if(friendshipValidator == null)
            friendshipValidator = new FriendshipValidator();
        return friendshipValidator;
    }

    public RepositoryUser getUserRepository() {
        if (userRepository == null)
            userRepository = new UserDbRepository(url, username, password, getUserValidator());
        return userRepository;
    }

    public Repository<Tuple<Long,Long>, Friendship> getFriendshipRepository()
    {
        if(friendshipRepository == null)
            friendshipRepository = new FriendshipDbRepository(url,username,password, getFriendshipValidator());
        return friendshipRepository;
    }

    public Service getService()
    {
        if(service == null)
            service = new Service(getUserRepository(),
                    getFriendshipRepository());
        return service;
    }

    public FXMLLoader getLoader(String viewPath)
    {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(viewPath));
        fxmlLoader.setControllerFactory(controllerClass ->
        {
            Object object = null;
            try {
                object = controllerClass.getDeclaredConstructor().newInstance();
            } catch (Exception ex)
            {
                throw new RuntimeException(ex);
            }
            if(object instanceof SetterService serviceSetter)
                serviceSetter.setService(getService());
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