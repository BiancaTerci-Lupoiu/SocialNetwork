package project.lab6.service;

import javafx.scene.paint.Color;
import project.lab6.domain.Entity;
import project.lab6.domain.Tuple;
import project.lab6.domain.User;
import project.lab6.domain.chat.Chat;
import project.lab6.domain.chat.Message;
import project.lab6.domain.chat.UserChatInfo;
import project.lab6.domain.dtos.ChatDTO;
import project.lab6.domain.dtos.MessageDTO;
import project.lab6.domain.dtos.UserChatInfoDTO;
import project.lab6.domain.validators.Validator;
import project.lab6.repository.Repository;
import project.lab6.repository.RepositoryUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ServiceMessages {
    private final RepositoryUser repoUsers;
    private final Validator<User> validatorUser;
    private final Repository<Long, Chat> repoChats;
    private final Validator<Chat> validatorChat;
    private final Repository<Long, Message> repoMessages;
    private final Validator<Message> validatorMessage;
    private final Repository<Tuple<Long, Long>, UserChatInfo> repoUserChatInfo;
    private final Validator<UserChatInfo> validatorUserChatInfo;

    public ServiceMessages(RepositoryUser repoUsers, Validator<User> validatorUser,
                           Repository<Long, Chat> repoChats, Validator<Chat> validatorChat,
                           Repository<Long, Message> repoMessages, Validator<Message> validatorMessage,
                           Repository<Tuple<Long, Long>, UserChatInfo> repoUserChatInfo, Validator<UserChatInfo> validatorUserChatInfo) {
        this.repoUsers = repoUsers;
        this.validatorUser = validatorUser;
        this.repoChats = repoChats;
        this.validatorChat = validatorChat;
        this.repoMessages = repoMessages;
        this.validatorMessage = validatorMessage;
        this.repoUserChatInfo = repoUserChatInfo;
        this.validatorUserChatInfo = validatorUserChatInfo;
    }

    private Chat getOrCreatePrivateChatBetweenUsers(Long idUser1, Long idUser2) {
        return new Chat(1L, "", Color.ALICEBLUE, true);
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

    private List<MessageDTO> getMessagesSortedForChat(Long idChat)
    {
        //TODO: reply message nu e bine
        return repoMessages.findAll().stream()
                .filter(message -> message.getIdChat().equals(idChat))
                .map(message ->
                {
                    UserChatInfoDTO from = repoUserChatInfo.findOne(new Tuple<>(idChat,message.getIdUserFrom()))
                    return new MessageDTO(message.getId(),
                            message.getText(),
                            message.getDate(),
                            from,
                            null);
                });
    }

    public ChatDTO getChatDTO(Long idChat) {
        Chat chat = repoChats.findOne(idChat);

        ChatDTO chatDTO = new ChatDTO(chat.getName(), chat.getColor(), chat.isPrivateChat());

        getUserChatInfoDTOForChat(idChat)
                .forEach(chatDTO::addUserInfo);


        //TODO: add messages

        return null;
    }

//    public List<ChatDTO> getChatsDTO() {
//        ArrayList<ChatDTO> chats = new ArrayList<>();
//
//        var usersChatInfoMap = StreamSupport.stream(repoUserChatInfo.findAll().spliterator(), false)
//                .map(userChatInfo ->
//                {
//                    User user = repoUsers.findOne(userChatInfo.getIdUser());
//                    return new UserChatInfoDTO(user, userChatInfo.getNickname());
//                })
//                .collect(Collectors.groupingBy(UserChatInfoDTO::getIdChat));
//
//        var messagesMap = StreamSupport.stream(repoMessages.findAll().spliterator(), false)
//                        .collect(Collectors.groupingBy(Message::getIdChat));
//
//        repoChats.findAll().forEach(chat ->
//        {
//            ChatDTO chatDTO = new ChatDTO(chat.getName(), chat.getColor(), chat.isPrivateChat());
//            usersChatInfoMap.get(chat.getId()).forEach(userInfo ->
//            {
//                UserChatInfoDTO userInfoDTO =
//                        new UserChatInfoDTO(repoUsers.findOne(userInfo.getIdUser()),
//                                userInfo.getNickname());
//                chatDTO.addUserInfo(userInfoDTO);
//            });
//        });
//
//        return new ArrayList<>();
//    }
}
