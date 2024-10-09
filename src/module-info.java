module DanceInstructor {
    requires javafx.controls;
    requires javafx.fxml;
    
    opens com.danceinstructor.view to javafx.fxml;
    exports com.danceinstructor.view;
}
