package project.lab6.service;

import project.lab6.domain.*;
import project.lab6.domain.entities.Friendship;
import project.lab6.domain.entities.User;
import project.lab6.domain.validators.ValidationException;
import project.lab6.repository.repointerface.Repository;
import project.lab6.repository.repointerface.RepositoryUser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ServiceFriends {
    private final RepositoryUser repoUsers;
    private final Repository<Tuple<Long, Long>, Friendship> repoFriendships;

    /**
     * @param repoUsers       the repository with users
     * @param repoFriendships the repository with friendships
     */
    public ServiceFriends(RepositoryUser repoUsers, Repository<Tuple<Long, Long>, Friendship> repoFriendships) {
        this.repoUsers = repoUsers;
        this.repoFriendships = repoFriendships;
    }

    /**
     * Based on a password and a salt creates a hashed password (for better security)
     *
     * @param password a Sting introduced by the user
     * @param salt     a 32 random char String
     * @return the hashed password
     */
    private String generateHashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(password.getBytes(StandardCharsets.UTF_8));
            digest.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] passwordBytes = digest.digest();
            return HexFormat.of().formatHex(passwordBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("Error at algorithm name!");
        }
    }

    /**
     * finds a user based on his email
     *
     * @param email
     * @return the user with the email =email, or null otherwise
     */
    public User findUserByEmail(String email) {
        return repoUsers.findByEmail(email);
    }

    /**
     * Checks if the user with email=email exists and if his password is correct. (log in the user)
     *
     * @param email
     * @param password
     * @return the user, if the user is logged in successfully, or null otherwise
     */
    public User loginUser(String email, String password) {
        User user = repoUsers.findByEmail(email);
        if (user == null)
            return null;
        if (generateHashPassword(password, user.getSalt()).equals(user.getHashPassword()))
            return user;
        return null;
    }

    /**
     * Returns a list with users whose name(last name + first name) matches the string name,
     * except the loggedUser, and they are not friends with the logged user
     *
     * @param loggedUser the logged user
     * @param name       string with a name
     * @return a list with users whose name(last name + first name) matches the string name
     */
    public List<User> searchUsersByNameNotFriendsWithLoggedUser(User loggedUser, String name) {
        String nameWithoutExtraSpaces = name.trim().replaceAll("[ ]+", " ").toLowerCase();
        List<User> usersWithName = StreamSupport.stream(repoUsers.findAll().spliterator(), false)
                .filter(user -> {
                    String lastNameFirstName = (user.getLastName() + " " + user.getFirstName()).toLowerCase();
                    String firstNameLastName = (user.getFirstName() + " " + user.getLastName()).toLowerCase();
                    return !user.getId().equals(loggedUser.getId()) &&
                            !loggedUser.findFriend(user.getId()) &&
                            (lastNameFirstName.startsWith(nameWithoutExtraSpaces)
                                    || firstNameLastName.startsWith(nameWithoutExtraSpaces));
                })
                .collect(Collectors.toList());
        return usersWithName;
    }

    /**
     * @param loggedUser
     * @param searchName
     * @return
     */
    public List<User> searchUsersByName(User loggedUser, String searchName) {
        String name = searchName.trim().replaceAll("[ ]+", " ").toLowerCase();
        List<User> usersWithName = StreamSupport.stream(repoUsers.findAll().spliterator(), false)
                .filter(user -> {
                    String lastNameFirstName = (user.getLastName() + " " + user.getFirstName()).toLowerCase();
                    String firstNameLastName = (user.getFirstName() + " " + user.getLastName()).toLowerCase();
                    return !user.getId().equals(loggedUser.getId()) &&
                            (lastNameFirstName.startsWith(name)
                                    || firstNameLastName.startsWith(name));
                })
                .collect(Collectors.toList());
        return usersWithName;

    }

    /**
     * generates a random 32 characters long string
     *
     * @return the random string (32 chars)
     */
    private String generateSalt() {
        StringBuilder randomString = new StringBuilder(32);
        for (int i = 0; i < 32; i++) {
            int character = (int) (255 * Math.random() + 1);
            randomString.append((char) character);
        }
        return randomString.toString();
    }

    /**
     * adds the user with the id,firstName,lastName
     *
     * @param firstName String
     * @param lastName  String
     * @return true- if the given entity is saved
     * otherwise returns false (id already exists)
     */
    public boolean addUser(String email, String firstName, String lastName, String password) {
        String salt = generateSalt(); //generam un salt random pentru acest user
        String hashPassword = generateHashPassword(password, salt);
        User user = new User(email, firstName, lastName, hashPassword, salt);
        user = repoUsers.save(user);
        return user != null;
    }

    /**
     * @return all the users
     */
    public Map<Long, User> getAllUsers() throws IOException {
        // trebuie sa completam si listele de prietenie
        Map<Long, User> usersWithFriends = new HashMap<>();
        // facem copie la prieteni pt a nu aparea duplicate in cazul repo in memory/file
        for (User user : repoUsers.findAll()) {
            User newUser = new User(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(),
                    user.getHashPassword(), user.getSalt());
            usersWithFriends.put(newUser.getId(), newUser);
        }
        for (Friendship friendship : repoFriendships.findAll()) {
            User user1 = usersWithFriends.get(friendship.getId().getLeft());
            User user2 = usersWithFriends.get(friendship.getId().getRight());
            user1.addFriend(new Friend(user2, friendship.getDate(), friendship.getStatus().toDirectedStatus(true)));
            user2.addFriend(new Friend(user1, friendship.getDate(), friendship.getStatus().toDirectedStatus(false)));
        }
        return usersWithFriends;
    }

    /**
     * @param id Long
     * @return the entity with the specified id
     * or null - if there is no entity with the given id
     * @throws IllegalArgumentException if id is null.
     */
    public User getUserWithFriends(Long id) {
        User user = repoUsers.findOne(id);
        if (user == null)
            throw new ServiceException("No user with id=" + id);
        StreamSupport.stream(repoFriendships.findAll().spliterator(), false)
                .filter(x -> x.getId().getLeft().equals(user.getId()))
                .map(x -> new Friend(repoUsers.findOne(x.getId().getRight()),
                        x.getDate(),
                        x.getStatus().toDirectedStatus(true)))
                .forEach(user::addFriend);

        StreamSupport.stream(repoFriendships.findAll().spliterator(), false)
                .filter(x -> x.getId().getRight().equals(user.getId()))
                .map(x -> new Friend(repoUsers.findOne(x.getId().getLeft()),
                        x.getDate(),
                        x.getStatus().toDirectedStatus(false)))
                .forEach(user::addFriend);
        return user;
    }

    /**
     * @param idUser id of the user
     * @return a collection of the friends of the specified idUser
     */
    public Collection<Friend> getFriends(Long idUser) {
        User user = repoUsers.findOne(idUser);
        if (user == null)
            throw new ServiceException("No user with id=" + idUser);
        return getUserWithFriends(idUser).getFriends(DirectedStatus.APPROVED);
    }

    /**
     * Returns a list with user's friends whose name(last name + first name) matches the string name
     *
     * @param idLoggedUser the id of the logged user
     * @param searchName       string with a name
     * @return a list with friends whose name(last name + first name) matches the string searchName
     */
    public List<Friend> searchFriendsByName(Long idLoggedUser, String searchName) {
        String name = searchName.trim().replaceAll("[ ]+", " ").toLowerCase();
        User user=getUserWithFriends(idLoggedUser);
        List<Friend> friendsWithName = StreamSupport.stream(user.getFriends().spliterator(), false)
                .filter(friend->friend.getStatus().equals(DirectedStatus.APPROVED))
                .filter(friend -> {
                    User userFriend=friend.getUser();
                    String lastNameFirstName = (userFriend.getLastName() + " " + userFriend.getFirstName()).toLowerCase();
                    String firstNameLastName = (userFriend.getFirstName() + " " + userFriend.getLastName()).toLowerCase();
                    return (lastNameFirstName.startsWith(name)
                                    || firstNameLastName.startsWith(name));
                })
                .collect(Collectors.toList());
        return friendsWithName;

    }

    /**
     * @param idUser
     * @param month
     * @return a list of friends of the given user for a specific month
     */
    public List<Friend> getFriendsMonth(Long idUser, Integer month) {
        User user = repoUsers.findOne(idUser);
        if (user == null)
            throw new ServiceException("No user with id=" + idUser);
        if (month > 12 || month < 1)
            throw new ServiceException("The given month is incorrect");
        return getUserWithFriends(idUser).getFriends(DirectedStatus.APPROVED)
                .stream()
                .filter(x -> x.getDate().getMonthValue() == month)
                .collect(Collectors.toList());
    }

    /**
     * @param id        Long
     * @param firstName String
     * @param lastName  String
     * @return true - if the entity is updated,
     * otherwise  returns false  - (e.g. id does not exist).
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidationException      if the entity is not valid.
     */
    public boolean updateUser(Long id, String firstName, String lastName) {
        User updatedUser = repoUsers.findOne(id);
        updatedUser.setId(id);
        updatedUser.setFirstName(firstName);
        updatedUser.setLastName(lastName);
        return repoUsers.update(updatedUser);
    }

    /**
     * deletes user with id
     *
     * @param id Long
     * @return true if the entity with id=id is removed or false if there is no entity with the given id
     * @throws IllegalArgumentException if the given id is null.
     */
    public boolean deleteUser(Long id) {
        for (Friend friend : getUserWithFriends(id).getFriends()) {
            repoFriendships.delete(new Tuple<>(id, friend.getUser().getId()));
            repoFriendships.delete(new Tuple<>(friend.getUser().getId(), id));
        }
        return repoUsers.delete(id);
    }

    /**
     * @return all Friendships
     */
    public Iterable<Friendship> getAllFriendships() {
        return repoFriendships.findAll();
    }

    /**
     * creates a friendship between user1 and user2
     *
     * @param id1 id user1
     * @param id2 id user2
     * @return true- if the given entity is saved
     * otherwise returns false (id already exists)
     * @throws ValidationException      if the entity is not valid
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ServiceException         if the users does not exist
     */
    public boolean addFriendship(Long id1, Long id2, LocalDate date, Status status) {
        //verifica daca exista userii
        Friendship friendship = repoFriendships.findOne(new Tuple<>(id2, id1));
        if (friendship != null)
            if (friendship.getStatus() == Status.APPROVED)
                throw new ServiceException("You are already friend with the other user");
            else
                throw new ServiceException("The other user already send you a invite");
        User user1 = repoUsers.findOne(id1);
        User user2 = repoUsers.findOne(id2);
        if (user1 != null && user2 != null) {
            return repoFriendships.save(new Friendship(id1, id2, date, status)) != null;
        } else
            throw new ServiceException("users not found!");
    }

    /**
     * deletes the friendship between user1 and user2 if it exists
     *
     * @param id1 id user1
     * @param id2 id user2
     * @return true if the friendship is deleted or false if there is no entity with the given id
     * @throws IllegalArgumentException if the given id is null.
     */
    public boolean deleteFriendship(Long id1, Long id2) {
        return repoFriendships.delete(new Tuple<>(id1, id2)) || repoFriendships.delete(new Tuple<>(id2, id1));
    }

    /**
     * visits all the neighbours of currentNode and adds them to the community
     *
     * @param nodes       the list with nodes
     * @param currentNode the node at the current step
     * @param community   the current community
     */
    private void dfsVisit(Map<Long, Boolean> nodes, Long currentNode, Community community) throws IOException {
        Map<Long, User> usersWithFriends = getAllUsers();
        nodes.put(currentNode, true);
        User currentUser = usersWithFriends.get(currentNode);
        for (var friend : currentUser.getFriends(DirectedStatus.APPROVED)) {
            var userFriend = friend.getUser();
            if (!nodes.get(userFriend.getId())) {
                community.addUser(userFriend);
                dfsVisit(nodes, userFriend.getId(), community);
            }
        }
    }

    /**
     * creates a list with all the communities (and the communities)
     *
     * @return a list with all the communities
     */
    private List<Community> findAllCommunities() throws IOException {
        // pastram toti userii si marcam daca i am vizitat sau nu
        Map<Long, Boolean> nodes = new HashMap<>();

        Map<Long, User> usersWithFriends = getAllUsers();
        List<Community> allCommunities = new ArrayList<>();
        for (User user : usersWithFriends.values()) {
            nodes.put(user.getId(), false);
        }
        for (Map.Entry<Long, Boolean> pair : nodes.entrySet())
            if (!pair.getValue()) {
                Community community = new Community();
                community.addUser(usersWithFriends.get(pair.getKey()));
                dfsVisit(nodes, pair.getKey(), community);
                allCommunities.add(community);
            }
        return allCommunities;
    }

    /**
     * @return the number of communities (int)
     */
    public int numberOfCommunities() throws IOException {
        return findAllCommunities().size();
    }

    /**
     * @return the most sociable community
     */
    public Community theMostSociableCommunity() throws IOException {
        List<Community> allCommunities = findAllCommunities();
        Community sociableCommunity = new Community();
        int longestPath = -1;
        for (Community community : allCommunities) {
            int pathDimension = community.findLongestPath();
            if (pathDimension > longestPath) {
                longestPath = pathDimension;
                sociableCommunity = community;
            }
        }
        return sociableCommunity;
    }

    /**
     * Modifies the given friend request
     *
     * @param idSender   The id of the user who sent the invitation
     * @param idReceiver The id of the user who received the invitation
     * @param newStatus  the new status of this friend request
     * @throws ServiceException if the operation could not be completed
     */
    public void modifyFriendRequestStatus(Long idSender, Long idReceiver, Status newStatus) {
        if (newStatus == Status.REJECTED) //if the new status is rejected, we will delete the friend request
        {
            if (!deleteFriendship(idSender, idReceiver))
                throw new ServiceException("No request from user with id=" + idSender);
            return;
        }

        var friendship = repoFriendships.findOne(new Tuple<>(idSender, idReceiver));
        if (friendship == null)
            throw new ServiceException("No request from user with id=" + idSender);
        friendship.setStatus(newStatus);
        if (!repoFriendships.update(friendship))
            throw new ServiceException("The request could not be modified");
    }
}
