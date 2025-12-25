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

public class ManageEmployeesActivity extends AppCompatActivity {

    EditText edtEmpId, edtEmpName, edtEmpEmail, edtEmpPassword, edtEmpPhone;
    Spinner spEmpSpecialty, spEmpGender;

    Button btnEmpAdd, btnEmpUpdate, btnEmpDelete, btnEmpSearch, btnEmpShowAll;

    TextView tabLogout, tabMain, tabLogin;

    RecyclerView recyclerEmployees;
    EmployeesAdapter adapter;
    ArrayList<EmployeeModel> employeeList;

    myDBClass db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_employees);

        db = new myDBClass(this);

        edtEmpId       = findViewById(R.id.edtEmpId);
        edtEmpName     = findViewById(R.id.edtEmpName);
        edtEmpEmail    = findViewById(R.id.edtEmpEmail);
        edtEmpPassword = findViewById(R.id.edtEmpPassword);
        edtEmpPhone    = findViewById(R.id.edtEmpPhone);

        spEmpSpecialty = findViewById(R.id.spEmpSpecialty);
        spEmpGender    = findViewById(R.id.spEmpGender);

        btnEmpAdd     = findViewById(R.id.btnEmpAdd);
        btnEmpUpdate  = findViewById(R.id.btnEmpUpdate);
        btnEmpDelete  = findViewById(R.id.btnEmpDelete);
        btnEmpSearch  = findViewById(R.id.btnEmpSearch);
        btnEmpShowAll = findViewById(R.id.btnEmpShowAll);

        recyclerEmployees = findViewById(R.id.recyclerEmployees);

        tabLogout = findViewById(R.id.tabLogout);
        tabMain   = findViewById(R.id.tabMain);
        tabLogin  = findViewById(R.id.tabLogin);

        setupGender();
        loadSpecialties();
        setupNav();

        btnEmpAdd.setOnClickListener(v -> addEmployee());
        btnEmpUpdate.setOnClickListener(v -> updateEmployee());
        btnEmpDelete.setOnClickListener(v -> deleteEmployee());
        btnEmpSearch.setOnClickListener(v -> searchEmployee());
        btnEmpShowAll.setOnClickListener(v -> loadAllEmployees());
    }

    private void setupGender() {
        String[] genders = {"Male", "Female"};
        ArrayAdapter<String> a = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, genders);
        spEmpGender.setAdapter(a);
    }

    private void loadSpecialties() {
        ArrayList<String> specialties = db.getAllSpecialties();
        if (specialties.isEmpty()) specialties.add("No Services Found");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                specialties
        );
        spEmpSpecialty.setAdapter(adapter);
    }

    private void setupNav() {
        tabMain.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        tabLogin.setOnClickListener(v -> startActivity(new Intent(this, login.class)));

        tabLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure?")
                    .setPositiveButton("Yes", (d, w) -> {
                        startActivity(new Intent(this, login.class));
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }


    private void addEmployee() {

        String name      = edtEmpName.getText().toString().trim();
        String email     = edtEmpEmail.getText().toString().trim();
        String password  = edtEmpPassword.getText().toString().trim();
        String specialty = spEmpSpecialty.getSelectedItem().toString();
        String phone     = edtEmpPhone.getText().toString().trim();
        String gender    = spEmpGender.getSelectedItem().toString();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Name, Email & Password are required", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase dbWrite = db.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", name);
        cv.put("email", email);
        cv.put("password", password);
        cv.put("specialty", specialty);
        cv.put("phone", phone);
        cv.put("gender", gender);

        long res = dbWrite.insert("employees", null, cv);

        if (res != -1) {
            new AlertDialog.Builder(this)
                    .setTitle("Success")
                    .setMessage("Employee inserted!")
                    .setPositiveButton("OK", null)
                    .show();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Failed inserting employee.")
                    .setPositiveButton("OK", null)
                    .show();
        }
    }


    private void updateEmployee() {

        if (edtEmpId.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter employee ID", Toast.LENGTH_SHORT).show();
            return;
        }

        int id           = Integer.parseInt(edtEmpId.getText().toString());
        String name      = edtEmpName.getText().toString().trim();
        String email     = edtEmpEmail.getText().toString().trim();
        String password  = edtEmpPassword.getText().toString().trim();
        String specialty = spEmpSpecialty.getSelectedItem().toString();
        String phone     = edtEmpPhone.getText().toString().trim();
        String gender    = spEmpGender.getSelectedItem().toString();

        new AlertDialog.Builder(this)
                .setTitle("Confirm Update")
                .setMessage("Update this employee?")
                .setPositiveButton("OK", (dialog, which) -> {

                    SQLiteDatabase dbWrite = db.getWritableDatabase();
                    ContentValues cv = new ContentValues();

                    cv.put("name", name);
                    cv.put("email", email);
                    cv.put("password", password);
                    cv.put("specialty", specialty);
                    cv.put("phone", phone);
                    cv.put("gender", gender);

                    int rows = dbWrite.update("employees", cv, "id=?",
                            new String[]{String.valueOf(id)});

                    if (rows > 0) {
                        Toast.makeText(this, "Employee updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteEmployee() {

        if (edtEmpId.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter employee ID", Toast.LENGTH_SHORT).show();
            return;
        }

        int id = Integer.parseInt(edtEmpId.getText().toString());

        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Delete this employee?")
                .setPositiveButton("OK", (dialog, which) -> {

                    SQLiteDatabase dbWrite = db.getWritableDatabase();
                    int rows = dbWrite.delete("employees", "id=?",
                            new String[]{String.valueOf(id)});

                    if (rows > 0) {
                        Toast.makeText(this, "Employee deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void searchEmployee() {

        if (edtEmpId.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter ID", Toast.LENGTH_SHORT).show();
            return;
        }

        int id = Integer.parseInt(edtEmpId.getText().toString());

        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT name, email, password, specialty, phone, gender FROM employees WHERE id=?",
                new String[]{String.valueOf(id)}
        );

        if (c.moveToFirst()) {

            edtEmpName.setText(c.getString(0));
            edtEmpEmail.setText(c.getString(1));      // NEW
            edtEmpPassword.setText(c.getString(2));   // NEW

            setSpecialtySelection(spEmpSpecialty, c.getString(3));
            edtEmpPhone.setText(c.getString(4));

            spEmpGender.setSelection(
                    c.getString(5).equals("Male") ? 0 : 1
            );
        }

        c.close();
    }

    private void setSpecialtySelection(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }


    private void loadAllEmployees() {
        employeeList = db.getAllEmployees();
        adapter = new EmployeesAdapter(this, employeeList);
        recyclerEmployees.setLayoutManager(new LinearLayoutManager(this));
        recyclerEmployees.setAdapter(adapter);
    }
}
