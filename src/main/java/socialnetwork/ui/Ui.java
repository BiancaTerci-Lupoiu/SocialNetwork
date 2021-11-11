package socialnetwork.ui;

import socialnetwork.domain.Community;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.service.Service;
import socialnetwork.service.ServiceException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


public class Ui implements AutoCloseable {
    private final Service service;
    private final BufferedReader inputReader;
    private final Map<String, UIFunction> commands;
    private final Map<String, UIFunction> shortCommands;

    public Ui(Service service) {
        this.service = service;
        inputReader = new BufferedReader(new InputStreamReader(System.in));
        commands = new TreeMap<>();
        shortCommands = new TreeMap<>();
        Initialize();
    }

    private void Initialize() {
        for (var method : Ui.class.getMethods()) {
            var attribute = method.getAnnotation(UIMethod.class);
            if (attribute != null) {
                var function = new UIFunction(method, attribute);
                commands.put(function.getName(), function);
            }
        }
        int number = 1;
        for (var command : commands.values()) {
            command.setShortName(Integer.toString(number));
            number += 1;
            shortCommands.put(command.getShortName(), command);
        }
    }

    /**
     * starts the application
     */
    public void start() {
        while (true) {
            try {
                System.out.print(">>>");
                String commandLine = inputReader.readLine();
                String[] args = commandLine.split(" ");
                String command = args[0];
                if (command.equals("exit"))
                    break;
                var function = commands.get(command);
                if (function == null)
                    function = shortCommands.get(command);
                if (function == null) {
                    System.out.println("The command is not recognized!");
                    System.out.println("Try the command help!");
                    continue;
                }

                function.Call(this, Arrays.copyOfRange(args, 1, args.length));
            } catch (NumberFormatException ex) {
                System.out.println("Wrong input for number");
            } catch (ServiceException | ValidationException ex) {
                System.out.println(ex.getMessage());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    /**
     * Shows the specified message to the user and reads a line from standard input
     *
     * @param message String The message to be show
     * @return String
     */
    public String readString(String message) throws IOException {
        System.out.print(message);
        return inputReader.readLine();
    }

    @UIMethod(name = "help", description = "Shows this menu")
    public void help() {
        for (var command : commands.values()) {
            System.out.println(command.getShortName() + ") " + command.getName() + " " + String.join(" ", command.getParametersName().stream().map(x -> "<" + x + ">").toList()) + " -> " + command.getDescription());
        }
        System.out.println("exit -> close the application");
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
     * ui function to print the users
     */
    @UIMethod(name = "showUsers", description = "shows all the users")
    public void getAllUsersUI() {
        for (User user : service.getAllUsers().values())
            System.out.println(user);
    }

    /**
     * ui function to add a friendship
     */
    @UIMethod(name = "addFriend", description = "adds a friendship")
    public void addFriendshipUi(@UIParameter("id user") Long idUser,
                                @UIParameter("id new friend") Long idNewFriend) {
        boolean result = service.addFriendship(idUser, idNewFriend);
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

    @Override
    public void close() throws Exception {
        inputReader.close();
    }
}

