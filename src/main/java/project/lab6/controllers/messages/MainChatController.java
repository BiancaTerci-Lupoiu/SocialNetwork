package project.lab6.controllers.messages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import project.lab6.controllers.Controller;
import project.lab6.controllers.HasTitleBar;
import project.lab6.domain.dtos.ChatDTO;
import project.lab6.domain.dtos.MessageDTO;
import project.lab6.domain.dtos.UserChatInfoDTO;
import project.lab6.factory.Factory;
import project.lab6.service.ServiceMessages;
import project.lab6.setter.SetterServiceMessages;
import project.lab6.utils.Constants;
import project.lab6.utils.observer.ObservableResource;
import project.lab6.utils.observer.ObserverWrapper;

import java.io.IOException;


public class MainChatController extends Controller implements SetterServiceMessages, HasTitleBar {
    private final Long idLoggedUser;
    @FXML
    public TextField searchChatTextField;
    @FXML
    public Button searchChatButton;
    @FXML
    public HBox mainHorizontalBox;
    @FXML
    public ListView<ChatDTO> listViewChats;
    ObservableList<ChatDTO> chatDTOList = FXCollections.observableArrayList();
    private ServiceMessages serviceMessages;


    public MainChatController(Long idLoggedUser) {
        this.idLoggedUser = idLoggedUser;
    }

    public ServiceMessages getServiceMessages() {
        return serviceMessages;
    }

    @Override
    public void setServiceMessages(ServiceMessages serviceMessages) {
        this.serviceMessages = serviceMessages;
    }

    @Override
    public String getViewPath() {
        return Constants.View.MAIN_CHAT;
    }

    public void createGroupAction() throws IOException {
        FXMLLoader loader = Factory.getInstance().getLoader(new CreateGroupController(idLoggedUser, chatDTOList, this));
        Scene scene = new Scene(loader.load(), 600, 430);
        scene.setFill(Color.TRANSPARENT);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void createPrivateChatAction() throws IOException {
        FXMLLoader loader = Factory.getInstance().getLoader(new OpenPrivateChatController(idLoggedUser, this, chatDTOList));
        Scene scene = new Scene(loader.load(), 600, 430);
        scene.setFill(Color.TRANSPARENT);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void initialize() {
        chatDTOList.setAll(serviceMessages.getChatsDTO(idLoggedUser));
        listViewChats.setItems(chatDTOList);
        listViewChats.setCellFactory(listView -> {
            ListCell<ChatDTO> cell = new CustomCellChat(idLoggedUser);
            cell.setOnMouseClicked(event -> {
                if (!cell.isEmpty()) {
                    setConversationView(cell.getItem().getIdChat());
                    event.consume();
                }
            });
            return cell;
        });
        System.out.println(chatDTOList.size());
        if (!chatDTOList.isEmpty())
            setConversationView(chatDTOList.get(0).getIdChat());
        searchChatTextField.textProperty().addListener((obs, oldText, newText) -> findChatByName());
    }

    private void updateListWithChatsOnSearch(String chatName) {
        chatDTOList.setAll(serviceMessages.findChatByName(idLoggedUser, chatName));
    }

    public void findChatByName() {
        updateListWithChatsOnSearch(searchChatTextField.getText());
    }

    public void setConversationView(Long idChat) {
        setConversationView(idChat, null);
    }

    public void setConversationView(Long idChat, MessageDTO messageToReply) {
        ObservableResource<ChatDTO> observableChatDTO = new ObservableResource<>(serviceMessages.getChatDTO(idChat));
        observableChatDTO.addObserver(ObserverWrapper.fromObservableList(chatDTOList));
        FXMLLoader loader = Factory.getInstance().getLoader(new ConversationController(observableChatDTO, idLoggedUser, this, messageToReply));
        Region region = null;
        try {
            region = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mainHorizontalBox.getChildren().size() > 1)
            mainHorizontalBox.getChildren().set(1, region);
        else
            mainHorizontalBox.getChildren().add(region);
        HBox.setHgrow(region, Priority.ALWAYS);
    }

    public static class CustomCellChat extends ListCell<ChatDTO> {
        HBox horizontalBox = new HBox();
        VBox nameAndMessageVBox = new VBox();
        Label lastMessage = new Label();
        Label chatName = new Label();
        Image groupImage;
        Circle circle = new Circle();
        ChatDTO chat;
        Long idLoggedUser;

        public CustomCellChat(Long idLoggedUser) {
            this.idLoggedUser = idLoggedUser;
            circle.setRadius(25);
            circle.setStroke(Color.web("#5c0e63"));
            circle.setStrokeWidth(2);
            chatName.setMaxWidth(120);
            chatName.setStyle("-fx-font-family: Cambria; -fx-background-color: transparent; -fx-font-size: 18;-fx-text-fill: #5c0e63");
            this.setStyle("-fx-background-color: #ccccff;-fx-border-color: transparent");
            lastMessage.setStyle("-fx-font-family: Cambria; -fx-background-color: transparent; -fx-font-size: 14;-fx-font-style: Italic;-fx-text-fill: #5c0e63");
            horizontalBox.getChildren().addAll(circle, nameAndMessageVBox);
            horizontalBox.setAlignment(Pos.CENTER_LEFT);
            nameAndMessageVBox.setAlignment(Pos.CENTER_LEFT);
            horizontalBox.setSpacing(10);
            lastMessage.setMaxWidth(110);
        }

        private void setLastMessage() {
            MessageDTO lastMessageDTO = chat.getLastMessage();
            if (lastMessageDTO != null) {
                UserChatInfoDTO userMessageFrom = lastMessageDTO.getUserFromInfo();
                String usersNameFrom = userMessageFrom.getNickname();
                if (userMessageFrom.getUser().getId().equals(idLoggedUser))
                    usersNameFrom = "You";
                String lastMessageText = usersNameFrom + ": " + lastMessageDTO.getText();
                lastMessage.setText(lastMessageText);
                nameAndMessageVBox.getChildren().setAll(chatName, lastMessage);
            } else
                nameAndMessageVBox.getChildren().setAll(chatName);
        }

        @Override
        protected void updateItem(ChatDTO item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                chat = null;
                setGraphic(null);
            } else {
                chat = item;
                chatName.setText(chat.getName(idLoggedUser));
                groupImage = item.getImage(idLoggedUser);
                circle.setFill(new ImagePattern(groupImage));
                setLastMessage();
                setGraphic(horizontalBox);
            }
        }
    }
}
