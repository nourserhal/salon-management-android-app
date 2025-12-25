package com.example.noor;

public class EmployeeModel {

    private int id;
    private String name;
    private String email;
    private String specialty;
    private String phone;
    private String gender;

    public EmployeeModel(int id, String name, String email,
                         String specialty, String phone, String gender) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.specialty = specialty;
        this.phone = phone;
        this.gender = gender;
    }

    public int getId() { return id; }

    public String getName() { return name; }

    public String getEmail() { return email; }

    public String getSpecialty() { return specialty; }

    public String getPhone() { return phone; }

    public String getGender() { return gender; }
}
