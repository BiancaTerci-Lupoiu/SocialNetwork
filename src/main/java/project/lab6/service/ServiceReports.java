package project.lab6.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import project.lab6.domain.entities.User;
import project.lab6.domain.entities.chat.Chat;
import project.lab6.domain.entities.chat.Message;
import project.lab6.domain.validators.ValidationException;
import project.lab6.repository.repointerface.Repository;
import project.lab6.repository.repointerface.RepositoryChat;
import project.lab6.utils.Constants;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceReports {

    private final RepositoryChat repoChats;
    private final Repository<Long, Message> repoMessages;
    private final Repository<Long, User> repoUsers;

    public ServiceReports(RepositoryChat repoChats, Repository<Long, Message> repoMessages, Repository<Long, User> repoUsers) {
        this.repoChats = repoChats;
        this.repoMessages = repoMessages;
        this.repoUsers = repoUsers;
    }

    private void validatePeriod(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate))
            throw new ValidationException("The period is not valid!");
    }

    private List<Message> getFriendMessagesReport(LocalDate startDate, LocalDate endDate,
                                                  Long idLoggedUser, Long idFriend) {
        validatePeriod(startDate, endDate);
        Chat chat = repoChats.getPrivateChatBetweenUsers(idLoggedUser, idFriend);
        if (chat == null)
            throw new ServiceException("You don't have any messages with this person!");
        List<Message> messages = new ArrayList<>();
        for (var message : repoMessages.findAll()) {
            if (!message.getIdChat().equals(chat.getId()))
                continue;
            messages.add(message);
        }
        return messages;
    }

    public void createFriendMessagesReport(String reportPath, LocalDate startDate, LocalDate endDate,
                                           Long idLoggedUser, Long idFriend) {
        var messages = getFriendMessagesReport(startDate, endDate, idLoggedUser, idFriend);
        var loggedUser = repoUsers.findOne(idLoggedUser);
        var friend = repoUsers.findOne(idFriend);
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream pageContentStream = new PDPageContentStream(document, page)) {
                pageContentStream.beginText();
                pageContentStream.setFont(PDType1Font.TIMES_ROMAN, 18);
                pageContentStream.newLineAtOffset(100, 700);
                pageContentStream.setLeading(18);

                pageContentStream.showText(String.format("Hello %s.",loggedUser.getFirstName()));
                pageContentStream.newLine();
                pageContentStream.showText(String.format("This is what %s send you",friend.getFirstName()));
                pageContentStream.newLine();
                pageContentStream.showText(String.format("from %s to %s.",startDate.format(Constants.DATE_FORMATTER),endDate.format(Constants.DATE_FORMATTER)));
                pageContentStream.newLine();
                pageContentStream.setLeading(15);
                for (var message : messages) {
                    pageContentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
                    pageContentStream.showText(message.getText());
                    pageContentStream.newLine();

                    pageContentStream.setFont(PDType1Font.TIMES_ROMAN, 8);
                    pageContentStream.showText(message.getDate().format(Constants.DATETIME_FORMATTER));
                    pageContentStream.newLine();
                    pageContentStream.newLine();
                }
                pageContentStream.endText();
            }
            document.save(reportPath);
        } catch (IOException e) {
            throw new ServiceException("The report could not be saved!");
        }
    }

    public void createFullActivityReport(String reportPath, LocalDate startDate, LocalDate endDate,
                                         Long idLoggedUser){

    }
}
