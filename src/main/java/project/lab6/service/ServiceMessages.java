package project.lab6.service;

import javafx.scene.paint.Color;
import project.lab6.domain.TupleWithIdChatUser;
import project.lab6.domain.User;
import project.lab6.domain.chat.Chat;
import project.lab6.domain.chat.Message;
import project.lab6.domain.chat.UserChatInfo;
import project.lab6.domain.dtos.ChatDTO;
import project.lab6.domain.dtos.MessageDTO;
import project.lab6.domain.dtos.UserChatInfoDTO;
import project.lab6.domain.validators.Validator;
import project.lab6.repository.repointerface.Repository;
import project.lab6.repository.repointerface.RepositoryChat;
import project.lab6.repository.repointerface.RepositoryUser;
import project.lab6.utils.Constants;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

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

    public Chat getOrCreatePrivateChatBetweenUsers(Long idUser1, Long idUser2) {
        Chat chat = repoChats.getPrivateChatBetweenUsers(idUser1, idUser2);
        if (chat != null)
            return chat;
        chat = new Chat(null, Constants.DEFAULT_CHAT_COLOR, true);
        chat = repoChats.saveAndReturnChat(chat);
        User user1 = repoUsers.findOne(idUser1);
        User user2 = repoUsers.findOne(idUser2);
        if (user1 == null || user2 == null)
            throw new ServiceException("Users not found");
        UserChatInfo info1 = new UserChatInfo(chat.getId(), idUser1, createNickName(user1));
        UserChatInfo info2 = new UserChatInfo(chat.getId(), idUser2, createNickName(user2));
        if (repoUserChatInfo.save(info1) && repoUserChatInfo.save(info2))
            return chat;
        else {
            repoUserChatInfo.delete(info1.getId());
            repoUserChatInfo.delete(info2.getId());
            return null;
        }
    }

    private void saveMessage(Long idChat, Long idUserFrom, String text, LocalDateTime date, Long idMessageToReply) {
        Message message = new Message(text, date, idUserFrom, idChat, idMessageToReply);
        validatorMessage.validate(message);

        repoMessages.save(message);
    }

    public void replyToMessage(Long idUserFrom, Long idMessageToReply, String text, LocalDateTime date) {
        Message message = repoMessages.findOne(idMessageToReply);
        saveMessage(message.getIdChat(), idUserFrom, text, date, idMessageToReply);
    }

    public void sendMessageInChat(Long idChat, Long idUserFrom, String text, LocalDateTime date) {
        saveMessage(idChat, idUserFrom, text, date, null);
    }

    /**
     * Takes the message from a user in a group chat and replies to that messge to the user in private
     */
    public void replyInPrivate(Long idUserFrom, Long idMessageToReply, String text, LocalDateTime date) {
        Message message = repoMessages.findOne(idMessageToReply);
        Chat chatInWhichToReply = getOrCreatePrivateChatBetweenUsers(idUserFrom, message.getIdUserFrom());

        saveMessage(chatInWhichToReply.getId(), idUserFrom, text, date, idMessageToReply);
    }

    private List<UserChatInfoDTO> getUsersChatInfoDTO() {
        return repoUserChatInfo.findAll().stream()
                .map(userChatInfo ->
                        new UserChatInfoDTO(
                                repoUsers.findOne(userChatInfo.getIdUser()),
                                userChatInfo.getNickname()
                        )).toList();
    }

    private List<UserChatInfoDTO> getUserChatInfoDTOForChat(Long idChat) {
        return repoUserChatInfo.findAll().stream()
                .filter(userChatInfo -> userChatInfo.getIdChat().equals(idChat))
                .map(userChatInfo ->
                        new UserChatInfoDTO(
                                repoUsers.findOne(userChatInfo.getIdUser()),
                                userChatInfo.getNickname()
                        )).toList();
    }

    private List<MessageDTO> getMessagesSortedForChat(Long idChat) {
        return repoMessages.findAll().stream()
                .filter(message -> message.getIdChat().equals(idChat))
                .map(message ->
                {
                    System.out.println(message);
                    UserChatInfo from = repoUserChatInfo.findOne(new TupleWithIdChatUser(idChat, message.getIdUserFrom()));
                    User userFrom = repoUsers.findOne(from.getIdUser());
                    UserChatInfoDTO fromDTO = new UserChatInfoDTO(userFrom, from.getNickname());
                    Message repliedMessage = null;
                    if (message.getIdReplyMessage() != null)
                        repliedMessage = repoMessages.findOne(message.getIdReplyMessage());
                    return new MessageDTO(message.getId(),
                            message.getText(),
                            message.getDate(),
                            fromDTO,
                            repliedMessage);
                }).sorted(Comparator.comparing(MessageDTO::getDate))
                .toList();
    }

    public ChatDTO getChatDTO(Long idChat) {
        Chat chat = repoChats.findOne(idChat);
        ChatDTO chatDTO = ChatDTO.createChatDTO(chat.getName(), chat.getColor(), chat.isPrivateChat());

        getUserChatInfoDTOForChat(idChat)
                .forEach(chatDTO::addUserInfo);

        getMessagesSortedForChat(idChat)
                .forEach(chatDTO::addMessage);

        return chatDTO;
    }

    public List<ChatDTO> getChatsDTO(Long idLoggedUser) {
        return repoChats.findAll().stream()
                .map(chat -> getChatDTO(chat.getId()))
                .filter(chatDTO -> chatDTO.getUsersInfo().stream()
                        .anyMatch(userInfo -> userInfo.getUser().getId().equals(idLoggedUser)))
                .toList();
    }

    public ChatDTO createChatGroup(String name, List<Long> idUsers) {
        Chat chat = new Chat(name, Constants.DEFAULT_CHAT_COLOR, false);
        validatorChat.validate(chat);
        chat = repoChats.saveAndReturnChat(chat);
        Chat finalChat = chat;
        idUsers.stream().map(repoUsers::findOne).forEach(user ->
        {
            repoUserChatInfo.save(new UserChatInfo(finalChat.getId(), user.getId(),
                    createNickName(user)));
        });

        return getChatDTO(chat.getId());
    }

    public void changeChatColor(Long idChat, Color newColor) {
        Chat chat = repoChats.findOne(idChat);
        if (chat == null)
            throw new ServiceException("No chat with the specified id exists!");
        chat.setColor(newColor);
        if (!repoChats.update(chat))
            throw new ServiceException("Could not change the color of the chat!");
    }

    public void addUserToChat(Long idChat, Long idUser) {
        Chat chat = repoChats.findOne(idChat);
        if (chat == null)
            throw new ServiceException("The chat doesn't exists");
        if (chat.isPrivateChat())
            throw new ServiceException("You can't add a user to a private chat");
        User user = repoUsers.findOne(idUser);
        if (user == null)
            throw new ServiceException("The id doesn't belong to a user");
        UserChatInfo userChatInfo = new UserChatInfo(idChat, idUser, createNickName(user));
        validatorUserChatInfo.validate(userChatInfo);
        if (!repoUserChatInfo.save(userChatInfo))
            throw new ServiceException("The user could not by added to the chat");
    }

    public void removeUserFromChat(Long idChat, Long idUser) {
        Chat chat = repoChats.findOne(idChat);
        if (chat == null)
            throw new ServiceException("The chat doesn't exists");
        if (chat.isPrivateChat())
            throw new ServiceException("You can't remove a user from a private chat");
        if (!repoUserChatInfo.delete(new TupleWithIdChatUser(idChat, idUser)))
            throw new ServiceException("The user cannot be deleted from the chat!");
    }

    public void changeNickname(Long idChat, Long idUser, String newNickname) {
        UserChatInfo userChatInfo = new UserChatInfo(idChat, idUser, newNickname);
        if (!repoUserChatInfo.update(userChatInfo))
            throw new ServiceException("Could not update the user nickname!");
    }

    private String createNickName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }
}
