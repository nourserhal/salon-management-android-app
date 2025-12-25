package com.example.noor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

public class myDBClass extends SQLiteOpenHelper {

    private static final String DB_NAME = "test1.db";
    private static final int DB_VERSION = 2;

    public myDBClass(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "email TEXT UNIQUE," +
                "password TEXT," +
                "phone TEXT," +
                "gender TEXT," +
                "dob TEXT," +
                "role TEXT)");


        db.execSQL("CREATE TABLE employees (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "email TEXT UNIQUE," +
                "password TEXT," +
                "specialty TEXT," +
                "phone TEXT," +
                "gender TEXT)");


        db.execSQL("CREATE TABLE services (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "price REAL," +
                "duration TEXT)");


        db.execSQL("CREATE TABLE service_employee (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "service_id INTEGER," +
                "employee_id INTEGER," +
                "FOREIGN KEY(service_id) REFERENCES services(id)," +
                "FOREIGN KEY(employee_id) REFERENCES employees(id))");

        db.execSQL("CREATE TABLE appointments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "customer_id INTEGER," +
                "service_id INTEGER," +
                "employee_id INTEGER," +
                "date TEXT," +
                "time TEXT," +
                "status TEXT," +
                "FOREIGN KEY(customer_id) REFERENCES users(id)," +
                "FOREIGN KEY(service_id) REFERENCES services(id)," +
                "FOREIGN KEY(employee_id) REFERENCES employees(id))");


        ContentValues admin = new ContentValues();
        admin.put("name", "Admin");
        admin.put("email", "admin@admin.com");
        admin.put("password", "123");
        admin.put("phone", "0000");
        admin.put("gender", "N/A");
        admin.put("dob", "2000-01-01");
        admin.put("role", "Admin");
        db.insert("users", null, admin);
    }
    public User loginEmployee(String email, String password) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT id, name FROM employees WHERE email=? AND password=?",
                new String[]{email, password}
        );

        if (c.moveToFirst()) {
            int id = c.getInt(0);
            String name = c.getString(1);
            c.close();
            return new User(id, name, "Employee");
        }

        c.close();
        return null;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS appointments");
        db.execSQL("DROP TABLE IF EXISTS service_employee");
        db.execSQL("DROP TABLE IF EXISTS services");
        db.execSQL("DROP TABLE IF EXISTS employees");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }


    public ArrayList<String> getEmployeesForService(int serviceId) {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT e.name " +
                        "FROM service_employee se " +
                        "JOIN employees e ON se.employee_id = e.id " +
                        "WHERE se.service_id = ?",
                new String[]{String.valueOf(serviceId)}
        );

        while (c.moveToNext()) {
            list.add(c.getString(0));
        }
        c.close();
        return list;
    }

    public ArrayList<String> getServicesForEmployee(int employeeId) {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT s.name " +
                        "FROM service_employee se " +
                        "JOIN services s ON se.service_id = s.id " +
                        "WHERE se.employee_id = ?",
                new String[]{String.valueOf(employeeId)}
        );

        while (c.moveToNext()) {
            list.add(c.getString(0));
        }
        c.close();
        return list;
    }


    public long registerCustomer(String name, String email, String password,
                                 String phone, String gender, String dob) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("email", email);
        cv.put("password", password);
        cv.put("phone", phone);
        cv.put("gender", gender);
        cv.put("dob", dob);
        cv.put("role", "Customer");

        return db.insert("users", null, cv);
    }

    public ArrayList<String> getAllSpecialties() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery("SELECT name FROM services", null);
        while (c.moveToNext()) {
            list.add(c.getString(0));
        }
        c.close();
        return list;
    }

    public Cursor getCustomerById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(
                "SELECT id, name, email, phone, gender, dob FROM users WHERE id=? AND role='Customer'",
                new String[]{String.valueOf(id)}
        );
    }

    public boolean deleteCustomer(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete("users", "id=? AND role='Customer'",
                new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public boolean updateCustomer(int id, String name, String email, String password,
                                  String phone, String gender, String dob) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", name);
        cv.put("email", email);
        cv.put("password", password);
        cv.put("phone", phone);
        cv.put("gender", gender);
        cv.put("dob", dob);

        int rows = db.update("users", cv, "id=? AND role='Customer'",
                new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public User loginUser(String email, String password, String roleExpected) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT id, name, role FROM users WHERE email=? AND password=? AND role=?",
                new String[]{email, password, roleExpected}
        );

        if (c.moveToFirst()) {
            int id = c.getInt(0);
            String name = c.getString(1);
            String role = c.getString(2);
            c.close();
            return new User(id, name, role);
        }

        c.close();
        return null;
    }

    public ArrayList<CustomerModel> getAllCustomers() {
        ArrayList<CustomerModel> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT id, name, email, phone, gender, dob FROM users WHERE role='Customer'",
                null
        );

        while (c.moveToNext()) {
            list.add(new CustomerModel(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getString(5)
            ));
        }
        c.close();
        return list;
    }

    public ArrayList<String> getAllCustomerNames() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT name FROM users WHERE role='Customer'",
                null
        );

        while (c.moveToNext()) {
            list.add(c.getString(0));
        }
        c.close();
        return list;
    }


    public long insertEmployee(String name, String email, String password,
                               String specialty, String phone, String gender) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", name);
        cv.put("email", email);
        cv.put("password", password);
        cv.put("specialty", specialty);
        cv.put("phone", phone);
        cv.put("gender", gender);

        return db.insert("employees", null, cv);
    }

    public boolean updateEmployee(int id, String name, String email, String password,
                                  String specialty, String phone, String gender) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", name);
        cv.put("email", email);
        cv.put("password", password);
        cv.put("specialty", specialty);
        cv.put("phone", phone);
        cv.put("gender", gender);

        int rows = db.update("employees", cv, "id=?",
                new String[]{String.valueOf(id)});
        return rows > 0;
    }

    // AUTO-MATCH: services.name = employees.specialty
    public ArrayList<ServiceEmployeeJoinModel> getServiceEmployeeJoined() {
        ArrayList<ServiceEmployeeJoinModel> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT s.id, s.name, s.price, s.duration, e.id, e.name " +
                        "FROM services s " +
                        "JOIN employees e ON e.specialty = s.name",
                null
        );

        while (c.moveToNext()) {
            int serviceId = c.getInt(0);
            String sName = c.getString(1);
            double price = c.getDouble(2);
            String duration = c.getString(3);
            int employeeId = c.getInt(4);
            String eName = c.getString(5);

            list.add(new ServiceEmployeeJoinModel(
                    serviceId,
                    sName,
                    price,
                    duration,
                    employeeId,
                    eName
            ));
        }
        c.close();
        return list;
    }

    public boolean deleteEmployee(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete("employees", "id=?",
                new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public ArrayList<EmployeeModel> getAllEmployees() {
        ArrayList<EmployeeModel> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT id, name, email, specialty, phone, gender FROM employees",
                null
        );

        while (c.moveToNext()) {
            list.add(new EmployeeModel(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getString(5)
            ));
        }
        c.close();
        return list;
    }

    public ArrayList<String> getAllEmployeesNames() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT name FROM employees",
                null
        );

        while (c.moveToNext()) {
            list.add(c.getString(0));
        }
        c.close();
        return list;
    }


    public long insertService(String name, double price, String duration) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", name);
        cv.put("price", price);
        cv.put("duration", duration);

        return db.insert("services", null, cv);
    }

    public boolean updateService(int id, String name, double price,
                                 String duration) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", name);
        cv.put("price", price);
        cv.put("duration", duration);

        int rows = db.update("services", cv, "id=?",
                new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public boolean deleteService(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete("services", "id=?",
                new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public ArrayList<ServiceModel> getAllServices() {
        ArrayList<ServiceModel> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT id, name, duration, price FROM services",
                null
        );

        while (c.moveToNext()) {
            list.add(new ServiceModel(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getDouble(3)
            ));
        }
        c.close();
        return list;
    }

    public ArrayList<String> getAllServicesNames() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT name FROM services",
                null
        );

        while (c.moveToNext()) {
            list.add(c.getString(0));
        }
        c.close();
        return list;
    }

    public ArrayList<ServiceModel> getEmployeeServices() {
        ArrayList<ServiceModel> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT services.id, services.name, services.duration, services.price, " +
                        "employees.id, employees.name " +
                        "FROM services " +
                        "LEFT JOIN service_employee ON services.id = service_employee.service_id " +
                        "LEFT JOIN employees ON employees.id = service_employee.employee_id",
                null
        );

        while (c.moveToNext()) {
            list.add(new ServiceModel(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getDouble(3)
            ));
        }
        c.close();
        return list;
    }

   
    public boolean bookAppointment(int customerId, int serviceId, int employeeId,
                                   String date, String time) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("customer_id", customerId);
        cv.put("service_id", serviceId);
        cv.put("employee_id", employeeId);
        cv.put("date", date);
        cv.put("time", time);
        cv.put("status", "Booked");

        return db.insert("appointments", null, cv) != -1;
    }

    public ArrayList<AppointmentModel> getAppointmentsByCustomer(int customerId) {
        ArrayList<AppointmentModel> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT a.id, s.name, e.name, u.name, a.date, a.time, a.status " +
                        "FROM appointments a " +
                        "LEFT JOIN services s ON a.service_id=s.id " +
                        "LEFT JOIN employees e ON a.employee_id=e.id " +
                        "LEFT JOIN users u ON a.customer_id=u.id " +
                        "WHERE a.customer_id=?",
                new String[]{String.valueOf(customerId)}
        );

        while (c.moveToNext()) {
            list.add(new AppointmentModel(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getString(5),
                    c.getString(6)
            ));
        }
        c.close();
        return list;
    }

    public ArrayList<AppointmentModel> getAppointmentsByEmployee(int employeeId) {
        ArrayList<AppointmentModel> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT a.id, s.name, e.name, u.name, a.date, a.time, a.status " +
                        "FROM appointments a " +
                        "LEFT JOIN services s ON a.service_id=s.id " +
                        "LEFT JOIN employees e ON a.employee_id=e.id " +
                        "LEFT JOIN users u ON a.customer_id=u.id " +
                        "WHERE a.employee_id=?",
                new String[]{String.valueOf(employeeId)}
        );

        while (c.moveToNext()) {
            list.add(new AppointmentModel(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getString(5),
                    c.getString(6)
            ));
        }
        c.close();
        return list;
    }

    public ArrayList<AppointmentModel> getAppointmentsForEmployeeFiltered(int employeeId,
                                                                          String status,
                                                                          String date) {
        ArrayList<AppointmentModel> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        StringBuilder sql = new StringBuilder(
                "SELECT a.id, s.name, e.name, u.name, a.date, a.time, a.status " +
                        "FROM appointments a " +
                        "LEFT JOIN services s ON a.service_id=s.id " +
                        "LEFT JOIN employees e ON a.employee_id=e.id " +
                        "LEFT JOIN users u ON a.customer_id=u.id " +
                        "WHERE a.employee_id=?"
        );

        ArrayList<String> args = new ArrayList<>();
        args.add(String.valueOf(employeeId));

        if (status != null && !status.equals("All")) {
            sql.append(" AND a.status=?");
            args.add(status);
        }

        if (date != null && !date.isEmpty()) {
            sql.append(" AND a.date=?");
            args.add(date);
        }

        Cursor c = db.rawQuery(sql.toString(), args.toArray(new String[0]));

        while (c.moveToNext()) {
            list.add(new AppointmentModel(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getString(5),
                    c.getString(6)
            ));
        }
        c.close();
        return list;
    }

    public ArrayList<AppointmentModel> getAllAppointments() {
        ArrayList<AppointmentModel> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT a.id, s.name, e.name, u.name, a.date, a.time, a.status " +
                        "FROM appointments a " +
                        "LEFT JOIN services s ON a.service_id=s.id " +
                        "LEFT JOIN employees e ON a.employee_id=e.id " +
                        "LEFT JOIN users u ON a.customer_id=u.id",
                null
        );

        while (c.moveToNext()) {
            list.add(new AppointmentModel(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getString(5),
                    c.getString(6)
            ));
        }
        c.close();
        return list;
    }

    public boolean updateAppointmentBasic(int id, String date, String time) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("date", date);
        cv.put("time", time);

        int rows = db.update("appointments", cv, "id=?",
                new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public boolean updateAppointmentStatus(int id, String newStatus) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("status", newStatus);

        int rows = db.update("appointments", cv, "id=?",
                new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public boolean deleteAppointment(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete("appointments", "id=?",
                new String[]{String.valueOf(id)});
        return rows > 0;
    }
}
