package socialnetwork.service;

import socialnetwork.domain.Community;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.repository.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Service {
    private Repository<Long, User> repoUsers;
    private Repository<Tuple<Long, Long>, Friendship> repoFriendships;
    private Long idMax=0L;

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
     * based on friendships, adds the friends to each user
     */
    private void connectFriends() {
        for (Friendship friendship : repoFriendships.findAll()) {
            User user1 = repoUsers.findOne(friendship.getId().getLeft());
            User user2 = repoUsers.findOne(friendship.getId().getRight());
            user1.addFriend(user2);
            user2.addFriend(user1);
        }
    }

    /**
     *  finds the maximum value for the user ids
     * and sets the idMax data member to that value
     */
    private void setIdMax(){
        for(User user: repoUsers.findAll())
            if(user.getId()>idMax)
                idMax=user.getId();

    }

    /**
     * adds the user with the id,firstName,lastName
     *
     * @param firstName String
     * @param lastName  String
     * @return null- if the given entity is saved
     * otherwise returns the entity (id already exists)
     * @throws ValidationException      if the entity is not valid
     * @throws IllegalArgumentException if the given entity is null.
     */
    public boolean addUser(String firstName, String lastName) {
        User user = new User(firstName, lastName);
        user.setId(idMax+1);
        boolean result = repoUsers.save(user);
        if(result==true)
            idMax++;
        return result;
    }

    /**
     * @return all the users
     */
    public Map<Long,User> getAllUsers() {
        // trebuie sa completam si listele de prietenie
        Map<Long,User> usersWithFriends=new HashMap<>();
        // facem copie la prieteni pt a nu aparea duplicate in cazul repo in memory/file
        for(User user:repoUsers.findAll()) {
            User newUser=new User(user.getFirstName(), user.getLastName());
            newUser.setId(user.getId());
            usersWithFriends.put(newUser.getId(), newUser);
        }
        for(Friendship friendship:repoFriendships.findAll())
        {
            User user1=usersWithFriends.get(friendship.getId().getLeft());
            User user2=usersWithFriends.get(friendship.getId().getRight());
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
        User result = repoUsers.findOne(id);
        return result;
    }

    /**
     * @param id        Long
     * @param firstName String
     * @param lastName  String
     * @return null - if the entity is updated,
     * otherwise  returns the entity  - (e.g id does not exist).
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidationException      if the entity is not valid.
     */
    public boolean updateUser(Long id, String firstName, String lastName) {
        User updatedUser = new User(firstName, lastName);
        updatedUser.setId(id);
        boolean result = repoUsers.update(updatedUser);
        return result;
    }

    /**
     * deletes user with id
     *
     * @param id Long
     * @return the removed entity or null if there is no entity with the given id
     * @throws IllegalArgumentException if the given id is null.
     */
    public boolean deleteUser(Long id) {
        Map<Long,User> usersWithFriends=getAllUsers();
        boolean result = repoUsers.delete(id);
        if (result == true) {
            // s-a sters userul, trebuie sa stergem si prieteniile
            for(User user:usersWithFriends.get(id).getFriends())
                repoFriendships.delete(new Tuple<>(id, user.getId()));
            /*for (User user : result.getFriends()) {
                repoFriendships.delete(new Tuple<>(id, user.getId()));
                // il sterg pe userul cu id-ul id din din listele de prietenie ale prietenilor
                user.removeFriend(id);
            }*/

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
     * @return null- if the given entity is saved
     * otherwise returns the entity (id already exists)
     * @throws ValidationException      if the entity is not valid
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ServiceException         if the users does not exist
     */
    public boolean addFriendship(Long id1, Long id2) {
        //verifica daca exista userii

        User user1 = repoUsers.findOne(id1);
        User user2 = repoUsers.findOne(id2);
        if (user1 != null && user2 != null) {
            Friendship friendship = new Friendship(id1, id2);
            boolean result = repoFriendships.save(friendship);
            // daca se salveaza prietenia, adaugam prietenii in lista de prieteni
            /*if (result == null) {
                user1.addFriend(user2);
                user2.addFriend(user1);
            }*/
            return result;
        } else
            throw new ServiceException("users not found!");

    }

    /**
     * deletes the friendship between user1 and user2 if it exists
     *
     * @param id1 id user1
     * @param id2 id user2
     * @return the removed entity or null if there is no entity with the given id
     * @throws IllegalArgumentException if the given id is null.
     */
    public boolean deleteFriendship(Long id1, Long id2) {
        boolean result = repoFriendships.delete(new Tuple<>(id1, id2));
        /*if (result != null) {
            User user1 = repoUsers.findOne(id1);
            user1.removeFriend(id2);
            User user2 = repoUsers.findOne(id2);
            user2.removeFriend(id1);
        }*/
        return result;
    }

    /**
     * visits all the neighbours of currentNode and adds them to the community
     *
     * @param nodes       the list with nodes
     * @param currentNode the node at the current step
     * @param community   the current community
     */
    private void dfsVisit(Map<Long, Boolean> nodes, Long currentNode, Community community) {
        Map<Long,User> usersWithFriends=getAllUsers();
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

        Map<Long,User> usersWithFriends=getAllUsers();
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
        Integer longestPath = -1;
        for (Community community : allCommunities) {
            Integer pathDimension = community.findLongestPath();
            if (pathDimension > longestPath) {
                longestPath = pathDimension;
                sociableCommunity = community;
            }
        }
        //System.out.println(longestPath);
        return sociableCommunity;

    }

}
