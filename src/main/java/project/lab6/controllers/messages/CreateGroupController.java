package project.lab6.controllers.messages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import project.lab6.controllers.AlertMessage;
import project.lab6.controllers.Controller;
import project.lab6.controllers.utils.UserRecord;
import project.lab6.domain.dtos.ChatDTO;
import project.lab6.domain.entities.User;
import project.lab6.domain.validators.ValidationException;
import project.lab6.service.ServiceException;
import project.lab6.service.ServiceFriends;
import project.lab6.service.ServiceMessages;
import project.lab6.setter.SetterServiceFriends;
import project.lab6.setter.SetterServiceMessages;
import project.lab6.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class CreateGroupController extends Controller implements SetterServiceMessages, SetterServiceFriends {
    private final ObservableList<UserRecord> usersRecord = FXCollections.observableArrayList();
    private ServiceMessages serviceMessages;
    private ServiceFriends serviceFriends;
    private final Long idLoggedUser;
    private final ObservableList<ChatDTO> observableList;
    private final MainChatController mainChatController;
    private final List<Long> participants = new ArrayList<>();
    @FXML
    private TableColumn<UserRecord, String> name;
    @FXML
    private TableColumn<UserRecord, Button> addButton;
    @FXML
    private TableView<UserRecord> addParticipantsTableView;
    @FXML
    private TextField userNameTextField;
    @FXML
    private TextField groupName;
    @FXML
    private Button done;

    public CreateGroupController(Long idLoggedUser, ObservableList<ChatDTO> observableList, MainChatController mainChatController) {
        this.idLoggedUser = idLoggedUser;
        this.observableList = observableList;
        this.mainChatController = mainChatController;
    }


    @FXML
    public void initialize() {
        addParticipantsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        name.setCellValueFactory(new PropertyValueFactory<UserRecord, String>("name"));
        addButton.setCellValueFactory(new PropertyValueFactory<UserRecord, Button>("addButton"));
        name.prefWidthProperty().bind(addParticipantsTableView.widthProperty().divide(2));
        addButton.prefWidthProperty().bind(addParticipantsTableView.widthProperty().divide(2));
        userNameTextField.textProperty().addListener((obs, oldText, newText) -> findUserByName());
        done.setOnAction(event -> {
                    participants.add(idLoggedUser);
                    String chatName = groupName.textProperty().getValue();
                    try {
                        ChatDTO newChat = serviceMessages.createChatGroup(chatName, participants);
                        observableList.add(0, newChat);
                        mainChatController.setConversationView(newChat.getIdChat());
                        getStage().close();
                    } catch (ServiceException | ValidationException ex) {
                        AlertMessage.showErrorMessage(ex.getMessage());
                        participants.remove(idLoggedUser);
                    }
                }
        );
        addParticipantsTableView.getStylesheets().add(CreateGroupController.class.getClassLoader().getResource("project/lab6/css/tableViewNoHorizontalScroll.css").toExternalForm());

        addParticipantsTableView.setItems(usersRecord);
        updateTableAtSearch("");
    }

    private Button createAddParticipantsButton(Long id) {
        Button addParticipantButton = new Button();
        addParticipantButton.setText("Add");
        addParticipantButton.setOnAction(event ->
        {
            participants.add(id);
            addParticipantButton.setText("Added");
            addParticipantButton.disabledProperty();
            usersRecord.remove(new UserRecord(id, serviceFriends.getUserWithFriends(id).getLastName() + " "
                    + serviceFriends.getUserWithFriends(id).getFirstName(), addParticipantButton));

        });
        return addParticipantButton;
    }

    private List<UserRecord> getUserList(String searchName) {
        List<User> usersList = serviceFriends.searchUsersByName(serviceFriends.getUserWithFriends(idLoggedUser), searchName);
        List<UserRecord> userRecordObservableList = new ArrayList<>();
        for (User user : usersList) {
            if (!(participants.contains(user.getId()))) {
                String name = user.getLastName() + " " + user.getFirstName();
                Button addParticipantButton = createAddParticipantsButton(user.getId());
                UserRecord userRecord = new UserRecord(user.getId(), name, addParticipantButton);

                userRecordObservableList.add(userRecord);
            }
        }
        return userRecordObservableList;
    }

    private void updateTableAtSearch(String searchName) {
        usersRecord.setAll(getUserList(searchName));
    }

    private void findUserByName() {
        updateTableAtSearch(userNameTextField.getText());
    }

    @Override
    public String getViewPath() {
        return Constants.View.CREATE_NEW_GROUP;
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
