package com.example.noor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class EmployeeDashboardActivity extends AppCompatActivity {

    TextView txtEmpName, tabLogout, tabMain, tabLogin;
    Button btnShowMyAppointments;

    int employeeId;
    String employeeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dashboard);

        employeeId = getIntent().getIntExtra("employeeId", -1);
        employeeName = getIntent().getStringExtra("employeeName");

        txtEmpName = findViewById(R.id.txtEmpName);
        txtEmpName.setText(employeeName);

        btnShowMyAppointments = findViewById(R.id.btnShowMyAppointments);

        btnShowMyAppointments.setOnClickListener(v -> {
            Intent i = new Intent(this, EmployeeAppointmentsActivity.class);
            i.putExtra("employeeId", employeeId);
            startActivity(i);
        });

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

        tabLogout.setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle("Logout")
                        .setMessage("Logout now?")
                        .setPositiveButton("Yes", (d, w) -> {
                            startActivity(new Intent(this, login.class));
                            finish();
                        })
                        .setNegativeButton("No", null)
                        .show());
    }
}
