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
    public static void main(String[] args) throws Exception {
        //TODO: adauga enum Status(pending,approved,rejected)
        //TODO: adauga clasa Friend, User in loc sa aibe o lista de User, sa aibe o lista de Friend.
        //      Friend sa contina: User user, LocalDateTime data, status prietenie
        //TODO: Actualizare clasa Friendship incat sa contina si LocalDateTime data, Status status.
        //TODO: Schimbarea RepoFriendship sa functioneze cu noile atribute adaugate
        //TODO: Ui poate ar trebui redenumit la UIAdmin, si sa cream un nou UIUtilizator care primeste
        //      un utilizator ca parametru si afiseaza acel UI specific pentru utilizator
        //TODO: Adaugarea functiilor de login in UIAdmin si logout(exit) in UIUtilizator
        //TODO: Adaugarea clasei Message si sa extinda Entity, varianta fara replyMessage
        //TODO: Adaugarea RepositoryMessage in baza de date
        //TODO: In UIUtilizator, afisarea cererilor de prietenie
        //TODO: In UIUtilizator, posibilitatea de a accepta/respinge cereri de prietenie

        UserValidator userValidator = new UserValidator();
        FriendshipValidator friendshipValidator = new FriendshipValidator();
//        String username = "postgres";
//        String password = "Bianca01*";
//        String url = "jdbc:postgresql://localhost:5432/social_network";
//        Repository<Long, User> userDbRepository = new UserDbRepository(url, username, password, userValidator);
//        Repository<Tuple<Long, Long>, Friendship> friendshipDbRepository = new FriendshipDbRepository(url, username, password, friendshipValidator);
//        Service service = new Service(userDbRepository, friendshipDbRepository);
        Repository<Long,User> userFile = new UserFile("data/users.csv", userValidator);
        Repository<Tuple<Long,Long>,Friendship> friendshipFile = new FriendshipFile("data/friendships.csv", friendshipValidator);
        Service service = new Service(userFile, friendshipFile);

        try (Ui ui = new Ui(service)) {
            ui.start();
        }
    }
}
