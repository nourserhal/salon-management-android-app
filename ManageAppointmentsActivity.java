package com.example.noor;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class ManageAppointmentsActivity extends AppCompatActivity {

    RecyclerView recyclerAppointments;
    AppointmentsAdapter adapter;
    ArrayList<AppointmentModel> appointmentList;

    myDBClass db;

    TextView tabLogout, tabMain, tabLogin;
    Spinner spAppointment, spService, spCustomer, spEmployee, spStatus;
    EditText etDate, etTime;
    Button btnInsert, btnUpdate, btnDelete, btnShowAll;


    ArrayList<String> serviceNames = new ArrayList<>();
    ArrayList<String> employeeNames = new ArrayList<>();
    ArrayList<String> customerNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_appointments);

        db = new myDBClass(this);

        // Views
        recyclerAppointments = findViewById(R.id.recyclerAppointments);

        spAppointment = findViewById(R.id.spAppointment);
        spService     = findViewById(R.id.spService);
        spCustomer    = findViewById(R.id.spCustomer);
        spEmployee    = findViewById(R.id.spEmployee);
        spStatus      = findViewById(R.id.spStatus);

        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);

        btnInsert  = findViewById(R.id.btnInsert);
        btnUpdate  = findViewById(R.id.btnUpdate);
        btnDelete  = findViewById(R.id.btnDelete);
        btnShowAll = findViewById(R.id.btnShowAll);

        tabLogout = findViewById(R.id.tabLogout);
        tabMain   = findViewById(R.id.tabMain);
        tabLogin  = findViewById(R.id.tabLogin);

        setupNav();
        setupStatusSpinner();
        loadServiceSpinner();
        loadEmployeeSpinner();
        loadCustomerSpinner();
        loadAppointments();

        etDate.setOnClickListener(v -> pickDate());
        etTime.setOnClickListener(v -> pickTime());

        btnInsert.setOnClickListener(v -> insertAppointment());

        btnUpdate.setOnClickListener(v -> updateAppointmentStatus());

        btnDelete.setOnClickListener(v -> deleteAppointment());

        btnShowAll.setOnClickListener(v -> loadAppointments());
    }


    private void setupNav() {
        if (tabMain != null) {
            tabMain.setOnClickListener(v ->
                    startActivity(new Intent(this, MainActivity.class)));
        }

        if (tabLogin != null) {
            tabLogin.setOnClickListener(v ->
                    startActivity(new Intent(this, login.class)));
        }

        if (tabLogout != null) {
            tabLogout.setOnClickListener(v ->
                    new AlertDialog.Builder(this)
                            .setTitle("Logout")
                            .setMessage("Are you sure you want to logout?")
                            .setPositiveButton("Yes", (d, w) -> {
                                startActivity(new Intent(this, login.class));
                                finish();
                            })
                            .setNegativeButton("No", null)
                            .show());
        }
    }



    private void setupStatusSpinner() {
        String[] statuses = {"Booked", "Completed", "Cancelled"};
        ArrayAdapter<String> a = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                statuses
        );
        spStatus.setAdapter(a);
    }

    private void loadServiceSpinner() {
        serviceNames = db.getAllServicesNames();
        ArrayAdapter<String> a = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                serviceNames
        );
        spService.setAdapter(a);
    }

    private void loadEmployeeSpinner() {
        employeeNames = db.getAllEmployeesNames();
        ArrayAdapter<String> a = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                employeeNames
        );
        spEmployee.setAdapter(a);
    }

    private void loadCustomerSpinner() {
        customerNames = db.getAllCustomerNames();
        ArrayAdapter<String> a = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                customerNames
        );
        spCustomer.setAdapter(a);
    }

    private void loadAppointments() {
        appointmentList = db.getAllAppointments();


        adapter = new AppointmentsAdapter(this, appointmentList);
        recyclerAppointments.setLayoutManager(new LinearLayoutManager(this));
        recyclerAppointments.setAdapter(adapter);


        ArrayList<String> appLabels = new ArrayList<>();
        for (AppointmentModel a : appointmentList) {
            String label = "ID " + a.getId()
                    + " - " + a.getCustomerName()
                    + " / " + a.getServiceName()
                    + " / " + a.getEmployeeName();
            appLabels.add(label);
        }
        ArrayAdapter<String> ad = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                appLabels
        );
        spAppointment.setAdapter(ad);
    }



    private void pickDate() {
        Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dlg = new DatePickerDialog(this, (view, year, month, day) -> {
            String dateStr = day + "/" + (month + 1) + "/" + year;
            etDate.setText(dateStr);
        }, y, m, d);
        dlg.show();
    }

    private void pickTime() {
        Calendar c = Calendar.getInstance();
        int h = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);

        TimePickerDialog dlg = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            String timeStr = hourOfDay + ":" + (minute < 10 ? "0" + minute : minute);
            etTime.setText(timeStr);
        }, h, min, true);
        dlg.show();
    }



    private void insertAppointment() {
        if (customerNames == null || customerNames.isEmpty()
                || serviceNames == null || serviceNames.isEmpty()
                || employeeNames == null || employeeNames.isEmpty()) {
            Toast.makeText(this, "Please make sure customers, services and employees exist.", Toast.LENGTH_SHORT).show();
            return;
        }

        int customerIndex = spCustomer.getSelectedItemPosition();
        int serviceIndex  = spService.getSelectedItemPosition();
        int employeeIndex = spEmployee.getSelectedItemPosition();

        if (customerIndex < 0 || serviceIndex < 0 || employeeIndex < 0) {
            Toast.makeText(this, "Please select customer, service and employee.", Toast.LENGTH_SHORT).show();
            return;
        }

        int customerId = customerIndex + 1;
        int serviceId  = serviceIndex + 1;
        int employeeId = employeeIndex + 1;

        String date = etDate.getText().toString().trim();
        String time = etTime.getText().toString().trim();

        if (date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Please select date and time.", Toast.LENGTH_SHORT).show();
            return;
        }


        new AlertDialog.Builder(this)
                .setTitle("Confirm Insert")
                .setMessage("Are you sure you want to add this appointment?")
                .setPositiveButton("OK", (dialog, which) -> {

                    boolean ok = db.bookAppointment(customerId, serviceId, employeeId, date, time);
                    if (ok) {
                        new AlertDialog.Builder(this)
                                .setTitle("Success")
                                .setMessage("Appointment inserted successfully.")
                                .setPositiveButton("OK", null)
                                .show();
                        loadAppointments();
                    } else {
                        new AlertDialog.Builder(this)
                                .setTitle("Error")
                                .setMessage("Error inserting appointment.")
                                .setPositiveButton("OK", null)
                                .show();
                    }

                })
                .setNegativeButton("Cancel", (dialog, which) ->
                        Toast.makeText(this, "Insert canceled", Toast.LENGTH_SHORT).show())
                .show();
    }


    private void updateAppointmentStatus() {
        if (appointmentList == null || appointmentList.isEmpty()) {
            Toast.makeText(this, "No appointments to update.", Toast.LENGTH_SHORT).show();
            return;
        }

        int pos = spAppointment.getSelectedItemPosition();
        if (pos < 0 || pos >= appointmentList.size()) {
            Toast.makeText(this, "Please select an appointment.", Toast.LENGTH_SHORT).show();
            return;
        }

        AppointmentModel app = appointmentList.get(pos);
        int appId = app.getId();

        String newStatus = spStatus.getSelectedItem() != null
                ? spStatus.getSelectedItem().toString()
                : "Booked";


        new AlertDialog.Builder(this)
                .setTitle("Confirm Update")
                .setMessage("Are you sure you want to update this appointment status?")
                .setPositiveButton("OK", (dialog, which) -> {

                    boolean ok = db.updateAppointmentStatus(appId, newStatus);
                    if (ok) {
                        new AlertDialog.Builder(this)
                                .setTitle("Updated")
                                .setMessage("Appointment status updated successfully.")
                                .setPositiveButton("OK", null)
                                .show();
                        loadAppointments();
                    } else {
                        new AlertDialog.Builder(this)
                                .setTitle("Error")
                                .setMessage("Error updating status.")
                                .setPositiveButton("OK", null)
                                .show();
                    }

                })
                .setNegativeButton("Cancel", (dialog, which) ->
                        Toast.makeText(this, "Update canceled", Toast.LENGTH_SHORT).show())
                .show();
    }



    private void deleteAppointment() {
        if (appointmentList == null || appointmentList.isEmpty()) {
            Toast.makeText(this, "No appointments to delete.", Toast.LENGTH_SHORT).show();
            return;
        }

        int pos = spAppointment.getSelectedItemPosition();
        if (pos < 0 || pos >= appointmentList.size()) {
            Toast.makeText(this, "Please select an appointment.", Toast.LENGTH_SHORT).show();
            return;
        }

        AppointmentModel app = appointmentList.get(pos);
        int appId = app.getId();


        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this appointment?")
                .setPositiveButton("OK", (dialog, which) -> {

                    boolean ok = db.deleteAppointment(appId);
                    if (ok) {
                        new AlertDialog.Builder(this)
                                .setTitle("Deleted")
                                .setMessage("Appointment deleted successfully.")
                                .setPositiveButton("OK", null)
                                .show();
                        loadAppointments();
                    } else {
                        new AlertDialog.Builder(this)
                                .setTitle("Error")
                                .setMessage("Error deleting appointment.")
                                .setPositiveButton("OK", null)
                                .show();
                    }

                })
                .setNegativeButton("Cancel", (dialog, which) ->
                        Toast.makeText(this, "Delete canceled", Toast.LENGTH_SHORT).show())
                .show();
    }
}
