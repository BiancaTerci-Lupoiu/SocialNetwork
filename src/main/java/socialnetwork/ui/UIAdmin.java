package socialnetwork.ui;

import socialnetwork.domain.Community;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.Status;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.service.Service;
import socialnetwork.service.ServiceException;
import socialnetwork.utils.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;


public class UIAdmin extends UI {
    public UIAdmin(Service service) {
        super(service);
    }

    /**
     * ui function to add users
     */
    @UIMethod(name = "addUser", description = "adds a user")
    public void addUserUi(@UIParameter("first name") String firstName,
                          @UIParameter("last name") String lastName) {
        boolean result = service.addUser(firstName, lastName);
        if (!result)
            System.out.println("The user already exists!");
    }

    /**
     * ui function to update users
     */
    @UIMethod(name = "updateUser", description = "updates a user")
    public void updateUserUi(@UIParameter("id") Long id,
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
    public void deleteUserUi(@UIParameter("id") Long id) {
        boolean result = service.deleteUser(id);
        if (!result)
            System.out.println("The user with id=" + id + " does not exist!");
    }

    /**
     * ui function to find users
     */
    @UIMethod(name = "findUser", description = "finds a user by their id")
    public void findUserUi(@UIParameter("id") Long id) {
        User result = service.findUser(id);
        if (result == null)
            System.out.println("The user with id=" + id + " does not exist!");
    }

    /**
     * ui function to add a friendship
     */
    @UIMethod(name = "addFriend", description = "adds a friendship")
    public void addFriendshipUi(@UIParameter("id user") Long idUser,
                                @UIParameter("id new friend") Long idNewFriend) {
        LocalDate now=LocalDate.now();
        boolean result = service.addFriendship(idUser, idNewFriend,now, Status.APPROVED);
        if (!result)
            System.out.println("They are already friends!");
    }

    /**
     * ui function to delete a friendship
     */
    @UIMethod(name = "deleteFriend", description = "deletes a friendship")
    public void deleteFriendshipUi(@UIParameter("id user1") Long idUser,
                                   @UIParameter("id user2") Long idNewFriend) {
        boolean result = service.deleteFriendship(idUser, idNewFriend);
        if (!result)
            System.out.println("Friendship does not exist!");
    }

    /**
     * ui function to print the number of communities
     */
    @UIMethod(name = "communities", description = "shows the number of communities")
    public void numberOfCommunitiesUi() {
        int numberOfCommunities = service.numberOfCommunities();
        System.out.println("Number of communities: " + numberOfCommunities);
    }

    /**
     * ui function to print the most sociable community
     */
    @UIMethod(name = "sociable", description = "shows the most sociable community")
    public void theMostSociableCommunityUi() {
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
        var user = service.findUser(id);
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
}

