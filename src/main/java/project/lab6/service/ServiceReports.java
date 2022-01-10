package project.lab6.service;

import project.lab6.domain.entities.chat.Chat;
import project.lab6.domain.entities.chat.Message;
import project.lab6.domain.validators.ValidationException;
import project.lab6.repository.repointerface.Repository;
import project.lab6.repository.repointerface.RepositoryChat;

import java.time.LocalDateTime;

public class ServiceReports {

    private RepositoryChat repoChats;
    private Repository<Long, Message> repoMessages;
    private void validatePeriod(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate))
            throw new ValidationException("The period is not valid!");
    }

    private boolean isInChat(Long idChat, Long idUser)
    {
        return false;
    }

    public void createFriendMessagesReport(String reportPath, LocalDateTime startDate, LocalDateTime endDate,
                                           Long idLoggedUser, Long idFriend) {
        validatePeriod(startDate, endDate);
        Chat chat = repoChats.getPrivateChatBetweenUsers(idLoggedUser, idFriend);
        //for(var message : )
    }
}
