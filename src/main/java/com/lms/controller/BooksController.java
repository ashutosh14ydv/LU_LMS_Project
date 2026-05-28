package com.lms.controller;

import com.lms.model.Book;
import com.lms.service.LibraryService;
import com.lms.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class BooksController implements Initializable {

    @FXML private TextField txtTitle, txtAuthor, txtISBN, txtPublisher, txtQuantity, txtSearch, txtPrice;
    @FXML private TableView<Book> tblBooks;
    @FXML private TableColumn<Book, Integer> colId, colQuantity, colAvailable;
    @FXML private TableColumn<Book, String> colTitle, colAuthor, colISBN;
    @FXML private TableColumn<Book, Double> colPrice;

    private final LibraryService service = LibraryService.getInstance();
    private final ObservableList<Book> bookList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colAvailable.setCellValueFactory(new PropertyValueFactory<>("available"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        loadTableData();
        setupSearchFilter();
    }

    private void loadTableData() {
        try {
            bookList.clear();
            bookList.addAll(service.getAllBooks());
            tblBooks.setItems(bookList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupSearchFilter() {
        FilteredList<Book> filteredData = new FilteredList<>(bookList, b -> true);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(book -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String low = newValue.toLowerCase();
                return book.getTitle().toLowerCase().contains(low) || 
                       book.getAuthor().toLowerCase().contains(low) ||
                       book.getIsbn().toLowerCase().contains(low);
            });
        });
        tblBooks.setItems(filteredData);
    }

    @FXML
    private void handleAddBook() {
        try {
            String title = txtTitle.getText();
            String author = txtAuthor.getText();
            String isbn = txtISBN.getText();
            String pub = txtPublisher.getText();
            int qty = Integer.parseInt(txtQuantity.getText());
            double price = Double.parseDouble(txtPrice.getText());

            if (title.isEmpty() || author.isEmpty()) {
                AlertUtil.showError("Validation", "Fields cannot be empty.");
                return;
            }

            Book b = new Book(0, title, author, isbn, pub, qty, qty, price);
            if (service.addBook(b)) {
                AlertUtil.showInfo("Success", "Book added successfully!");
                loadTableData();
                handleClear();
                setupSearchFilter(); // refresh bound list
            }
        } catch (NumberFormatException e) {
            AlertUtil.showError("Input Error", "Quantity and Price must be numeric.");
        } catch (Exception e) {
            AlertUtil.showError("Database Error", e.getMessage());
        }
    }

    @FXML
    private void handleClear() {
        txtTitle.clear(); txtAuthor.clear(); txtISBN.clear(); txtPublisher.clear(); txtQuantity.clear(); txtPrice.clear();
    }
}
