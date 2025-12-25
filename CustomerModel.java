package com.example.noor;

public class CustomerModel {

    int id;
    String name;
    String email;
    String phone;
    String gender;
    String dob;

    public CustomerModel(int id, String name, String email, String phone, String gender, String dob) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.dob = dob;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getGender() { return gender; }
    public String getDob() { return dob; }
}
