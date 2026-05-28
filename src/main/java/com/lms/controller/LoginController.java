package com.lms.controller;

import com.lms.App;
import com.lms.dao.UserDAO;
import com.lms.model.User;
import com.lms.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label lblMessage;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void handleLogin() {
        String userStr = txtUsername.getText().trim();
        String passStr = txtPassword.getText().trim();

        if (userStr.isEmpty() || passStr.isEmpty()) {
            lblMessage.setText("Please fill in all fields.");
            return;
        }

        try {
            User user = userDAO.authenticate(userStr, passStr);
            if (user != null) {
                SessionManager.setCurrentUser(user);
                // Switch to main layout
                App.setRoot("main_layout", "Dashboard - LU-LMS");
            } else {
                lblMessage.setText("Invalid username or password.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("Database connection error.");
        }
    }

    @FXML
    private void goToRegister() throws java.io.IOException {
        App.setRoot("register", "Create Account - LU-LMS");
    }
}
