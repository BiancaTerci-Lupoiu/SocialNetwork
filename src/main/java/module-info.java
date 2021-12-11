module project.lab6 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens project.lab6 to javafx.fxml;
    exports project.lab6;
    exports project.lab6.controllers;
    opens project.lab6.controllers to javafx.fxml;
    exports project.lab6.factory;
    opens project.lab6.factory to javafx.fxml;
}