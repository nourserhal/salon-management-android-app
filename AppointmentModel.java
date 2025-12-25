package com.example.noor;

public class AppointmentModel {

    int id;
    String serviceName;
    String employeeName;
    String customerName;
    String date;
    String time;
    String status;

    public AppointmentModel(int id, String serviceName, String employeeName, String customerName,
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
    public void setStatus(String status) {
        this.status = status;
    }
}
