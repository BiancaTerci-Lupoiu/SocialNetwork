package project.lab6.ui.console;

import project.lab6.domain.*;
import project.lab6.service.Service;
import project.lab6.ui.console.uiexception.ExitException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class UIUser extends UI {
    private User user;

    private void updateUser() {
        user = service.getUserWithFriends(user.getId());
    }

    public UIUser(Service service, User user) {
        super(service);
        this.user = user;
    }

    @UIMethod(name = "profile", description = "shows this user profile")
    public void profile() {
        System.out.println("You are " + user);
    }

    @UIMethod(name = "showRequests", description = "shows all friend requests")
    public void showRequests() {
        updateUser();
        var friendRequests = user.getFriends(DirectedStatus.PENDING_RECEIVED);
        if (friendRequests.size() == 0) {
            System.out.println("You have no friend requests :(");
            return;
        }

        System.out.println("You have friend requests from:");
        for (var request : friendRequests)
            System.out.println(request);
    }

    @UIMethod(name = "accept", description = "accepts the friend request from this user")
    public void acceptFriendRequest(@UIParameter("id user") Long idUser) {
        service.modifyFriendRequestStatus(idUser, user.getId(), Status.APPROVED);
    }

    @UIMethod(name = "decline", description = "declines the friend request from this user")
    public void declineFriendRequest(@UIParameter("id user") Long idUser) {
        service.modifyFriendRequestStatus(idUser, user.getId(), Status.REJECTED);
    }

    @UIMethod(name = "add", description = "sends a friend request")
    public void sendFriendRequest(@UIParameter("id user") Long idUser,
                                  @UIParameter("date") LocalDate date) {
        boolean result = service.addFriendship(user.getId(), idUser, date, Status.PENDING);
        if (!result)
            System.out.println("They are already friends!");
    }

    @UIMethod(name = "sendMessage", description = "sends a message to a user")
    public void sendMessage(@UIParameter("text") String text,
                            @UIParameter("id users to") List<Long> idUsersTo) {
        boolean result = service.sendMessage(text, LocalDateTime.now(), user.getId(), idUsersTo);
        if (!result)
            System.out.println("Message already sent!");
    }

    @UIMethod(name = "replyMessage", description = "replies to a message")
    public void replyToMessage(@UIParameter("text") String text,
                               @UIParameter("id reply message") Long idReplyMessage) {
        boolean result = service.replyMessage(text, LocalDateTime.now(), user.getId(), idReplyMessage);
        if (!result)
            System.out.println("Message already sent!");
    }

    @UIMethod(name = "conversationPartners", description = "shows the users that have messages with the logged user ")
    public void showConversationPartners() {
        Collection<User> conversationPartners = service.getUsersThatHaveMessagesWithSomeUser(user.getId());
        if (conversationPartners.isEmpty())
            System.out.println("There are no conversation partners");
        else {
            System.out.println("Partners:");
            for (User user : conversationPartners)
                System.out.println(user);
        }
    }

    @UIMethod(name = "showConversation", description = "shows the conversation between the user and some other user")
    public void showConversation(@UIParameter("id user") Long idUser) {
        Conversation conversation = service.getConversation(user.getId(), idUser);
        if (conversation.getMessagesList().size() == 0)
            System.out.println("The users does not have an open conversation");
        else
            System.out.println(conversation);
    }

    @UIMethod(name = "logout", description = "closes this active session")
    public void logout() {
        throw new ExitException();
    }

    @UIMethod(name = "findMyFriends", description = "shows all your friends")
    public void getFriends() {
        for (Friend friend : service.getFriends(user.getId()))
            System.out.println(friend);
    }

    @UIMethod(name = "findMyFriendsByMonth", description = "shows all your friends for a specified month")
    public void getFriendsByMonth(@UIParameter("month") Integer month) {
        for (Friend friend : service.getFriendsMonth(user.getId(), month))
            System.out.println(friend);
    }
}
