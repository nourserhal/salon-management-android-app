package com.example.noor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CustomerDashboardActivity extends AppCompatActivity {

    TextView txtWelcome, tabLogout, tabMain, tabLogin, btnBook, btnMyAppointments;

    int customerId;
    String customerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        // GET INTENTS FIRST (before any buttons)
        customerId = getIntent().getIntExtra("customerId", -1);
        customerName = getIntent().getStringExtra("customerName");

        if (customerId == -1) {
            Toast.makeText(this, "Error: No customer ID passed!", Toast.LENGTH_LONG).show();
            finish(); // STOP crash
            return;
        }

        // FIND VIEWS
        txtWelcome = findViewById(R.id.txtWelcome);
        btnBook = findViewById(R.id.btnBook);
        btnMyAppointments = findViewById(R.id.btnMyAppointments);

        txtWelcome.setText("Welcome, " + customerName);

        btnBook.setOnClickListener(v -> {
            Intent i = new Intent(this, BookAppointmentActivity.class);
            i.putExtra("customerId", customerId);
            i.putExtra("customerName", customerName);
            startActivity(i);
        });

        btnMyAppointments.setOnClickListener(v -> {
            Intent i = new Intent(this, CustomerAppointmentsActivity.class);
            i.putExtra("customerId", customerId);
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
