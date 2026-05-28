package com.lms;

import com.lms.util.DatabaseUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        DatabaseUtil.initializeDatabase(); // Setup DB tables
        setRoot("login", "Library Login - Lucknow University");
    }

    public static void setRoot(String fxml, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/lms/view/" + fxml + ".fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        
        // Apply default CSS globally
        String css = App.class.getResource("/com/lms/css/style.css").toExternalForm();
        scene.getStylesheets().add(css);

        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
    
    public static void setContent(Parent content) {
        // helper for dynamic content loading if needed
    }

    public static void main(String[] args) {
        launch();
    }
}
