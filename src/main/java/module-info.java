module DanceInstructor {
    requires transitive javafx.controls;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.swing;

    // AWS SDK v2 Modules
    requires software.amazon.awssdk.services.s3;
    requires software.amazon.awssdk.services.dynamodb;
    requires software.amazon.awssdk.services.lambda;
    requires software.amazon.awssdk.regions;
    requires software.amazon.awssdk.core;
    requires software.amazon.awssdk.auth;

    // Vision and Data
    requires opencv;
    requires java.sql;
    requires com.fasterxml.jackson.databind;
    requires io.github.cdimascio.dotenv.java;

    exports com.instructor.main;
    exports com.instructor.aws;
    exports com.instructor.controller;
}
