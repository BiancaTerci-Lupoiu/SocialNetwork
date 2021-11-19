package socialnetwork;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Status;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.FriendshipValidator;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.database.FriendshipDbRepository;
import socialnetwork.repository.database.UserDbRepository;
import socialnetwork.service.Service;
import socialnetwork.ui.UIAdmin;

//TODO: Terec: url, username si password citite din linia de comanda pentru conectarea la baza de date. --DONE

//TODO: Bianca: adauga enum Status(pending,approved,rejected)  --DONE
//TODO: Decea: Friend sa contina: User user, LocalDate date, Status status --DONE
//      in service functia GetFriends(Long idUtilizator, Status status) care    --DONE
//      returneaza List<Friend> -> toate prieteniile care au acest status
//TODO: Bianca: !!!Actualizare clasa Friendship incat sa contina si LocalDate data, Status status. --DONE
//TODO: Bianca: !!!Schimbarea RepoFriendship din fisier si baza de date sa functioneze cu noile atribute adaugate  --DONE
//TODO: Decea: addFriendship(idUser1,idUser2,LocalDate date, Status status) va trebui sa primeasca si LocalDate si va seta statusul approved --DONE
//TODO: Decea: Cerinta 1 si 2 in UI

//TODO: Bianca: Adaugarea clasei Message si sa extinda Entity, varianta fara replyMessage
//TODO: Bianca: Adaugarea RepositoryMessage in baza de date
//TODO: Bianca: In service adaugam metoda getConversation(int idUser1, idUser2) -> List<Message> SAU Conversation.
//            Conversation va implementa toString() pentru a afisa frumos in UI
//            In ambele cazuri, trebuie ordonate descrescator dupa data
//TODO: Terec & Bianca: Cerinta 3. Fie din UIAdmin sa vedem conversatia, fie din UIUser, fie din ambele locuri
//                 Poate o cale si de a vedea istoria conversatiilor a unui user

//TODO: Terec: UI poate ar trebui redenumit la UIAdmin, si sa cream un nou UIUtilizator care primeste
//      un utilizator ca parametru si afiseaza acel UI specific pentru utilizator --DONE
//TODO: Ce se intampla daca 1 da cerere de prietenie la 2 si 2 da cerere de prietenie la 1?
//TODO: Terec: In UIUser, afisarea cererilor de prietenie --DONE
//TODO: Terec: In UIUser, posibilitatea de a accepta/respinge cereri de prietenie --DONE
//TODO: Terec: In UIUser, "addFriendship" va adauga cu statusul pending si va avea posibilitatea de a accepta celalalt user --DONE
//TODO: Terec: Cerinta 4 --DONE

//TODO: Optional. Mutarea acestui TODO list intr-un loc mai bun

public class Main {
    public static void main(String[] args) {
        if(args.length != 3)
        {
            System.out.println("Da ca si argumente url username password pentru conectarea la baza de date");
            return;
        }
        String url = args[0];
        String username = args[1];
        String password = args[2];

        UserValidator userValidator = new UserValidator();
        FriendshipValidator friendshipValidator = new FriendshipValidator();

        Repository<Long, User> userRepository = new UserDbRepository(url, username, password, userValidator);
        Repository<Tuple<Long, Long>, Friendship> friendshipRepository = new FriendshipDbRepository(url, username, password, friendshipValidator);

        Service service = new Service(userRepository, friendshipRepository);

        UIAdmin ui = new UIAdmin(service);
        ui.start();

    }
}
