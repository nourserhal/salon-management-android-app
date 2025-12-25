package com.example.noor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EmployeeAppointmentsActivity extends AppCompatActivity {

    RecyclerView recyclerEmployeeAppointments;
    AppointmentsAdapter adapter;
    ArrayList<AppointmentModel> list;

    myDBClass db;
    int employeeId;

    TextView tabLogout, tabMain, tabLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_appointments);

        employeeId = getIntent().getIntExtra("employeeId", -1);

        db = new myDBClass(this);

        recyclerEmployeeAppointments = findViewById(R.id.recyclerEmployeeAppointments);

        list = db.getAppointmentsByEmployee(employeeId);

        adapter = new AppointmentsAdapter(this, list);
        recyclerEmployeeAppointments.setLayoutManager(new LinearLayoutManager(this));
        recyclerEmployeeAppointments.setAdapter(adapter);

        tabLogout = findViewById(R.id.tabLogout);
        tabMain = findViewById(R.id.tabMain);
        tabLogin = findViewById(R.id.tabLogin);
        setupNav();
    }

    private void setupNav() {
        tabMain.setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));

        tabLogin.setOnClickListener(v ->
                startActivity(new Intent(this, login.class)));

        tabLogout.setOnClickListener(v -> {
            startActivity(new Intent(this, login.class));
            finish();
        });
    }
}
