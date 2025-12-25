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

import java.util.ArrayList;
import java.util.Calendar;

public class BookAppointmentActivity extends AppCompatActivity {

    private Spinner spinnerServices;      // ONE spinner: Service + Employee (JOIN)
    private TextView txtPrice, txtDuration;
    private EditText etDate, etTime;
    private Button btnConfirm;

    private TextView tabLogout, tabMain, tabLogin;

    private myDBClass db;

    private ArrayList<ServiceEmployeeJoinModel> joinList = new ArrayList<>();

    private int customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        db = new myDBClass(this);


        customerId = getIntent().getIntExtra("customerId", -1);


        spinnerServices = findViewById(R.id.spinnerServices);
        txtPrice        = findViewById(R.id.txtPrice);
        txtDuration     = findViewById(R.id.txtDuration);
        etDate          = findViewById(R.id.etDate);
        etTime          = findViewById(R.id.etTime);
        btnConfirm      = findViewById(R.id.btnConfirm);

        tabLogout = findViewById(R.id.tabLogout);
        tabMain   = findViewById(R.id.tabMain);
        tabLogin  = findViewById(R.id.tabLogin);


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
                            .show()
            );
        }


        loadServiceEmployeeJoin();

        etDate.setOnClickListener(v -> showDatePicker());
        etTime.setOnClickListener(v -> showTimePicker());


        btnConfirm.setOnClickListener(v -> confirmAppointment());
    }


    private void loadServiceEmployeeJoin() {

        joinList = db.getServiceEmployeeJoined();

        if (joinList == null || joinList.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle("No Services")
                    .setMessage("There are no service-employee combinations available.\nAsk the admin to assign employees to services.")
                    .setPositiveButton("OK", (d, w) -> finish())
                    .show();
            return;
        }


        ArrayList<String> labels = new ArrayList<>();
        for (ServiceEmployeeJoinModel m : joinList) {
            String label = m.getServiceName() + " — " + m.getEmployeeeeName();
            labels.add(label);
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, labels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerServices.setAdapter(adapter);


        spinnerServices.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent,
                                       android.view.View view,
                                       int position,
                                       long id) {

                ServiceEmployeeJoinModel selected = joinList.get(position);

                txtPrice.setText("Price: " + selected.getPrice());
                txtDuration.setText("Duration: " + selected.getDuration());
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });

        // Initialize price and duration for first item
        ServiceEmployeeJoinModel first = joinList.get(0);
        txtPrice.setText("Price: " + first.getPrice());
        txtDuration.setText("Duration: " + first.getDuration() + " min");
    }


    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dp = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String date = year + "-" + (month + 1) + "-" + dayOfMonth;
                    etDate.setText(date);
                }, y, m, d);
        dp.show();
    }

    private void showTimePicker() {
        Calendar c = Calendar.getInstance();
        int h = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);

        TimePickerDialog tp = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    String time = String.format("%02d:%02d", hourOfDay, minute);
                    etTime.setText(time);
                }, h, min, true);
        tp.show();
    }


    private void confirmAppointment() {

        if (customerId == -1) {
            Toast.makeText(this, "Missing customer id (not logged in correctly)", Toast.LENGTH_SHORT).show();
            return;
        }

        if (joinList == null || joinList.isEmpty()) {
            Toast.makeText(this, "No service-employee selected", Toast.LENGTH_SHORT).show();
            return;
        }

        String date = etDate.getText().toString().trim();
        String time = etTime.getText().toString().trim();

        if (date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Please choose date and time", Toast.LENGTH_SHORT).show();
            return;
        }

        int pos = spinnerServices.getSelectedItemPosition();
        if (pos < 0 || pos >= joinList.size()) {
            Toast.makeText(this, "Invalid selection", Toast.LENGTH_SHORT).show();
            return;
        }

        ServiceEmployeeJoinModel selected = joinList.get(pos);

        int serviceId  = selected.getServiceId();
        int employeeId = selected.getEmployeeeeId();

        boolean ok = db.bookAppointment(customerId, serviceId, employeeId, date, time);

        Toast.makeText(this,
                ok ? "Appointment booked!" : "Failed to book appointment.",
                Toast.LENGTH_SHORT).show();

        if (ok) {
            finish();
        }
    }
}
