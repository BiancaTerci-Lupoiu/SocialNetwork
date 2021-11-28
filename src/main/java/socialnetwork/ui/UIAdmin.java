package socialnetwork.ui;

import socialnetwork.domain.*;
import socialnetwork.service.Service;
import socialnetwork.ui.uiexception.ExitException;

import java.time.LocalDate;

public class UIAdmin extends UI {
    public UIAdmin(Service service) {
        super(service);
    }

    /**
     * ui function to add users
     */
    @UIMethod(name = "addUser", description = "adds a user")
    public void addUser(@UIParameter("first name") String firstName,
                          @UIParameter("last name") String lastName) {
        boolean result = service.addUser(firstName, lastName);
        if (!result)
            System.out.println("The user already exists!");
    }

    /**
     * ui function to update users
     */
    @UIMethod(name = "updateUser", description = "updates a user")
    public void updateUser(@UIParameter("id") Long id,
                             @UIParameter("first name") String firstName,
                             @UIParameter("last name") String lastName) {
        boolean result = service.updateUser(id, firstName, lastName);
        if (!result)
            System.out.println("The user with id=" + id + " does not exist!");
    }

    /**
     * ui function to delete users
     */
    @UIMethod(name = "deleteUser", description = "deletes a user")
    public void deleteUser(@UIParameter("id") Long id) {
        boolean result = service.deleteUser(id);
        if (!result)
            System.out.println("The user with id=" + id + " does not exist!");
    }

    /**
     * ui function to find users
     */
    @UIMethod(name = "findUser", description = "finds a user by their id")
    public void findUser(@UIParameter("id") Long id) {
        User result = service.getUserWithFriends(id);
        if (result == null)
            System.out.println("The user with id=" + id + " does not exist!");
    }

    /**
     * ui function to add a friendship
     */
    @UIMethod(name = "addFriend", description = "adds a friendship")
    public void addFriendship(@UIParameter("id user") Long idUser,
                                @UIParameter("id new friend") Long idNewFriend,
                                @UIParameter("date") LocalDate date) {
        boolean result = service.addFriendship(idUser, idNewFriend,date, Status.APPROVED);
        if (!result)
            System.out.println("They are already friends!");
    }

    /**
     * ui function to delete a friendship
     */
    @UIMethod(name = "deleteFriend", description = "deletes a friendship")
    public void deleteFriendship(@UIParameter("id user1") Long idUser,
                                   @UIParameter("id user2") Long idNewFriend) {
        boolean result = service.deleteFriendship(idUser, idNewFriend);
        if (!result)
            System.out.println("Friendship does not exist!");
    }

    /**
     * ui function to print the number of communities
     */
    @UIMethod(name = "communities", description = "shows the number of communities")
    public void numberOfCommunities() {
        int numberOfCommunities = service.numberOfCommunities();
        System.out.println("Number of communities: " + numberOfCommunities);
    }

    /**
     * ui function to print the most sociable community
     */
    @UIMethod(name = "sociable", description = "shows the most sociable community")
    public void theMostSociableCommunity() {
        Community community = service.theMostSociableCommunity();
        System.out.println("The most sociable community is:");
        for (User user : community.getCommunityUsers()) {
            System.out.println(user);
        }
    }

    /**
     * ui function to print all the friendships
     */
    @UIMethod(name = "showFriendships", description = "shows all the friendships")
    public void printAllFriendships() {
        for (Friendship friendship : service.getAllFriendships())
            System.out.println(friendship);
    }

    @UIMethod(name = "login", description = "")
    public void login(@UIParameter("id") Long id) throws Exception {
        var user = service.getUserWithFriends(id);
        if (user == null) {
            System.out.println("The user with id=" + id + " does not exist!");
        }
        UI ui = new UIUser(service, user);
        ui.start();
    }

    @UIMethod(name = "exit", description = "closes the application")
    public void exit() {
        throw new ExitException();
    }

    /**
     * ui method for get friends
     * @param id the id of a user
     */
    @UIMethod(name="getFriends",description ="finds all the friends of a user")
    public void getFriendsUI(@UIParameter("id")Long id){
        for(Friend friend: service.getFriends(id))
            System.out.println(friend);

    }

    /**
     *
     * @param id
     * @param month
     */
    @UIMethod(name="getFriendsByMonth",description ="finds the friends for a specific month")
    public void getFriendsByMonthUI(@UIParameter("id")Long id,
                                    @UIParameter("month") Integer month){
        for(Friend friend: service.getFriendsMonth(id,month))
            System.out.println(friend);
    }
}

