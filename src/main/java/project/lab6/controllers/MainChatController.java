package project.lab6.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.util.Callback;
import project.lab6.domain.chat.Chat;
import project.lab6.domain.dtos.ChatDTO;
import project.lab6.factory.Factory;
import project.lab6.service.ServiceMessages;
import project.lab6.setter_interface.SetterIdLoggedUser;
import project.lab6.setter_interface.SetterServiceMessages;
import project.lab6.utils.Constants;

import java.io.IOException;


public class MainChatController implements SetterServiceMessages, SetterIdLoggedUser {

    public static class CustomCellChat extends ListCell<ChatDTO> {
        HBox horizontalBox=new HBox();
        Label chatName=new Label();
        ImageView groupImage=new ImageView();
        ChatDTO chat;
        Long idLoggedUser;

        public CustomCellChat(Long idLoggedUser){
            super();
            this.idLoggedUser=idLoggedUser;
            horizontalBox.getChildren().addAll(groupImage,chatName);
            horizontalBox.setAlignment(Pos.TOP_RIGHT);
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

    ObservableList<ChatDTO> chatDTOList= FXCollections.observableArrayList();
    private ServiceMessages serviceMessages;
    private Long idLoggedUser;

    public void initialize(){
        chatDTOList.setAll(serviceMessages.getChatsDTO(idLoggedUser));
        listViewChats.setItems(chatDTOList);
        /*listViewChats.setCellFactory(new Callback<ListView<ChatDTO>, ListCell<ChatDTO>>() {
            @Override
            public ListCell<ChatDTO> call(ListView<ChatDTO> param) {
                return new CustomCellChat(idLoggedUser);
            }
        });*/
        listViewChats.setCellFactory(listView->{
            ListCell<ChatDTO> cell=new CustomCellChat(idLoggedUser);
            cell.setOnMouseClicked(event -> {
                if(!cell.isEmpty()){
                    setConversationView(cell.getItem().getIdChat());
                    event.consume();
                }
            });
            return cell;
        });
        setConversationView(chatDTOList.get(0).getIdChat());
    }

    /**
     * sets the id of the logged user to idLoggedUser
     *
     * @param idLoggedUser
     */
    @Override
    public void setIdLoggedUser(Long idLoggedUser) {
        this.idLoggedUser=idLoggedUser;
    }

    @Override
    public void setServiceMessages(ServiceMessages serviceMessages) {
        this.serviceMessages=serviceMessages;
    }

    public void setConversationView(Long idChat){
        FXMLLoader loader=Factory.getInstance().getLoader(Constants.View.CONVERSATION);
        ConversationController conversationController=loader.getController();
        conversationController.setIdChat(idChat);

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
}