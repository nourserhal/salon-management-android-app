package com.example.noor;

public class CustomerAppointementModel {

    int id;
    String serviceName;
    String employeeName;
    String customerName;
    String date;
    String time;
    String status;

    public CustomerAppointementModel(int id, String serviceName, String employeeName, String customerName,
                            String date, String time, String status) {

        this.id = id;
        this.serviceName = serviceName;
        this.employeeName = employeeName;
        this.customerName = customerName;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public int getId() { return id; }
    public String getServiceName() { return serviceName; }
    public String getEmployeeName() { return employeeName; }
    public String getCustomerName() { return customerName; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getStatus() { return status; }

}
