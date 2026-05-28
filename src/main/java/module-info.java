module com.lms {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.lms to javafx.fxml;
    opens com.lms.controller to javafx.fxml;
    opens com.lms.model to javafx.base;
    
    exports com.lms;
}
