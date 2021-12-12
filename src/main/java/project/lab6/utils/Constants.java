package project.lab6.utils;

import javafx.scene.paint.Color;

import java.time.format.DateTimeFormatter;

public class Constants {
    public static DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    public static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static Color DEFAULT_CHAT_COLOR = Color.MAGENTA;
}
