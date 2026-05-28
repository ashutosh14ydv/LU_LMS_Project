package com.lms.controller;

import com.lms.App;
import com.lms.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainLayoutController implements Initializable {

    @FXML
    private Label lblWelcome;
    @FXML
    private StackPane contentArea;
    
    @FXML
    private Button btnManageBooks;
    @FXML
    private Button btnManageMembers;
    @FXML
    private Button btnIssue;
    @FXML
    private Button btnReturn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (SessionManager.getCurrentUser() != null) {
            lblWelcome.setText("Welcome, " + SessionManager.getCurrentUser().getUsername());
            
            if ("STUDENT".equals(SessionManager.getCurrentUser().getRole())) {
                 btnManageBooks.setDisable(true);
                 btnManageMembers.setDisable(true);
                 // I'll also bind the Issue and Return button IDs in fxml next step to disable them.
                 btnIssue.setDisable(true);
                 btnReturn.setDisable(true);
            }
        }
        showDashboard();
    }

    private void loadView(String fxml) {
        try {
            Parent node = FXMLLoader.load(getClass().getResource("/com/lms/view/" + fxml + ".fxml"));
            contentArea.getChildren().removeAll();
            contentArea.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showDashboard() {
        loadView("dashboard");
    }

    @FXML
    public void showBooks() {
        loadView("books");
    }

    @FXML
    public void showMembers() {
        loadView("members");
    }

    @FXML
    public void showIssueBook() {
        loadView("issue_book");
    }

    @FXML
    public void showReturnBook() {
        loadView("return_book");
    }

    @FXML
    public void showHistory() {
        loadView("history");
    }

    @FXML
    public void handleLogout() throws IOException {
        SessionManager.logout();
        App.setRoot("login", "Login - LU-LMS");
    }
}
