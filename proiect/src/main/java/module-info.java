module tasks {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires log4j;

    opens tasks.model to javafx.base,org.junit.platform.commons, org.junit.jupiter.api;
    exports tasks.model;
    opens tasks.view to javafx.fxml, org.junit.platform.commons, org.junit.jupiter.api;
    exports tasks.view;
    opens tasks.controller to javafx.fxml,org.junit.platform.commons, org.junit.jupiter.api;
    exports tasks.controller;

    opens tasks.services to org.junit.platform.commons, org.junit.jupiter.api;




}
