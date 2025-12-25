package com.example.noor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomerAppointmentsActivity extends AppCompatActivity {

    RecyclerView recyclerMyAppointments;
    CustomerAppointementAdapter adapter;
    ArrayList<CustomerAppointementModel> list;

    myDBClass db;
    int customerId;

    TextView tabLogout, tabMain, tabLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_appointments);

        customerId = getIntent().getIntExtra("customerId", -1);

        db = new myDBClass(this);

        recyclerMyAppointments = findViewById(R.id.recyclerAppointments);

        // FIX START -----------------------------------------
        ArrayList<AppointmentModel> temp = db.getAppointmentsByCustomer(customerId);
        list = new ArrayList<>();

        for (AppointmentModel a : temp) {
            list.add(new CustomerAppointementModel(
                    a.getId(),
                    a.getServiceName(),
                    a.getEmployeeName(),
                    a.getCustomerName(),
                    a.getDate(),
                    a.getTime(),
                    a.getStatus()
            ));
        }
        // FIX END -------------------------------------------

        adapter = new CustomerAppointementAdapter(this, list);
        recyclerMyAppointments.setLayoutManager(new LinearLayoutManager(this));
        recyclerMyAppointments.setAdapter(adapter);

        tabLogout = findViewById(R.id.tabLogout);
        tabMain = findViewById(R.id.tabMain);
        tabLogin = findViewById(R.id.tabLogin);

        setupNav();
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
            tabLogout.setOnClickListener(v -> {
                startActivity(new Intent(this, login.class));
                finish();
            });
        }
    }
}
