package com.lms.controller;

import com.lms.model.Member;
import com.lms.service.LibraryService;
import com.lms.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class MembersController implements Initializable {

    @FXML private TextField txtName, txtEmail, txtPhone, txtRollNo;
    @FXML private TextArea txtAddress;
    @FXML private TableView<Member> tblMembers;
    @FXML private TableColumn<Member, Integer> colId;
    @FXML private TableColumn<Member, String> colName, colRoll, colEmail, colPhone;

    private final LibraryService service = LibraryService.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colRoll.setCellValueFactory(new PropertyValueFactory<>("rollNo"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        loadTableData();
    }

    private void loadTableData() {
        try {
            tblMembers.setItems(FXCollections.observableArrayList(service.getAllMembers()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAdd() {
        try {
            String n = txtName.getText();
            String e = txtEmail.getText();
            String r = txtRollNo.getText();
            
            if(n.isEmpty() || e.isEmpty() || r.isEmpty()) {
                AlertUtil.showError("Missing Info", "Name, Email and Roll No are required.");
                return;
            }

            Member m = new Member(0, n, e, txtPhone.getText(), r, txtAddress.getText());
            if (service.addMember(m)) {
                AlertUtil.showInfo("Success", "Member registered!");
                loadTableData();
                handleClear();
            }
        } catch (Exception e) {
            AlertUtil.showError("Database Error", e.getMessage());
        }
    }

    @FXML
    private void handleClear() {
        txtName.clear(); txtEmail.clear(); txtPhone.clear(); txtRollNo.clear(); txtAddress.clear();
    }
}
