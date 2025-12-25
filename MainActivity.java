package com.example.noor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    LinearLayout cardCustomer, cardEmployee, cardAdmin;
    myDBClass db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new myDBClass(this);

        cardCustomer = findViewById(R.id.btnCustomerLogin);
        cardEmployee = findViewById(R.id.btnEmployeeLogin);
        cardAdmin    = findViewById(R.id.cardAdmin);

        cardCustomer.setOnClickListener(v -> openLogin("Customer"));
        cardEmployee.setOnClickListener(v -> openLogin("Employee"));
        cardAdmin.setOnClickListener(v -> openLogin("Admin"));
    }


    private void openLogin(String role) {
        Intent intent = new Intent(MainActivity.this, login.class);
        intent.putExtra("role", role);
        startActivity(intent);
    }
}
