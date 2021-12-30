module project.lab6 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports project.lab6;
    exports project.lab6.controllers;
    opens project.lab6.controllers to javafx.fxml;
    exports project.lab6.factory;
    opens project.lab6.factory to javafx.fxml;
    exports project.lab6.service;
    exports project.lab6.repository.repointerface;
    exports project.lab6.domain;
    exports project.lab6.domain.chat;
    exports project.lab6.domain.validators;
    exports project.lab6.domain.dtos;
    exports project.lab6.repository.database;
}