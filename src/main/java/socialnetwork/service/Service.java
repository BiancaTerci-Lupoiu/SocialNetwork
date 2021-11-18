package socialnetwork.service;

import socialnetwork.domain.*;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.repository.Repository;
import socialnetwork.domain.Status;

import java.time.LocalDate;
import java.util.*;

public class Service {
    private final Repository<Long, User> repoUsers;
    private final Repository<Tuple<Long, Long>, Friendship> repoFriendships;
    private Long idMax = 0L;

    /**
     * @param repoUsers       the repository with users
     * @param repoFriendships the repository with friendships
     */
    public Service(Repository<Long, User> repoUsers, Repository<Tuple<Long, Long>, Friendship> repoFriendships) {
        this.repoUsers = repoUsers;
        this.repoFriendships = repoFriendships;
        //connectFriends();
        setIdMax();
    }

    /**
     * finds the maximum value for the user ids
     * and sets the idMax data member to that value
     */
    private void setIdMax() {
        for (User user : repoUsers.findAll())
            if (user.getId() > idMax)
                idMax = user.getId();

    }

    /**
     * adds the user with the id,firstName,lastName
     *
     * @param firstName String
     * @param lastName  String
     * @return true- if the given entity is saved
     * otherwise returns false (id already exists)
     * @throws ValidationException      if the entity is not valid
     * @throws IllegalArgumentException if the given entity is null.
     */
    public boolean addUser(String firstName, String lastName) {
        User user = new User(firstName, lastName);
        user.setId(idMax + 1);
        boolean result = repoUsers.save(user);
        if (result)
            idMax++;
        return result;
    }

    /**
     * @return all the users
     */
    public Map<Long, User> getAllUsers() {
        // trebuie sa completam si listele de prietenie
        Map<Long, User> usersWithFriends = new HashMap<>();
        // facem copie la prieteni pt a nu aparea duplicate in cazul repo in memory/file
        for (User user : repoUsers.findAll()) {
            User newUser = new User(user.getFirstName(), user.getLastName());
            newUser.setId(user.getId());
            usersWithFriends.put(newUser.getId(), newUser);
        }
        for (Friendship friendship : repoFriendships.findAll()) {
            User user1 = usersWithFriends.get(friendship.getId().getLeft());
            User user2 = usersWithFriends.get(friendship.getId().getRight());
            user1.addFriend(user2);
            user2.addFriend(user1);
        }
        return usersWithFriends;
    }

    /**
     * @param id Long
     * @return the entity with the specified id
     * or null - if there is no entity with the given id
     * @throws IllegalArgumentException if id is null.
     */
    public User findUser(Long id) {
        return repoUsers.findOne(id);
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
        User updatedUser = new User(firstName, lastName);
        updatedUser.setId(id);
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
        Map<Long, User> usersWithFriends = getAllUsers();
        boolean result = repoUsers.delete(id);
        if (result) {
            // s-a sters userul, trebuie sa stergem si prieteniile
            for (User user : usersWithFriends.get(id).getFriends())
                repoFriendships.delete(new Tuple<>(id, user.getId()));
        }
        return result;
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
        User user1 = repoUsers.findOne(id1);
        User user2 = repoUsers.findOne(id2);
        if (user1 != null && user2 != null) {
            Friendship friendship = new Friendship(id1, id2,date,status);
            return repoFriendships.save(friendship);
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
        return repoFriendships.delete(new Tuple<>(id1, id2));
    }

    /**
     * visits all the neighbours of currentNode and adds them to the community
     *
     * @param nodes       the list with nodes
     * @param currentNode the node at the current step
     * @param community   the current community
     */
    private void dfsVisit(Map<Long, Boolean> nodes, Long currentNode, Community community) {
        Map<Long, User> usersWithFriends = getAllUsers();
        nodes.put(currentNode, true);
        User currentUser = usersWithFriends.get(currentNode);
        for (User userFriend : currentUser.getFriends()) {
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
    private List<Community> findAllCommunities() {
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
    public int numberOfCommunities() {

        return findAllCommunities().size();
    }

    /**
     * @return the most sociable community
     */
    public Community theMostSociableCommunity() {
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
     *
     * @param idUser id of the user
     * @param status the status of the friendship
     * @returns a list of the friends of a specified user with the given status
     */
    public List<Friend> getFriends(Long idUser,Status status){
        List<Friend> friends=new ArrayList<>();
        for(Friendship friendship: getAllFriendships()) {
            if (Objects.equals(idUser, friendship.getId().getLeft()) && status == friendship.getStatus()) {
                friends.add(new Friend(findUser(friendship.getId().getRight()), friendship.getDate(), friendship.getStatus()));
            }
            if (Objects.equals(friendship.getId().getRight(), idUser) && status == friendship.getStatus()){
                friends.add(new Friend(findUser(friendship.getId().getLeft()), friendship.getDate(), friendship.getStatus()));
            }
        }
        return friends;
    }

}
