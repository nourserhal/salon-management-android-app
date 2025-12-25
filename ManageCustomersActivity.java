package com.example.noor;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ManageCustomersActivity extends AppCompatActivity {

    EditText edtCustId, edtCustName, edtCustEmail, edtCustPassword, edtCustPhone, edtCustDob;
    Spinner spCustGender;
    Button btnCustAdd, btnCustUpdate, btnCustDelete, btnCustSearch, btnCustShowAll;

    TextView tabLogout, tabMain, tabLogin;

    RecyclerView recyclerCustomers;
    CustomersAdapter adapter;
    ArrayList<CustomerModel> customerList;

    myDBClass db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_customers);

        db = new myDBClass(this);

        // INPUTS
        edtCustId       = findViewById(R.id.edtCustId);
        edtCustName     = findViewById(R.id.edtCustName);
        edtCustEmail    = findViewById(R.id.edtCustEmail);
        edtCustPassword = findViewById(R.id.edtCustPassword);
        edtCustPhone    = findViewById(R.id.edtCustPhone);
        edtCustDob      = findViewById(R.id.edtCustDob);

        spCustGender = findViewById(R.id.spCustGender);

        // BUTTONS
        btnCustAdd      = findViewById(R.id.btnInsertCust);
        btnCustUpdate   = findViewById(R.id.btnUpdateCust);
        btnCustDelete   = findViewById(R.id.btnDeleteCust);
        btnCustSearch   = findViewById(R.id.btnSearchCust);
        btnCustShowAll  = findViewById(R.id.btnViewAllCust);

        recyclerCustomers = findViewById(R.id.recyclerCustomers);

        // NAV BAR
        tabLogout = findViewById(R.id.tabLogout);
        tabMain   = findViewById(R.id.tabMain);
        tabLogin  = findViewById(R.id.tabLogin);

        setupGender();
        setupNav();

        btnCustAdd.setOnClickListener(v -> addCustomer());
        btnCustUpdate.setOnClickListener(v -> updateCustomer());
        btnCustDelete.setOnClickListener(v -> deleteCustomer());
        btnCustSearch.setOnClickListener(v -> searchCustomer());
        btnCustShowAll.setOnClickListener(v -> loadAllCustomers());
    }
    private void setupGender() {
        String[] genders = {"Male", "Female"};
        ArrayAdapter<String> a = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, genders);
        spCustGender.setAdapter(a);
    }

    private void setupNav() {

        tabMain.setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));

        tabLogin.setOnClickListener(v ->
                startActivity(new Intent(this, login.class)));

        tabLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (d, w) -> {
                        startActivity(new Intent(this, login.class));
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void addCustomer() {

        String name   = edtCustName.getText().toString();
        String email  = edtCustEmail.getText().toString();
        String pass   = edtCustPassword.getText().toString();
        String phone  = edtCustPhone.getText().toString();
        String gender = spCustGender.getSelectedItem().toString();
        String dob    = edtCustDob.getText().toString();

        if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Name, Email, and Password required!", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirm Insert")
                .setMessage("Add this customer?")
                .setPositiveButton("OK", (dialog, which) -> {

                    long res = db.registerCustomer(name, email, pass, phone, gender, dob);

                    if (res != -1) {
                        Toast.makeText(this, "Customer added!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to add customer!", Toast.LENGTH_SHORT).show();
                    }

                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    private void updateCustomer() {

        String idStr = edtCustId.getText().toString();
        if (idStr.isEmpty()) {
            Toast.makeText(this, "Enter Customer ID", Toast.LENGTH_SHORT).show();
            return;
        }

        int id = Integer.parseInt(idStr);

        String name   = edtCustName.getText().toString();
        String email  = edtCustEmail.getText().toString();
        String pass   = edtCustPassword.getText().toString();
        String phone  = edtCustPhone.getText().toString();
        String gender = spCustGender.getSelectedItem().toString();
        String dob    = edtCustDob.getText().toString();

        new AlertDialog.Builder(this)
                .setTitle("Confirm Update")
                .setMessage("Update this customer?")
                .setPositiveButton("OK", (dialog, which) -> {

                    SQLiteDatabase wdb = db.getWritableDatabase();
                    ContentValues cv = new ContentValues();

                    cv.put("name", name);
                    cv.put("email", email);
                    cv.put("password", pass);
                    cv.put("phone", phone);
                    cv.put("gender", gender);
                    cv.put("dob", dob);

                    int rows = wdb.update("users", cv, "id=? AND role='Customer'",
                            new String[]{idStr});

                    if (rows > 0) {
                        Toast.makeText(this, "Customer updated!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Update failed!", Toast.LENGTH_SHORT).show();
                    }

                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    private void deleteCustomer() {

        String idStr = edtCustId.getText().toString();
        if (idStr.isEmpty()) {
            Toast.makeText(this, "Enter Customer ID", Toast.LENGTH_SHORT).show();
            return;
        }

        int id = Integer.parseInt(idStr);

        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Delete this customer?")
                .setPositiveButton("OK", (dialog, which) -> {

                    boolean ok = db.deleteCustomer(id);

                    if (ok) {
                        Toast.makeText(this, "Customer deleted!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Delete failed!", Toast.LENGTH_SHORT).show();
                    }

                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    private void searchCustomer() {

        String idStr = edtCustId.getText().toString();
        if (idStr.isEmpty()) {
            Toast.makeText(this, "Enter Customer ID", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor c = db.getCustomerById(Integer.parseInt(idStr));

        if (c.moveToFirst()) {

            edtCustName.setText(c.getString(1));
            edtCustEmail.setText(c.getString(2));
            edtCustPhone.setText(c.getString(3));
            edtCustDob.setText(c.getString(5));

            String gender = c.getString(4);
            spCustGender.setSelection(gender.equalsIgnoreCase("Male") ? 0 : 1);

        } else {
            Toast.makeText(this, "Customer not found!", Toast.LENGTH_SHORT).show();
        }

        c.close();
    }

    private void loadAllCustomers() {
        customerList = db.getAllCustomers();
        adapter = new CustomersAdapter(this, customerList);
        recyclerCustomers.setLayoutManager(new LinearLayoutManager(this));
        recyclerCustomers.setAdapter(adapter);
    }
}
