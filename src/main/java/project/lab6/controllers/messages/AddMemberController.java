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
import project.lab6.controllers.utils.UserRecord;
import project.lab6.domain.User;
import project.lab6.service.ServiceFriends;
import project.lab6.service.ServiceMessages;
import project.lab6.utils.Constants;
import project.lab6.utils.observer.ObservableChatDTO;

import java.util.ArrayList;
import java.util.List;

public class AddMemberController extends Controller {
    private ObservableList<UserRecord> usersRecord = FXCollections.observableArrayList();
    private final ServiceFriends serviceFriends;
    private final ServiceMessages serviceMessages;
    private final ObservableChatDTO observableChatDTO;
    private final Long idLoggedUser;

    public AddMemberController(ServiceMessages serviceMessages, ServiceFriends serviceFriends, Long idLoggedUser, ObservableChatDTO observableChatDTO) {
        this.serviceMessages = serviceMessages;
        this.serviceFriends = serviceFriends;
        this.idLoggedUser = idLoggedUser;
        this.observableChatDTO = observableChatDTO;
    }

    @FXML
    private TextField searchField;
    @FXML
    private TableColumn<UserRecord, String> name;
    @FXML
    private TableColumn<UserRecord, Button> addButton;
    @FXML
    private TableView<UserRecord> addMembersTableView;
    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        addMembersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        name.setCellValueFactory(new PropertyValueFactory<UserRecord, String>("name"));
        addButton.setCellValueFactory(new PropertyValueFactory<UserRecord, Button>("addButton"));
        name.prefWidthProperty().bind(addMembersTableView.widthProperty().divide(2));
        addButton.prefWidthProperty().bind(addMembersTableView.widthProperty().divide(2));
        searchField.textProperty().addListener((obs, oldText, newText) -> findUserByName());
        addMembersTableView.setItems(usersRecord);
        updateTableAtSearch("");
        backButton.setOnAction(event->getStage().close());

    }

    private Button createAddParticipantsButton(Long id) {
        Button addParticipantButton = new Button();
        addParticipantButton.setText("Add");
        addParticipantButton.setOnAction(event ->
        {
            serviceMessages.addUserToChat(observableChatDTO.getChat().getIdChat(), id);
            addParticipantButton.setText("Added");
            addParticipantButton.disabledProperty();
            usersRecord.remove(new UserRecord(id, serviceFriends.getUserWithFriends(id).getLastName() + " "
                    + serviceFriends.getUserWithFriends(id).getFirstName(), addParticipantButton));
            Long idChat = observableChatDTO.getChat().getIdChat();
            observableChatDTO.setChat(serviceMessages.getChatDTO(idChat));
        });
        return addParticipantButton;
    }

    private List<UserRecord> getUserList(String searchName) {
        List<User> usersList = serviceFriends.searchUsersByName(serviceFriends.getUserWithFriends(idLoggedUser), searchName);
        List<UserRecord> userRecordObservableList = new ArrayList<>();
        Long idChat = observableChatDTO.getChat().getIdChat();
        for (User user : usersList) {
            if (!(serviceMessages.getChatDTO(idChat).getUsersInfo().stream().map(x->x.getUser().getId()).toList().contains(user.getId()))) {
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
        updateTableAtSearch(searchField.getText());
    }

    @Override
    public String getViewPath() {
        return Constants.View.ADD_GROUP_MEMBER;
    }
}
