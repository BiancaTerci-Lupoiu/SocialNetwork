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


public class Ui implements AutoCloseable {
    private final Service service;
    private final BufferedReader inputReader;

    public Ui(Service service) {
        this.service = service;
        inputReader = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * prints the menu
     */
    private void printMenu() {
        System.out.println("Meniu");
        System.out.println("0.Quit");
        System.out.println("1.Add User");
        System.out.println("2.Delete User");
        System.out.println("3.Update User");
        System.out.println("4.Find user by id");
        System.out.println("5.Print all users");
        System.out.println("6.Add friend for some user");
        System.out.println("7.Delete friend for some user");
        System.out.println("8.Number of communities");
        System.out.println("9.The most sociable community");
        System.out.println("10.Print all friendships");
        System.out.println("Select command:");
    }

    /**
     * ui function to add users
     *
     * @throws IOException if the readline() fails
     */
    private void addUserUi() throws IOException {
        //System.out.println("id=");
        // Long id = Long.parseLong(inputReader.readLine());
        System.out.println("firstName=");
        String firstName = inputReader.readLine();
        System.out.println("lastName=");
        String lastName = inputReader.readLine();
        boolean result = service.addUser(firstName, lastName);
        if (result ==false)
            System.out.println("The user already exists!");
    }

    /**
     * ui function to update users
     *
     * @throws IOException if the readline() fails
     */
    private void updateUserUi() throws IOException {
        System.out.println("id=");
        Long id = Long.parseLong(inputReader.readLine());
        System.out.println("firstName=");
        String firstName = inputReader.readLine();
        System.out.println("lastName=");
        String lastName = inputReader.readLine();
        boolean result = service.updateUser(id, firstName, lastName);
        if (result ==false)
            System.out.println("The user with id=" + id + " does not exist!");
    }

    /**
     * ui function to delete users
     *
     * @throws IOException if the readline() fails
     */
    private void deleteUserUi() throws IOException {
        System.out.println("id=");
        Long id = Long.parseLong(inputReader.readLine());
        boolean result = service.deleteUser(id);
        if (result == false)
            System.out.println("The user with id=" + id + " does not exist!");

    }

    /**
     * ui function to find users
     *
     * @throws IOException if the readline() fails
     */
    private void findUserUi() throws IOException {
        System.out.println("id=");
        Long id = Long.parseLong(inputReader.readLine());
        User result = service.findUser(id);
        if (result == null)
            System.out.println("The user with id=" + id + " does not exist!");
    }

    /**
     * ui function to print the users
     */
    private void getAllUsersUI() {
        for (User user : service.getAllUsers().values())
            System.out.println(user);
    }

    /**
     * ui function to add a friendship
     *
     * @throws IOException if the readline() fails
     */
    private void addFriendshipUi() throws IOException {
        System.out.println("id user=");
        Long idUser = Long.parseLong(inputReader.readLine());
        System.out.println("id new friend=");
        Long idNewFriend = Long.parseLong(inputReader.readLine());
        boolean result = service.addFriendship(idUser, idNewFriend);
        if (result ==false)
            System.out.println("They are already friends!");
    }

    /**
     * ui function to delete a friendship
     *
     * @throws IOException if the readline() fails
     */
    private void deleteFriendshipUi() throws IOException {
        System.out.println("id user1=");
        Long idUser = Long.parseLong(inputReader.readLine());
        System.out.println("id user2=");
        Long idNewFriend = Long.parseLong(inputReader.readLine());
        boolean result = service.deleteFriendship(idUser, idNewFriend);
        if (result == false)
            System.out.println("Friendship does not exist!");
    }

    /**
     * ui function to print the number of communities
     */
    private void numberOfCommunitiesUi() {
        int numberOfCommunities = service.numberOfCommunities();
        System.out.println("Number of communities: " + numberOfCommunities);
    }

    /**
     * ui function to print the most sociable community
     */
    private void theMostSociableCommunityUi() {
        Community community = service.theMostSociableCommunity();
        System.out.println("The most sociable community is:");
        for (User user : community.getCommunityUsers()) {
            System.out.println(user);
        }
    }

    /**
     * ui function to print all the friendships
     */
    private void printAllFriendships() {
        for (Friendship friendship : service.getAllFriendships())
            System.out.println(friendship);
    }

    /**
     * starts the application
     */
    public void start() {
        while (true) {
            printMenu();
            try {
                String command = inputReader.readLine();
                switch (command) {
                    case "0":
                        return;
                    case "1":
                        addUserUi();
                        break;
                    case "2":
                        deleteUserUi();
                        break;
                    case "3":
                        updateUserUi();
                        break;
                    case "4":
                        findUserUi();
                        break;
                    case "5":
                        getAllUsersUI();
                        break;
                    case "6":
                        addFriendshipUi();
                        break;
                    case "7":
                        deleteFriendshipUi();
                        break;
                    case "8":
                        numberOfCommunitiesUi();
                        break;
                    case "9":
                        theMostSociableCommunityUi();
                        break;
                    case "10":
                        printAllFriendships();
                        break;
                    default:
                        System.out.println("Command not found!");
                }
            } catch (NumberFormatException numberFormatException) {
                System.out.println("Wrong input for number");
            } catch (ValidationException | IllegalArgumentException | ServiceException validationException) {
                System.out.println(validationException.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void close() throws Exception {
        inputReader.close();
    }
}

