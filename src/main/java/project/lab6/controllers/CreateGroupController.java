package project.lab6.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import project.lab6.domain.User;
import project.lab6.domain.dtos.ChatDTO;
import project.lab6.service.ServiceFriends;
import project.lab6.service.ServiceMessages;
import project.lab6.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class CreateGroupController extends Controller {
    private ObservableList<UserRecord> usersRecord = FXCollections.observableArrayList();
    private final ServiceMessages serviceMessages;
    private final ServiceFriends serviceFriends;
    private final Long idLoggedUser;
    private final ObservableList<ChatDTO> observableList;
    private final MainChatController mainChatController;

    public CreateGroupController(ServiceMessages serviceMessages, ServiceFriends serviceFriends, Long idLoggedUser, ObservableList<ChatDTO> observableList, MainChatController mainChatController) {
        this.serviceMessages = serviceMessages;
        this.serviceFriends = serviceFriends;
        this.idLoggedUser = idLoggedUser;
        this.observableList = observableList;
        this.mainChatController = mainChatController;
    }

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
    private List<Long> participants=new ArrayList<>() ;

    /*todo trebuie setat numele chat-ului(constructor?)--done
     *  trebuie adaugat userul logat in grup pe langa userii pe care doreste sa ii adauge--done
     * todo fiecare user adaugat in grup trebuie sters din observable */
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
                    ChatDTO newChat = serviceMessages.createChatGroup(chatName, participants);
                    observableList.add(0, newChat);
                    mainChatController.setConversationView(newChat.getIdChat());
                    getStage().close();
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
            usersRecord.remove(new UserRecord(id,serviceFriends.getUserWithFriends(id).getLastName() + " "
                    + serviceFriends.getUserWithFriends(id).getFirstName(),addParticipantButton));

        });
        return addParticipantButton;
    }

    private List<UserRecord> getUserList(String searchName) {
        List<User> usersList = serviceFriends.searchUsersByName(serviceFriends.getUserWithFriends(idLoggedUser), searchName);
        List<UserRecord> userRecordObservableList = new ArrayList<>();
        for (User user : usersList) {
            if(!(participants.contains(user.getId())))
            {String name = user.getLastName() + " " + user.getFirstName();
            Button addParticipantButton = createAddParticipantsButton(user.getId());
            UserRecord userRecord = new UserRecord(user.getId(), name, addParticipantButton);

            userRecordObservableList.add(userRecord);}
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
}
