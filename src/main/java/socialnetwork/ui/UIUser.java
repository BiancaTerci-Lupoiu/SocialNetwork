package socialnetwork.ui;

import socialnetwork.domain.DirectedStatus;
import socialnetwork.domain.Status;
import socialnetwork.domain.User;
import socialnetwork.service.Service;
import socialnetwork.ui.uiexception.ExitException;

import java.time.LocalDate;

public class UIUser extends UI{
    private User user;

    private void updateUser()
    {
        user = service.findUser(user.getId());
    }

    public UIUser(Service service, User user) {
        super(service);
        this.user = user;
    }

    @UIMethod(name = "showRequests",description = "shows all friend requests")
    public void showRequests()
    {
        updateUser();
        var friendRequests = user.getFriends(DirectedStatus.PENDING_RECEIVED);
        if(friendRequests.size()==0)
        {
            System.out.println("You have no friend requests :(");
            return;
        }
        System.out.println("The friend requests are:");
        for(var request: friendRequests)
            System.out.println(request);
    }

    @UIMethod(name = "sendRequest", description = "sends a friend request")
    public void sendFriendRequest(@UIParameter("id user") Long idUser,
                                  @UIParameter("date") LocalDate date)
    {
        service.addFriendship(user.getId(), idUser, date, Status.PENDING);
    }

    @UIMethod(name = "accept", description = "accepts the friend request from this user")
    public void acceptFriendRequest(@UIParameter("id user") Long idUser)
    {
        service.modifyFriendRequestStatus(idUser, user.getId(), Status.APPROVED);
    }

    @UIMethod(name = "decline", description = "declines the friend request from this user")
    public void declineFriendRequest(@UIParameter("id user") Long idUser)
    {
        service.modifyFriendRequestStatus(idUser, user.getId(), Status.REJECTED);
    }

    @UIMethod(name = "add", description = "sends a friend request")
    public void addFriend(@UIParameter("id user") Long idUser,
                          @UIParameter("date") LocalDate date)
    {
        boolean result = service.addFriendship(user.getId(), idUser, date, Status.PENDING);
        if (!result)
            System.out.println("They are already friends!");
    }

    @UIMethod(name = "logout", description = "closes this active session")
    public void logout()
    {
        throw new ExitException();
    }
}
