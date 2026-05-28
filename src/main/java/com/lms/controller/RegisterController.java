package com.lms.controller;

import com.lms.App;
import com.lms.dao.MemberDAO;
import com.lms.dao.UserDAO;
import com.lms.model.Member;
import com.lms.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML private TextField txtUsername, txtFullName, txtRollNo;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<String> comboRole;
    @FXML private Label lblMsg;

    private final UserDAO userDAO = new UserDAO();
    private final MemberDAO memberDAO = new MemberDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboRole.setItems(FXCollections.observableArrayList("STUDENT", "LIBRARIAN"));
        comboRole.getSelectionModel().selectFirst(); // Default to Student
    }

    @FXML
    private void handleRegister() {
        String user = txtUsername.getText().trim();
        String pass = txtPassword.getText().trim();
        String role = comboRole.getValue();

        if (user.isEmpty() || pass.isEmpty()) {
            lblMsg.setText("Fields cannot be empty.");
            return;
        }

        try {
            Integer memberId = null;
            
            if ("STUDENT".equals(role)) {
                String fn = txtFullName.getText().trim();
                String rn = txtRollNo.getText().trim();
                if (rn.isEmpty()) {
                    lblMsg.setText("Roll No required for Students.");
                    return;
                }
                
                // A. First check if this Student was ALREADY added by Librarian!
                Member existing = memberDAO.getMemberByRollNo(rn);
                if (existing != null) {
                    memberId = existing.getId();
                } else {
                    // B. If not pre-registered by librarian, create automatically now
                    if (fn.isEmpty()) {
                        lblMsg.setText("Full Name required to create new student record.");
                        return;
                    }
                    // Ensure email uniqueness by utilizing the pre-validated Unique Roll Number
                    Member m = new Member(0, fn, rn + "@mail.com", "", rn, "");
                    int generatedId = memberDAO.addMember(m);
                    if (generatedId <= 0) {
                        lblMsg.setText("Could not register student profile. Database issue.");
                        return;
                    }
                    memberId = generatedId;
                }
            }

            // 2. Create main user linking back to generated ID
            if (userDAO.register(user, pass, role, memberId)) {
                AlertUtil.showInfo("Success", "Account created successfully! Please login.");
                goToLogin();
            }
        } catch (Exception e) {
            e.printStackTrace();
            lblMsg.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void goToLogin() throws IOException {
        App.setRoot("login", "Login - LU-LMS");
    }
}
