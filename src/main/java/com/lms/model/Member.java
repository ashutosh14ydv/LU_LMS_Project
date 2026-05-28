package com.lms.model;

public class Member {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String rollNo;
    private String address;

    public Member() {}

    public Member(int id, String name, String email, String phone, String rollNo, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.rollNo = rollNo;
        this.address = address;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
