package project.lab6.factory;

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

}