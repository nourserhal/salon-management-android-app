package com.example.noor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class login extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    Button btnLogin;
    TextView txtRegister;

    myDBClass db;
    String role = "Customer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new myDBClass(this);

        edtEmail    = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin    = findViewById(R.id.btnLogin);
        txtRegister = findViewById(R.id.txtRegister);

        // Which role are we logging in as?
        String r = getIntent().getStringExtra("role");
        if (r != null) role = r;

        // Only customers can register
        if (!role.equals("Customer")) {
            txtRegister.setVisibility(View.GONE);
        }

        btnLogin.setOnClickListener(v -> loginUser());

        txtRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void loginUser() {
        String email = edtEmail.getText().toString().trim();
        String pass  = edtPassword.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }


        if (role.equals("Employee")) {

            User emp = db.loginEmployee(email, pass);

            if (emp == null) {
                Toast.makeText(this, "Invalid employee login!", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Logged in as Employee", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(this, EmployeeDashboardActivity.class);
            i.putExtra("employeeId", emp.getId());
            i.putExtra("employeeName", emp.getName());
            startActivity(i);

            return;
        }


        User u = db.loginUser(email, pass, role);

        if (u == null) {
            Toast.makeText(this, "Invalid " + role + " login!", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Logged in as: " + role, Toast.LENGTH_SHORT).show();

        if (role.equals("Customer")) {
            Intent i = new Intent(this, CustomerDashboardActivity.class);
            i.putExtra("customerId", u.getId());
            i.putExtra("customerName", u.getName());
            startActivity(i);

        } else if (role.equals("Admin")) {
            Intent i = new Intent(this, AdminDashboardActivity.class);
            startActivity(i);
        }
    }
}
