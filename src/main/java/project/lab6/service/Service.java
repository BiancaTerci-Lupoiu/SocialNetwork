package project.lab6.service;

import project.lab6.domain.*;
import project.lab6.domain.chat.Message;
import project.lab6.domain.validators.ValidationException;
import project.lab6.repository.Repository;
import project.lab6.repository.RepositoryUser;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Service {
    private final RepositoryUser repoUsers;
    private final Repository<Tuple<Long, Long>, Friendship> repoFriendships;
    private Long idMax = 0L;

    /**
     * @param repoUsers       the repository with users
     * @param repoFriendships the repository with friendships
     */
    public Service(RepositoryUser repoUsers, Repository<Tuple<Long, Long>, Friendship> repoFriendships) {
        this.repoUsers = repoUsers;
        this.repoFriendships = repoFriendships;
        //setIdMax();
    }

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
     * @param loggedUser     the logged user
     * @param name           string with a name
     *
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
     * finds the maximum value for the user ids
     * and sets the idMax data member to that value
     */
    private void setIdMax() {
        for (User user : repoUsers.findAll())
            if (user.getId() > idMax)
                idMax = user.getId();
    }

    /**
     * generates a random 32 characters long string
     *
     * @return the random string (32 chars)
     */
    private String generateSalt() {
        StringBuilder randomString = new StringBuilder(32);
        for (int i = 0; i < 32; i++) {
            int index = (int) (256 * Math.random());
            randomString.append((char) index);
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
     * @throws ValidationException      if the entity is not valid
     * @throws IllegalArgumentException if the given entity is null.
     */
    public boolean addUser(String email, String firstName, String lastName, String password) {
        String salt = generateSalt(); //generam un salt random pentru acest user
        String hashPassword = generateHashPassword(password, salt);
        User user = new User(idMax + 1, email, firstName, lastName, hashPassword, salt);
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
            return repoFriendships.save(new Friendship(id1, id2, date, status));
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
    private void dfsVisit(Map<Long, Boolean> nodes, Long currentNode, Community community) {
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

    /**
     <<<<<<< HEAD
     =======
     * sends a message from the user with id=idUserFrom to the users with ids from the idUsersTo list
     *
     * @param text       the text of the message
     * @param date       the date of the message
     * @param idUserFrom the id of the user who sent the message
     * @param idUsersTo  a list with ids of the recipients users
     * @return true if the message is sent, or false otherwise
     * @throws ServiceException         if the user with id=idUserFrom does not exist or,
     *                                  some user with id from the idUsersTo list does not exist
     * @throws ValidationException      if the message is not valid
     * @throws IllegalArgumentException if the given message is null.
     */
//    public boolean sendMessage(String text, LocalDateTime date, Long idUserFrom, List<Long> idUsersTo) {
//        return addMessage(text, date, idUserFrom, idUsersTo, null);
//    }

    /**
     * replies to an existent message
     *
     * @param text           the text of the message
     * @param date           the date of the message
     * @param idUserFrom     the id of the user who sent the message
     * @param idReplyMessage the id of the message the user wants to reply
     * @return
     * @throws ServiceException         if the user with id=idUserFrom does not exist or,
     *                                  some user with id from the idUsersTo list does not exist or,
     *                                  the message with id=idReplyMessage does not exist
     * @throws ValidationException      if the message is not valid
     * @throws IllegalArgumentException if the given message is null.
     */
//    public boolean replyMessage(String text, LocalDateTime date, Long idUserFrom, Long idReplyMessage) {
//        Message messageReply = repoMessages.findOne(idReplyMessage);
//        if (messageReply == null)
//            throw new ServiceException("The message you want to reply does not exist!\n");
//        Long idUserTo = messageReply.getIdUserFrom();
//        return addMessage(text, date, idUserFrom, List.of(idUserTo), idReplyMessage);
//    }

    /**
     * @param text           the text of the message
     * @param date           the date of the message
     * @param idUserFrom     the id of the user who sent the message
     * @param idUsersTo      a list with ids of the recipients users
     * @param idReplyMessage the id of the message the user wants to reply
     * @return true if the message is added, false otherwise
     * @throws ServiceException         if the user with id=idUserFrom does not exist or,
     *                                  some user with id from the idUsersTo list does not exist
     * @throws ValidationException      if the message is not valid
     * @throws IllegalArgumentException if the given message is null.
     */
//    private boolean addMessage(String text, LocalDateTime date, Long idUserFrom, List<Long> idUsersTo, Long idReplyMessage) {
//        if (repoUsers.findOne(idUserFrom) == null)
//            throw new ServiceException("The user that sends the message does not exist!\n");
//        String invalidUsersIds = "";
//        List<Long> validIdUsersTo = new ArrayList<>();
//        for (Long idUserTo : idUsersTo)
//            if (repoUsers.findOne(idUserTo) == null)
//                invalidUsersIds += idUserTo + " ";
//            else
//                validIdUsersTo.add(idUserTo);
//        if (validIdUsersTo.isEmpty())
//            throw new ServiceException("All the users from the recipients list do not exist!\n");
//        Message message = new Message(text, date, idUserFrom, validIdUsersTo);
//        message.setIdReplyMessage(idReplyMessage);
//        boolean result = repoMessages.save(message);
//        if (!invalidUsersIds.isEmpty())
//            throw new ServiceException("Some users from the recipients list does not exist: " + invalidUsersIds + "\n");
//        return result;
//    }

    /**
     >>>>>>> lab6
     * deletes the message with the specified id
     *
     * @param idMessage the id of the message to be deleted
     * @return true is the message is deleted, false otherwise
     * @throws IllegalArgumentException if the given idMessage is null.
     */
//    public boolean deleteMessage(Long idMessage) {
//        return repoMessages.delete(idMessage);
//    }

    /**
     * converts a Message object to a MessageDTO object
     *
     * @param message the message to be converted
     * @return the MessageDTO with the attributes of message
     */
//    private MessageDTO getMessageDTOFromMessage(Message message) {
//        User userFrom = repoUsers.findOne(message.getIdUserFrom());
//        Map<Long, User> listUsersTo = new HashMap<>();
//        for (Long idUser : message.getIdUsersTo()) {
//            User user = repoUsers.findOne(idUser);
//            listUsersTo.put(idUser, user);
//        }
//        MessageDTO replyMessage = null;
//        if (message.getIdReplyMessage() != null)
//            replyMessage = getMessageDTOFromMessage(repoMessages.findOne(message.getIdReplyMessage()));
//
//        MessageDTO messageDTO = new MessageDTO(message.getText(), message.getDate(), userFrom, replyMessage, listUsersTo);
//        messageDTO.setId(message.getId());
//        return messageDTO;
//
//    }

    /**
     * @return all the messages (a list of MessageDTO objects)
     */
//    public Iterable<MessageDTO> getAllMessages() {
//        Iterable<Message> messages = repoMessages.findAll();
//        List<MessageDTO> listMessagesDTO = new ArrayList<>();
//        for (Message message : messages) {
//            MessageDTO messageDTO = getMessageDTOFromMessage(message);
//            listMessagesDTO.add(messageDTO);
//        }
//        return listMessagesDTO;
//    }

    /**
     * Gets the conversation between the users with ids: idUser1,idUser2 in chronological order
     *
     * @param idUser1
     * @param idUser2
     * @return the conversation between the users with ids: idUser1 and idUser2
     */
//    public Conversation getConversation(Long idUser1, Long idUser2) {
//        List<MessageDTO> messagesList = new ArrayList<>();
//        for (MessageDTO message : getAllMessages()) {
//            if ((message.getUserFrom().getId().equals(idUser1) && message.getUserToById(idUser2) != null) || (message.getUserFrom().getId().equals(idUser2) && message.getUserToById(idUser1) != null))
//                messagesList.add(message);
//        }
//        messagesList = messagesList.stream().sorted((message1, message2) -> {
//            if (message1.getDate().isBefore(message2.getDate()))
//                return -1;
//            if (message1.getDate().equals(message2.getDate()))
//                return 0;
//            else return 1;
//        }).toList();
//        return new Conversation(messagesList);
//    }

    /**
     * @param idUser
     * @return the users that have open conversations with the user with id=idUser
     * @throws ServiceException if the user with id=idUser does not exist
     */
//    public Collection<User> getUsersThatHaveMessagesWithSomeUser(Long idUser) {
//        if (repoUsers.findOne(idUser) == null)
//            throw new ServiceException("The user with id=" + idUser + " does not exist\n");
//        Map<Long, User> users = new HashMap<>();
//        for (MessageDTO message : getAllMessages())
//            if (message.getUserFrom().getId().equals(idUser)) {
//                for (User user : message.getUsersTo())
//                    users.put(user.getId(), user);
//            } else if (message.getUserToById(idUser) != null)
//                users.put(message.getUserFrom().getId(), message.getUserFrom());
//        return users.values();
//    }
}
