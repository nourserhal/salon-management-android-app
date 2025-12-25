package com.example.noor;

public class ServiceModel {

    int id;
    String name;
    String duration;
    double price;


    public ServiceModel(int id, String name, String duration, double price) {

        this.id = id;
        this.name = name;
        this.duration = duration;
        this.price = price;

    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDuration() { return duration; }
    public double getPrice() { return price; }

}
