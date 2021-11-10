package socialnetwork;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.FriendshipValidator;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.database.FriendshipDbRepository;
import socialnetwork.repository.database.UserDbRepository;
import socialnetwork.repository.file.FriendshipFile;
import socialnetwork.repository.file.UserFile;
import socialnetwork.service.Service;
import socialnetwork.ui.Ui;

public class Main {
    public static void main(String[] args) {
        UserValidator userValidator = new UserValidator();
        FriendshipValidator friendshipValidator = new FriendshipValidator();
        String username="postgres";
        String password="Bianca01*";
        String url="jdbc:postgresql://localhost:5432/social_network";
        Repository<Long,User> userDbRepository=new UserDbRepository(url,username,password,userValidator);
        Repository<Tuple<Long,Long>, Friendship> friendshipDbRepository=new FriendshipDbRepository(url,username,password,friendshipValidator);
        Service service = new Service(userDbRepository, friendshipDbRepository);
//        Repository<Long,User> userFile = new UserFile("data/users.csv", userValidator);
//        Repository<Tuple<Long,Long>,Friendship> friendshipFile = new FriendshipFile("data/friendships.csv", friendshipValidator);
//        Service service = new Service(userFile, friendshipFile);

        Ui ui = new Ui(service);
        ui.start();
    }
}
