module com.milacanete.messagesfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.google.gson;
    requires java.desktop;

    opens com.milacanete.messagesfx to javafx.fxml;
    exports com.milacanete.messagesfx;
    exports com.milacanete.messagesfx.models;
    opens com.milacanete.messagesfx.models to javafx.fxml, com.google.gson;
    exports com.milacanete.messagesfx.controllers;
    opens com.milacanete.messagesfx.controllers to javafx.fxml;

    opens com.milacanete.messagesfx.models.responses to com.google.gson;
    exports com.milacanete.messagesfx.models.responses;




}

