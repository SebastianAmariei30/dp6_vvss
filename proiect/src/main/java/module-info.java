module tasks {
    exports tasks.model;
    exports tasks.services;
    exports tasks.controller;

    opens tasks.model to org.junit.platform.commons, org.junit.jupiter.api;
    opens tasks.services to org.junit.platform.commons, org.junit.jupiter.api;
    opens tasks.controller to org.junit.platform.commons, org.junit.jupiter.api;
}
