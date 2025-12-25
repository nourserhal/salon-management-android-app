package com.example.noor;

public class ServiceEmployeeJoinModel {

    int serviceId;
    String serviceName;
    double price;
    String duration;

    int employeeId;
    String employeeName;

    public ServiceEmployeeJoinModel(int serviceId, String serviceName,
                                    double price, String duration,
                                    int employeeId, String employeeName) {

        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.price = price;
        this.duration = duration;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
    }

    public int getServiceId() { return serviceId; }
    public String getServiceName() { return serviceName; }
    public double getPrice() { return price; }
    public String getDuration() { return duration; }

    public int getEmployeeeeId() { return employeeId; }
    public String getEmployeeeeName() { return employeeName; }
}
