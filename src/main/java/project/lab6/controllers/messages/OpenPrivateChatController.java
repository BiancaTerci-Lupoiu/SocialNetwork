package project.lab6.controllers.messages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import project.lab6.controllers.Controller;
import project.lab6.controllers.HasTitleBar;
import project.lab6.controllers.utils.UserRecord;
import project.lab6.domain.dtos.ChatDTO;
import project.lab6.domain.entities.User;
import project.lab6.domain.entities.chat.Chat;
import project.lab6.service.ServiceFriends;
import project.lab6.service.ServiceMessages;
import project.lab6.setter.SetterServiceFriends;
import project.lab6.setter.SetterServiceMessages;
import project.lab6.utils.Constants;

import java.util.List;

public class OpenPrivateChatController extends Controller implements SetterServiceFriends, SetterServiceMessages, HasTitleBar {
    private final ObservableList<UserRecord> usersRecord = FXCollections.observableArrayList();
    private final Long idLoggedUser;
    private final MainChatController mainChatController;
    private final ObservableList<ChatDTO> observableList;
    private ServiceFriends serviceFriends;
    private ServiceMessages serviceMessages;
    @FXML
    private TextField searchField;
    @FXML
    private TableColumn<UserRecord, String> name;
    @FXML
    private TableColumn<UserRecord, Button> addButton;
    @FXML
    private TableView<UserRecord> usersTable;

    public OpenPrivateChatController(Long idLoggedUser, MainChatController mainChatController, ObservableList<ChatDTO> observableList) {
        this.idLoggedUser = idLoggedUser;
        this.mainChatController = mainChatController;
        this.observableList = observableList;
    }

    @FXML
    public void initialize() {
        usersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        name.setCellValueFactory(new PropertyValueFactory<UserRecord, String>("name"));
        addButton.setCellValueFactory(new PropertyValueFactory<UserRecord, Button>("addButton"));
        name.prefWidthProperty().bind(usersTable.widthProperty().divide(2));
        addButton.prefWidthProperty().bind(usersTable.widthProperty().divide(2));
        searchField.textProperty().addListener((obs, oldText, newText) -> findUserByName());
        usersTable.getStylesheets().add(OpenPrivateChatController.class.getClassLoader().getResource("project/lab6/css/tableViewNoHorizontalScroll.css").toExternalForm());
        usersTable.setItems(usersRecord);
        updateTableAtSearch("");
    }

    private Button createOpenChatButton(Long id) {
        Button addParticipantButton = new Button();
        addParticipantButton.setText("Open Chat");
        addParticipantButton.setOnAction(event ->
        {
            Chat chat = serviceMessages.getOrCreatePrivateChatBetweenUsers(idLoggedUser, id);
            if (observableList.filtered(x -> x.getIdChat().equals(chat.getId())).isEmpty())
                observableList.add(0, serviceMessages.getChatDTO(chat.getId()));
            mainChatController.setConversationView(chat.getId());
            getStage().close();
        });
        return addParticipantButton;
    }

    private ObservableList<UserRecord> getUserList(String searchName) {
        List<User> usersList = serviceFriends.searchUsersByName(serviceFriends.getUserWithFriends(idLoggedUser), searchName);
        ObservableList<UserRecord> userRecordObservableList = FXCollections.observableArrayList();
        for (User user : usersList) {
            String name = user.getLastName() + " " + user.getFirstName();
            Button addParticipantButton = createOpenChatButton(user.getId());
            UserRecord userRecord = new UserRecord(user.getId(), name, addParticipantButton);

            userRecordObservableList.add(userRecord);
        }
        return userRecordObservableList;
    }

    private void updateTableAtSearch(String searchName) {
        usersTable.getItems().clear();
        usersRecord.setAll(getUserList(searchName));
    }

    private void findUserByName() {
        updateTableAtSearch(searchField.getText());
    }

    @Override
    public String getViewPath() {
        return Constants.View.OPEN_PRIVATE_CHAT;
    }

    @Override
    public void setServiceFriends(ServiceFriends serviceFriends) {
        this.serviceFriends = serviceFriends;
    }

    @Override
    public void setServiceMessages(ServiceMessages serviceMessages) {
        this.serviceMessages = serviceMessages;
    }
}
