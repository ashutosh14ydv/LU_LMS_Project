package com.lms.model;

public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private String publisher;
    private int quantity;
    private int available;
    private double price;

    public Book() {}

    public Book(int id, String title, String author, String isbn, String publisher, int quantity, int available, double price) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publisher = publisher;
        this.quantity = quantity;
        this.available = available;
        this.price = price;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getAvailable() { return available; }
    public void setAvailable(int available) { this.available = available; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
