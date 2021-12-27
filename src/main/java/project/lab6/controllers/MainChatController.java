package project.lab6.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import project.lab6.domain.dtos.ChatDTO;
import project.lab6.factory.Factory;
import project.lab6.service.ServiceMessages;
import project.lab6.setter_interface.SetterIdLoggedUser;
import project.lab6.setter_interface.SetterServiceMessages;

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
    public ListView listViewChats;


    private ServiceMessages serviceMessages;
    private Long idLoggedUser;
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
}
