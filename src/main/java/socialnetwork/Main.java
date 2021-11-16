package socialnetwork;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.FriendshipValidator;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.file.FriendshipFile;
import socialnetwork.repository.file.UserFile;
import socialnetwork.service.Service;
import socialnetwork.ui.UIAdmin;

//TODO: url, username si password citite din linia de comanda pentru conectarea la baza de date

//TODO: adauga enum Status(pending,approved,rejected)
//TODO: adauga clasa Friend, User in loc sa aibe o lista de User, sa aibe o lista de Friend.
//      Friend sa contina: User user, LocalDateTime date, Status status
//      SAU
//      in service functia GetFriends(Long idUtilizator, Status status) care
//      returneaza List<Friend> -> toate prieteniile care au acest status
//TODO: Actualizare clasa Friendship incat sa contina si LocalDateTime data, Status status.
//TODO: Schimbarea RepoFriendship din fisier si baza de date sa functioneze cu noile atribute adaugate
//TODO: Cerinta 1 si 2

//TODO: Adaugarea clasei Message si sa extinda Entity, varianta fara replyMessage
//TODO: Adaugarea RepositoryMessage in baza de date
//TODO: In service adaugam metoda getConversation(int idUser1, idUser2) -> List<Message> SAU Conversation.
//            Conversation va implementa toString() pentru a afisa frumos in UI
//            In ambele cazuri, trebuie ordonate descrescator dupa data
//TODO: Cerinta 3. Fie din UIAdmin sa vedem conversatia, fie din UIUser, fie din ambele locuri
//                 Poate o cale si de a vedea istoria conversatiilor a unui user

//TODO: In UIUser, afisarea cererilor de prietenie
//TODO: In UIUser, posibilitatea de a accepta/respinge cereri de prietenie
//TODO: Cerinta 4

//TODO: Optional. Mutarea acestui TODO list intr-un loc mai bun

public class Main {
    public static void main(String[] args) throws Exception {
        UserValidator userValidator = new UserValidator();
        FriendshipValidator friendshipValidator = new FriendshipValidator();
//        String username = "postgres";
//        String password = "Bianca01*";
//        String url = "jdbc:postgresql://localhost:5432/social_network";
//        Repository<Long, User> userDbRepository = new UserDbRepository(url, username, password, userValidator);
//        Repository<Tuple<Long, Long>, Friendship> friendshipDbRepository = new FriendshipDbRepository(url, username, password, friendshipValidator);
//        Service service = new Service(userDbRepository, friendshipDbRepository);
        Repository<Long, User> userFile = new UserFile("data/users.csv", userValidator);
        Repository<Tuple<Long, Long>, Friendship> friendshipFile = new FriendshipFile("data/friendships.csv", friendshipValidator);
        Service service = new Service(userFile, friendshipFile);

        UIAdmin ui = new UIAdmin(service);
        ui.start();
    }
}
