package project.lab6.controllers.messages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import project.lab6.controllers.Controller;
import project.lab6.domain.dtos.ChatDTO;
import project.lab6.domain.dtos.MessageDTO;
import project.lab6.factory.Factory;
import project.lab6.service.ServiceMessages;
import project.lab6.setter.SetterServiceMessages;
import project.lab6.utils.Constants;
import project.lab6.utils.observer.ObservableResource;
import project.lab6.utils.observer.ObserverWrapper;

import java.io.IOException;


public class MainChatController extends Controller implements SetterServiceMessages {
    private ServiceMessages serviceMessages;
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


    public MainChatController(Long idLoggedUser) {
        this.idLoggedUser = idLoggedUser;
    }

    public ServiceMessages getServiceMessages() {
        return serviceMessages;
    }

    @Override
    public String getViewPath() {
        return Constants.View.MAIN_CHAT;
    }

    public void createGroupAction() throws IOException {
        FXMLLoader loader = Factory.getInstance().getLoader(new CreateGroupController(idLoggedUser, chatDTOList, this));
        Scene scene = new Scene(loader.load(), 600, 400);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void createPrivateChatAction() throws IOException {
        FXMLLoader loader = Factory.getInstance().getLoader(new OpenPrivateChatController(idLoggedUser, this, chatDTOList));
        Scene scene = new Scene(loader.load(), 600, 400);
        Stage stage = new Stage();
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

    @Override
    public void setServiceMessages(ServiceMessages serviceMessages) {
        this.serviceMessages = serviceMessages;
    }

    public static class CustomCellChat extends ListCell<ChatDTO> {
        HBox horizontalBox = new HBox();
        Label chatName = new Label();
        ImageView groupImage = new ImageView();
        ChatDTO chat;
        Long idLoggedUser;

        public CustomCellChat(Long idLoggedUser) {
            this.idLoggedUser = idLoggedUser;
            groupImage.setFitWidth(24);
            groupImage.setFitHeight(24);
            chatName.setStyle("-fx-font-family: Cambria; -fx-background-color: transparent; -fx-font-size: 16");
            this.setStyle("-fx-background-color: #ccccff;-fx-border-color: transparent");
            horizontalBox.getChildren().addAll(groupImage, chatName);
            horizontalBox.setAlignment(Pos.TOP_LEFT);
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
                //groupImage.setImage(new Image("project/lab6/images/icon-chat-basic.png"));
                groupImage.setImage(item.getImage(idLoggedUser));
                setGraphic(horizontalBox);
            }
        }
    }
}
