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

    public static class View
    {
        public static String ADD_FRIENDS="views/addFriends.fxml";
        public static String CREATE_NEW_ACCOUNT="views/createNewAccount.fxml";
        public static String FRIENDS = "views/friends.fxml";
        public static String LOGIN = "views/login.fxml";
        public static String MAIN_VIEW="views/main-view.fxml";
        public static String REQUESTS="views/requests.fxml";
        public static String PROFILE="views/profile.fxml";
        public static String MAIN_CHAT="views/chatMain.fxml";
        public static String CONVERSATION="views/conversation.fxml";
        public static String CREATE_NEW_GROUP="views/createGroup.fxml";
        public static String ADD_GROUP_MEMBER="views/addGroupMember.fxml";
    }
}
