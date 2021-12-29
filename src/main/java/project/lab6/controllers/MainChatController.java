package project.lab6.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import project.lab6.domain.dtos.ChatDTO;
import project.lab6.service.ServiceMessages;
import project.lab6.utils.Constants;


public class MainChatController extends Controller {
    public MainChatController(Long idLoggedUser, ServiceMessages serviceMessages) {
        this.idLoggedUser = idLoggedUser;
        this.serviceMessages = serviceMessages;
    }

    @Override
    public String getViewPath() {
        return Constants.View.MAIN_CHAT;
    }

    public static class CustomCellChat extends ListCell<ChatDTO> {
        HBox horizontalBox = new HBox();
        Label chatName = new Label();
        ImageView groupImage = new ImageView();
        ChatDTO chat;
        Long idLoggedUser;

        public CustomCellChat(Long idLoggedUser) {
            super();
            this.idLoggedUser = idLoggedUser;
            groupImage.setFitWidth(20);
            groupImage.setFitHeight(20);
            chatName.setStyle("-fx-font-family: Cambria; -fx-background-color: transparent; -fx-font-size: 14");
            this.setStyle("-fx-background-color: #ccccff;-fx-border-color: transparent");
            horizontalBox.getChildren().addAll(groupImage, chatName);
            horizontalBox.setAlignment(Pos.TOP_LEFT);
        }

        @Override
        protected void updateItem(ChatDTO item, boolean empty) {
            super.updateItem(item, empty);
            if(empty){
                chat=null;
                setGraphic(null);
            }else{
                chat=item;
                chatName.setText(chat.getName(idLoggedUser));
                groupImage.setImage(new Image("project/lab6/images/icon-chat-basic.png"));
                setGraphic(horizontalBox);
            }
        }
    }

    @FXML
    public Button createGroupButton;
    @FXML
    public TextField searchChatTextField;
    @FXML
    public Button searchChatButton;
    @FXML
    public HBox mainHorizontalBox;
    @FXML
    public ListView<ChatDTO> listViewChats;

    ObservableList<ChatDTO> chatDTOList = FXCollections.observableArrayList();
    private final ServiceMessages serviceMessages;
    private final Long idLoggedUser;

    public void initialize() {
        chatDTOList.setAll(serviceMessages.getChatsDTO(idLoggedUser));
        listViewChats.setItems(chatDTOList);
        /*listViewChats.setCellFactory(new Callback<ListView<ChatDTO>, ListCell<ChatDTO>>() {
            @Override
            public ListCell<ChatDTO> call(ListView<ChatDTO> param) {
                return new CustomCellChat(idLoggedUser);
            }
        });*/
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
    }

    /**
     * sets the id of the logged user to idLoggedUser
     *
     * @param idChat
     */

    public void setConversationView(Long idChat) {
//        FXMLLoader loader=Factory.getInstance().getLoader(new ConversationController());
//        Region region = null;
//        try {
//            region = loader.load();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        ConversationController conversationController=loader.getController();
//        conversationController.setIdChat(idChat);
//        if (mainHorizontalBox.getChildren().size() > 1)
//            mainHorizontalBox.getChildren().set(1, region);
//        else
//            mainHorizontalBox.getChildren().add(region);
//        HBox.setHgrow(region, Priority.ALWAYS);
    }
}
