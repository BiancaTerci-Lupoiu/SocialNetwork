package project.lab6.utils;

import javafx.scene.paint.Color;

import java.time.format.DateTimeFormatter;

/**
 * class for declared constants
 */
public class Constants {
    public static DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    public static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static Color DEFAULT_CHAT_COLOR = Color.MAGENTA;

    public static class View {
        public static String ADD_FRIENDS = "views/friends/addFriends.fxml";
        public static String FRIENDS = "views/friends/friends.fxml";
        public static String REQUESTS = "views/friends/requests.fxml";

        public static String CREATE_NEW_ACCOUNT = "views/login/createNewAccount.fxml";
        public static String LOGIN = "views/login/login.fxml";

        public static String ADD_GROUP_MEMBER = "views/messages/addGroupMember.fxml";
        public static String CHAT_DETAILS = "views/messages/chatDetails.fxml";
        public static String MAIN_CHAT = "views/messages/chatMain.fxml";
        public static String CONVERSATION = "views/messages/conversation.fxml";
        public static String CREATE_NEW_GROUP = "views/messages/createGroup.fxml";
        public static String OPEN_PRIVATE_CHAT = "views/messages/openPrivateChat.fxml";

        public static String MAIN_VIEW = "views/main-view.fxml";
        public static String PROFILE = "views/profile.fxml";

    }
}
