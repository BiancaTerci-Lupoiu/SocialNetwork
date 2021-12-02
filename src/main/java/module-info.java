module project.lab6 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens project.lab6 to javafx.fxml;
    exports project.lab6;
}