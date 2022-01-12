package project.lab6.service;

import javafx.scene.paint.Color;
import project.lab6.domain.TupleWithIdChatUser;
import project.lab6.domain.dtos.ChatDTO;
import project.lab6.domain.dtos.MessageDTO;
import project.lab6.domain.dtos.UserChatInfoDTO;
import project.lab6.domain.entities.User;
import project.lab6.domain.entities.chat.Chat;
import project.lab6.domain.entities.chat.Message;
import project.lab6.domain.entities.chat.UserChatInfo;
import project.lab6.domain.validators.Validator;
import project.lab6.repository.repointerface.Repository;
import project.lab6.repository.repointerface.RepositoryChat;
import project.lab6.repository.repointerface.RepositoryUser;
import project.lab6.utils.Constants;
import project.lab6.utils.Lazy;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class ServiceMessages {
    private final RepositoryUser repoUsers;
    private final RepositoryChat repoChats;
    private final Validator<Chat> validatorChat;
    private final Repository<Long, Message> repoMessages;
    private final Validator<Message> validatorMessage;
    private final Repository<TupleWithIdChatUser, UserChatInfo> repoUserChatInfo;
    private final Validator<UserChatInfo> validatorUserChatInfo;

    public ServiceMessages(RepositoryUser repoUsers,
                           RepositoryChat repoChats, Validator<Chat> validatorChat,
                           Repository<Long, Message> repoMessages, Validator<Message> validatorMessage,
                           Repository<TupleWithIdChatUser, UserChatInfo> repoUserChatInfo, Validator<UserChatInfo> validatorUserChatInfo) {
        this.repoUsers = repoUsers;
        this.repoChats = repoChats;
        this.validatorChat = validatorChat;
        this.repoMessages = repoMessages;
        this.validatorMessage = validatorMessage;
        this.repoUserChatInfo = repoUserChatInfo;
        this.validatorUserChatInfo = validatorUserChatInfo;
    }

    /**
     * Returns the private chat between 2 users or if it doesn't exist creates a chat
     *
     * @param idUser1 the id of the first user
     * @param idUser2 the id of the second user
     * @return the chat returned or newly created
     */
    public Chat getOrCreatePrivateChatBetweenUsers(Long idUser1, Long idUser2) {
        Chat chat = repoChats.getPrivateChatBetweenUsers(idUser1, idUser2);
        if (chat != null)
            return chat;
        User user1 = repoUsers.findOne(idUser1);
        User user2 = repoUsers.findOne(idUser2);
        if (user1 == null || user2 == null)
            throw new ServiceException("Users not found");
        chat = new Chat(null, Constants.DEFAULT_CHAT_COLOR, true);
        chat = repoChats.save(chat);
        UserChatInfo info1 = new UserChatInfo(chat.getId(), idUser1, createNickname(user1));
        UserChatInfo info2 = new UserChatInfo(chat.getId(), idUser2, createNickname(user2));
        if (repoUserChatInfo.save(info1) != null && repoUserChatInfo.save(info2) != null)
            return chat;
        else {
            repoUserChatInfo.delete(info1.getId());
            repoUserChatInfo.delete(info2.getId());
            return null;
        }
    }

    /**
     * Saves a message in the database. It's a utility function
     *
     * @param idChat           the chat in which the message was sent
     * @param idUserFrom       the id of the user who send the message
     * @param text             the message text
     * @param date             the date when this message has sent
     * @param idMessageToReply the id of the message that this message replies to or null if it doesn't reply to any message
     */
    private void saveMessage(Long idChat, Long idUserFrom, String text, LocalDateTime date, Long idMessageToReply) {
        Message message = new Message(text, date, idUserFrom, idChat, idMessageToReply);
        validatorMessage.validate(message);

        repoMessages.save(message);
    }

    /**
     * Replies to a message
     *
     * @param idChat           the chat in which the message was sent
     * @param idUserFrom       the id of the user who send the message
     * @param idMessageToReply the id of the message that this message replies to
     * @param text             the message text
     * @param date             the date when this message has sent
     */
    public void replyToMessage(Long idChat, Long idUserFrom, Long idMessageToReply, String text, LocalDateTime date) {
        saveMessage(idChat, idUserFrom, text, date, idMessageToReply);
    }

    /**
     * Sends a message
     *
     * @param idChat     the chat in which the message was sent
     * @param idUserFrom the id of the user who send the message
     * @param text       the message text
     * @param date       the date when this message has sent
     */
    public void sendMessageInChat(Long idChat, Long idUserFrom, String text, LocalDateTime date) {
        saveMessage(idChat, idUserFrom, text, date, null);
    }


    private List<UserChatInfoDTO> getUsersChatInfoDTO() {
        return repoUserChatInfo.findAll().stream()
                .map(this::getUserChatInfoDTO)
                .toList();
    }

    private List<UserChatInfoDTO> getUserChatInfoDTOForChat(Long idChat) {
        return repoUserChatInfo.findAll().stream()
                .filter(userChatInfo -> userChatInfo.getIdChat().equals(idChat))
                .map(this::getUserChatInfoDTO)
                .toList();
    }

    private UserChatInfoDTO getUserChatInfoDTO(Long idChat, Long idUser) {
        return getUserChatInfoDTO(repoUserChatInfo.findOne(new TupleWithIdChatUser(idChat, idUser)));

    }

    private UserChatInfoDTO getUserChatInfoDTO(UserChatInfo info) {
        User userFrom = repoUsers.findOne(info.getIdUser());
        return new UserChatInfoDTO(info.getIdChat(), userFrom, info.getNickname());
    }


    private MessageDTO getMessageDTO(Message message) {
        if (message == null)
            return null;
        UserChatInfoDTO userFrom = getUserChatInfoDTO(message.getIdChat(), message.getIdUserFrom());
        Lazy<MessageDTO> repliedMessage = new Lazy<>(() -> getMessageDTO(message.getIdReplyMessage()));
        Chat chat = repoChats.findOne(message.getIdChat());
        return new MessageDTO(message.getId(), chat, message.getText(), message.getDate(), userFrom,
                repliedMessage);
    }

    private MessageDTO getMessageDTO(Long idMessage) {
        return getMessageDTO(repoMessages.findOne(idMessage));
    }

    private List<MessageDTO> getMessagesSortedForChat(Long idChat) {
        return repoMessages.findAll().stream()
                .filter(message -> message.getIdChat().equals(idChat))
                .map(message ->
                        getMessageDTO(message))
                .sorted(Comparator.comparing(MessageDTO::getDate))
                .toList();
    }

    /**
     * Creates a chatDTO from the specified id
     */
    public ChatDTO getChatDTO(Long idChat) {
        Chat chat = repoChats.findOne(idChat);
        return getChatDTO(chat);
    }

    /**
     * Creates a chatDTO from the specified chat
     */
    public ChatDTO getChatDTO(Chat chat)
    {
        Lazy<List<MessageDTO>> messages = new Lazy<>(() -> getMessagesSortedForChat(chat.getId()));
        Lazy<List<UserChatInfoDTO>> userInfos = new Lazy<>(() -> getUserChatInfoDTOForChat(chat.getId()));

        return ChatDTO.createChatDTO(chat.getId(), chat.getName(), chat.getColor(), chat.isPrivateChat(),
                messages, userInfos);
    }


    /**
     * Returns all the chat of the logged user
     *
     * @param idLoggedUser the id of the logged user
     */
    public List<ChatDTO> getChatsDTO(Long idLoggedUser) {
        return repoChats.findAll().stream()
                .map(chat -> getChatDTO(chat.getId()))
                .filter(chatDTO -> chatDTO.getUsersInfo().stream()
                        .anyMatch(userInfo -> userInfo.getUser().getId().equals(idLoggedUser)))
                .toList();
    }

    /**
     * Creates a Group Chat
     *
     * @param name    the name of the group
     * @param idUsers the id of the users who are initially in the group
     * @return The ChatDTO created
     */
    public ChatDTO createChatGroup(String name, List<Long> idUsers) {
        Chat chat = new Chat(name, Constants.DEFAULT_CHAT_COLOR, false);
        validatorChat.validate(chat);
        chat = repoChats.save(chat);
        Chat finalChat = chat;
        idUsers.stream().map(repoUsers::findOne).forEach(user ->
                repoUserChatInfo.save(new UserChatInfo(finalChat.getId(), user.getId(),
                        createNickname(user))));

        return getChatDTO(chat.getId());
    }

    /**
     * Changes the color of a chat
     *
     * @param idChat   the id of the chat
     * @param newColor the new color of the chat
     */
    public void changeChatColor(Long idChat, Color newColor) {
        Chat chat = repoChats.findOne(idChat);
        if (chat == null)
            throw new ServiceException("No chat with the specified id exists!");
        chat.setColor(newColor);
        if (!repoChats.update(chat))
            throw new ServiceException("Could not change the color of the chat!");
    }

    /**
     * Adds the specified user to the group chat
     *
     * @param idChat the group chat id
     * @param idUser the id of the user
     * @throws ServiceException If the chat doesn't exist or if the chat is a private one
     */
    public void addUserToChat(Long idChat, Long idUser) {
        Chat chat = repoChats.findOne(idChat);
        if (chat == null)
            throw new ServiceException("The chat doesn't exists");
        if (chat.isPrivateChat())
            throw new ServiceException("You can't add a user to a private chat");
        User user = repoUsers.findOne(idUser);
        if (user == null)
            throw new ServiceException("The id doesn't belong to a user");
        UserChatInfo userChatInfo = new UserChatInfo(idChat, idUser, createNickname(user));
        validatorUserChatInfo.validate(userChatInfo);
        if (repoUserChatInfo.save(userChatInfo) == null)
            throw new ServiceException("The user could not by added to the chat");
    }

    /**
     * Removes the specified user from the group chat
     *
     * @param idChat the group chat id
     * @param idUser the id of the user
     * @throws ServiceException If the chat doesn't exist or if the chat is a private one
     */
    public void removeUserFromChat(Long idChat, Long idUser) {
        Chat chat = repoChats.findOne(idChat);
        if (chat == null)
            throw new ServiceException("The chat doesn't exists");
        if (chat.isPrivateChat())
            throw new ServiceException("You can't remove a user from a private chat");
        if (!repoUserChatInfo.delete(new TupleWithIdChatUser(idChat, idUser)))
            throw new ServiceException("The user cannot be deleted from the chat!");
    }

    /**
     * Changes the nickname of a user in in chat
     *
     * @param idChat      the chat the user is into
     * @param idUser      the id of the user
     * @param newNickname the new nickname of the user
     */
    public void changeNickname(Long idChat, Long idUser, String newNickname) {
        UserChatInfo userChatInfo = new UserChatInfo(idChat, idUser, newNickname);
        validatorUserChatInfo.validate(userChatInfo);
        if (!repoUserChatInfo.update(userChatInfo))
            throw new ServiceException("Could not update the user nickname!");
    }

    /**
     * Creates a default nickname for the user
     */
    private String createNickname(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }

    /**
     * Filters the chat the user is into by the chatName
     *
     * @param idLoggedUser id of the logged user
     * @param chatName     the search pattern of the user
     * @return a list of all valid Chats
     */
    public List<ChatDTO> findChatByName(Long idLoggedUser, String chatName) {
        String chatNameWithoutExtraSpaces = chatName.trim().replaceAll("[ ]+", " ").toLowerCase();
        return getChatsDTO(idLoggedUser).stream().filter(chatDTO -> {
            String name = chatDTO.getName(idLoggedUser).toLowerCase();
            return name.startsWith(chatNameWithoutExtraSpaces);
        }).toList();
    }
}
