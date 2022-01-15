package project.lab6.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import project.lab6.domain.*;
import project.lab6.domain.entities.Friendship;
import project.lab6.domain.entities.chat.Chat;
import project.lab6.domain.entities.chat.Message;
import project.lab6.domain.entities.chat.UserChatInfo;
import project.lab6.domain.validators.ValidationException;
import project.lab6.repository.repointerface.Repository;
import project.lab6.repository.repointerface.RepositoryChat;
import project.lab6.repository.repointerface.RepositoryUser;
import project.lab6.utils.Constants;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServiceReports {
    private final RepositoryChat repoChats;
    private final Repository<Long, Message> repoMessages;
    private final RepositoryUser repoUsers;
    private final Repository<Tuple<Long, Long>, Friendship> repoFriendships;
    private final ServiceMessages serviceMessages;

    public ServiceReports(RepositoryChat repoChats, Repository<Long, Message> repoMessages, RepositoryUser repoUsers, Repository<Tuple<Long, Long>, Friendship> repoFriendships, Repository<TupleWithIdChatUser, UserChatInfo> repoUserInfo, ServiceMessages serviceMessages) {
        this.repoChats = repoChats;
        this.repoMessages = repoMessages;
        this.repoUsers = repoUsers;
        this.repoFriendships = repoFriendships;
        this.serviceMessages = serviceMessages;
    }

    /**
     * validates a period of time (startDate<endDate)
     *
     * @param startDate
     * @param endDate
     */
    private void validatePeriod(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) throw new ValidationException("The period is not valid!");
    }

    /**
     * validates the input parameters for the full activity report
     *
     * @param reportPath
     * @param startDate
     * @param endDate
     * @param idLoggedUser
     * @throws ServiceException if the input arguments are equal to null
     */
    private void validateFullActivityReport(String reportPath, LocalDate startDate, LocalDate endDate, Long idLoggedUser) {
        String errors = getErrorMessagesForInputArguments(reportPath, startDate, endDate, idLoggedUser);
        if (!errors.isEmpty()) throw new ServiceException(errors);
    }

    /**
     * validate the input parameters for the message report
     *
     * @param reportPath
     * @param startDate
     * @param endDate
     * @param idLoggedUser
     * @param idFriend
     * @throws ServiceException if the input arguments are equal to null
     */
    private void validateMessageReport(String reportPath, LocalDate startDate, LocalDate endDate, Long idLoggedUser, Long idFriend) {
        String errors = getErrorMessagesForInputArguments(reportPath, startDate, endDate, idLoggedUser);
        if (idFriend == null) errors += "invalid friend!\n";
        if (!errors.isEmpty()) throw new ServiceException(errors);
    }

    /**
     * @param reportPath
     * @param startDate
     * @param endDate
     * @param idLoggedUser
     * @return a string with all the errors: -reportPath-null
     * -startDate-null
     * -endDate-null
     * -idLoggedUser-null
     */
    private String getErrorMessagesForInputArguments(String reportPath, LocalDate startDate, LocalDate endDate, Long idLoggedUser) {
        String errors = "";
        if (reportPath == null) errors += "invalid file location!\n";
        if (startDate == null) errors += "invalid start date!\n";
        if (endDate == null) errors += "invalid end date!\n";
        if (idLoggedUser == null) errors += "invalid logged user!\n";
        return errors;
    }

    /**
     * @param startDate
     * @param endDate
     * @param idLoggedUser
     * @param idFriend
     * @return the list of messages the logged user received from the friend with the id=idFriend in the period of time startDate-endDate
     * @throws ServiceException if the period of time is invalid
     */
    private List<Message> getFriendMessagesReport(LocalDate startDate, LocalDate endDate, Long idLoggedUser, Long idFriend) {
        validatePeriod(startDate, endDate);
        Chat chat = repoChats.getPrivateChatBetweenUsers(idLoggedUser, idFriend);
        if (chat == null) throw new ServiceException("You don't have any messages with this person!");
        List<Message> messages = new ArrayList<>();
        for (var message : repoMessages.findAll()) {
            if (!message.getIdChat().equals(chat.getId())) continue;
            messages.add(message);
        }
        return messages;
    }

    /**
     * creates a pdf document with all the messages the logged user received from the friend with the id=idFriend in the period of time startDate-endDate
     *
     * @param reportPath
     * @param startDate
     * @param endDate
     * @param idLoggedUser
     * @param idFriend
     * @throws ServiceException if the report couldn't be saved
     */
    public void createFriendMessagesReport(String reportPath, LocalDate startDate, LocalDate endDate, Long idLoggedUser, Long idFriend) {
        validateMessageReport(reportPath, startDate, endDate, idLoggedUser, idFriend);
        var messages = getFriendMessagesReport(startDate, endDate, idLoggedUser, idFriend);
        var loggedUser = repoUsers.findOne(idLoggedUser);
        var friend = repoUsers.findOne(idFriend);
        try (PDDocument document = new PDDocument()) {
            int i = 0;
            while (i < messages.size()) {
                PDPage page = new PDPage();
                document.addPage(page);
                try (PDPageContentStream pageContentStream = new PDPageContentStream(document, page)) {
                    pageContentStream.beginText();
                    pageContentStream.setFont(PDType1Font.TIMES_ROMAN, 18);
                    pageContentStream.newLineAtOffset(100, 700);
                    pageContentStream.setLeading(18);
                    if (i == 0) {
                        pageContentStream.showText(String.format("Hello %s.", loggedUser.getFirstName()));
                        pageContentStream.newLine();
                        pageContentStream.showText(String.format("This is what %s send you", friend.getFirstName()));
                        pageContentStream.newLine();
                        pageContentStream.showText(String.format("from %s to %s.", startDate.format(Constants.DATE_FORMATTER), endDate.format(Constants.DATE_FORMATTER)));
                        pageContentStream.newLine();
                    }
                    pageContentStream.setLeading(15);
                    while (i < messages.size()) {
                        Message message = messages.get(i);
                        i++;
                        pageContentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
                        pageContentStream.showText(message.getText());
                        pageContentStream.newLine();

                        pageContentStream.setFont(PDType1Font.TIMES_ROMAN, 8);
                        pageContentStream.showText(message.getDate().format(Constants.DATETIME_FORMATTER));
                        pageContentStream.newLine();
                        pageContentStream.newLine();
                        if (i % 13 == 0) break;
                    }
                    pageContentStream.endText();

                }
            }

            document.save(reportPath);
        } catch (IOException e) {
            throw new ServiceException("The report could not be saved!");
        }
    }

    /**
     * @param idUserFor
     * @param startDate
     * @param endDate
     * @return a list with the friends that the user with id=idUserFor made in the period of time startDate-endDate
     */
    public List<Friend> getFriendsInPeriod(Long idUserFor, LocalDate startDate, LocalDate endDate) {
        var friendships = repoFriendships.findAll().stream().filter(x -> x.getId().getLeft().equals(idUserFor) || x.getId().getRight().equals(idUserFor)).filter(x -> x.getStatus() == Status.APPROVED).filter(x -> x.getDate().isAfter(startDate) && x.getDate().isBefore(endDate));

        return friendships.map(x -> {
            Long idUser = x.getId().getLeft();
            if (idUser.equals(idUserFor)) idUser = x.getId().getRight();
            return new Friend(repoUsers.findOne(idUser), x.getDate(), DirectedStatus.APPROVED);
        }).toList();
    }

    /**
     * creates a pdf document with all the messages the logged user received in the period of time startDate-endDate and all the friends made in the same period
     *
     * @param reportPath
     * @param startDate
     * @param endDate
     * @param idLoggedUser
     */
    public void createFullActivityReport(String reportPath, LocalDate startDate, LocalDate endDate, Long idLoggedUser) {
        validateFullActivityReport(reportPath, startDate, endDate, idLoggedUser);

        List<Friend> friends = getFriendsInPeriod(idLoggedUser, startDate, endDate);
        var chats = serviceMessages.getChatsDTO(idLoggedUser);
        try (PDDocument document = new PDDocument()) {
            PDPage pageFriends = new PDPage();
            document.addPage(pageFriends);

            //Writes the friends page
            try (PDPageContentStream pageContentStream = new PDPageContentStream(document, pageFriends)) {

                pageContentStream.beginText();
                pageContentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
                pageContentStream.newLineAtOffset(100, 700);
                pageContentStream.setLeading(12);
                pageContentStream.showText(String.format("In the period %s to %s",
                        startDate.format(Constants.DATE_FORMATTER),
                        endDate.format(Constants.DATE_FORMATTER)));
                pageContentStream.newLine();
                pageContentStream.showText("You made the following friends:");
                pageContentStream.newLine();

                for (var friend : friends) {
                    var friendUser = friend.getUser();
                    pageContentStream.showText(String.format("%s %s in %s",
                            friendUser.getLastName(), friendUser.getFirstName(), friend.getDate().format(Constants.DATE_FORMATTER)));
                    pageContentStream.newLine();
                }

                pageContentStream.endText();
            }


            for (int i = 0; i < chats.size(); i++) {
                PDPage pageMessages = new PDPage();
                document.addPage(pageMessages);
                var chat = chats.get(i);
                try (PDPageContentStream pageContentStream = new PDPageContentStream(document, pageMessages)) {
                    pageContentStream.beginText();
                    pageContentStream.setFont(PDType1Font.TIMES_ROMAN, 24);
                    pageContentStream.newLineAtOffset(100, 700);
                    pageContentStream.setLeading(30);
                    if (i == 0) {
                        pageContentStream.showText(String.format("In the period %s to %s",
                                startDate.format(Constants.DATE_FORMATTER),
                                endDate.format(Constants.DATE_FORMATTER)));
                        pageContentStream.newLine();
                        pageContentStream.showText("You received the folowing messages:");
                        pageContentStream.newLine();
                    }
                    String chatMessage;
                    if (chat.isPrivateChat())
                        chatMessage = "You received this messages in the chat with %s";
                    else
                        chatMessage = "You received this messages in the group chat %s";
                    pageContentStream.setFont(PDType1Font.TIMES_ROMAN, 18);
                    pageContentStream.setLeading(22);
                    pageContentStream.showText(String.format(chatMessage, chat.getName(idLoggedUser)));
                    pageContentStream.newLine();
                    pageContentStream.setLeading(12);

                    for (var message : chat.getMessages()) {
                        if (message.getUserFromInfo().getUser().getId().equals(idLoggedUser))
                            continue;
                        if (message.getDate().isBefore(startDate.atStartOfDay()) ||
                                message.getDate().isAfter(endDate.atStartOfDay()))
                            continue;
                        pageContentStream.setFont(PDType1Font.TIMES_ROMAN, 8);
                        pageContentStream.showText(message.getUserFromInfo().getNickname());
                        pageContentStream.newLine();

                        pageContentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
                        pageContentStream.showText(message.getText());
                        pageContentStream.newLine();

                        pageContentStream.setFont(PDType1Font.TIMES_ROMAN, 8);
                        pageContentStream.showText("          " + message.getDate().format(Constants.DATE_FORMATTER));
                        pageContentStream.newLine();
                    }
                    pageContentStream.endText();
                }
            }
            document.save(reportPath);
        } catch (IOException e) {
            throw new ServiceException("The report could not be saved!");
        }

    }
}
