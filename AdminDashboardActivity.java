package com.example.noor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    LinearLayout cardManageServices, cardManageEmployees, cardManageCustomers, cardManageAppointments;

    TextView tabLogout, tabMain, tabLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        cardManageServices = findViewById(R.id.cardManageServices);
        cardManageEmployees = findViewById(R.id.cardManageEmployees);
        cardManageCustomers = findViewById(R.id.cardManageCustomers);
        cardManageAppointments = findViewById(R.id.cardManageAppointments);

        cardManageServices.setOnClickListener(v ->
                startActivity(new Intent(this, ManageServicesActivity.class)));

        cardManageEmployees.setOnClickListener(v ->
                startActivity(new Intent(this, ManageEmployeesActivity.class)));

        cardManageCustomers.setOnClickListener(v ->
                startActivity(new Intent(this, ManageCustomersActivity.class)));

        cardManageAppointments.setOnClickListener(v ->
                startActivity(new Intent(this, ManageAppointmentsActivity.class)));
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
