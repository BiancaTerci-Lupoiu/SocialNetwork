package project.lab6.utils;

import javafx.scene.paint.Color;

import java.time.format.DateTimeFormatter;

/**
 * class for declared constants
 */
public class Constants {

    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final Color DEFAULT_CHAT_COLOR = Color.PLUM;
    public static final String PATH_DEFAULT_USER_IMAGE = "project/lab6/images/person-icon.png";
    public static final String PATH_DEFAULT_GROUP_CHAT_IMAGE = "project/lab6/images/group-chat-default.png";
    public static final String PATH_USER_IMAGES = "data/users";
    public static final String PATH_GROUP_CHAT_IMAGES = "data/chats";

    public static class View {
        public static final String ADD_FRIENDS = "views/friends/addFriends.fxml";
        public static final String FRIENDS = "views/friends/friends.fxml";
        public static final String REQUESTS = "views/friends/requests.fxml";

        public static final String CREATE_NEW_ACCOUNT = "views/login/createNewAccount.fxml";
        public static final String LOGIN = "views/login/login.fxml";

        public static final String ADD_GROUP_MEMBER = "views/messages/addGroupMember.fxml";
        public static final String CHAT_DETAILS = "views/messages/chatDetails.fxml";
        public static final String MAIN_CHAT = "views/messages/chatMain.fxml";
        public static final String CONVERSATION = "views/messages/conversation.fxml";
        public static final String CREATE_NEW_GROUP = "views/messages/createGroup.fxml";
        public static final String OPEN_PRIVATE_CHAT = "views/messages/openPrivateChat.fxml";

        public static final String MAIN_VIEW = "views/main-view.fxml";
        public static final String PROFILE = "views/profile.fxml";

        public static String CREATE_EVENT = "views/events/createEvent.fxml";
        public static String EVENTS = "views/events/events.fxml";
        public static String NOTIFICATIONS = "views/events/notifications.fxml";

        public static String ACTIVITY_REPORT = "views/reports/activityReport.fxml";
    }
}
