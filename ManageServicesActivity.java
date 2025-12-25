package com.example.noor;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ManageServicesActivity extends AppCompatActivity {

    EditText edtServiceId, edtServiceName, edtServicePrice, edtServiceDuration;
    Spinner spServiceEmployee;

    Button btnServiceAdd, btnServiceUpdate, btnServiceDelete, btnServiceSearch, btnServiceShowAll;

    TextView tabLogout, tabMain, tabLogin;

    RecyclerView recyclerServices;
    ServicesAdapter adapter;
    ArrayList<ServiceModel> serviceList;

    ArrayList<String> employeeNames;


    myDBClass db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_services);

        db = new myDBClass(this);

        edtServiceId = findViewById(R.id.edtServiceId);
        edtServiceName = findViewById(R.id.edtServiceName);
        edtServicePrice = findViewById(R.id.edtServicePrice);
        edtServiceDuration = findViewById(R.id.edtServiceDuration);

        btnServiceAdd = findViewById(R.id.btnServiceAdd);
        btnServiceUpdate = findViewById(R.id.btnServiceUpdate);
        btnServiceDelete = findViewById(R.id.btnServiceDelete);
        btnServiceSearch = findViewById(R.id.btnServiceSearch);
        btnServiceShowAll = findViewById(R.id.btnServiceShowAll);

        recyclerServices = findViewById(R.id.recyclerServices);

        tabLogout = findViewById(R.id.tabLogout);
        tabMain = findViewById(R.id.tabMain);
        tabLogin = findViewById(R.id.tabLogin);

        setupNav();

        btnServiceAdd.setOnClickListener(v -> addService());
        btnServiceUpdate.setOnClickListener(v -> updateService());
        btnServiceDelete.setOnClickListener(v -> deleteService());
        btnServiceSearch.setOnClickListener(v -> searchService());
        btnServiceShowAll.setOnClickListener(v -> loadAllServices());
    }

    private void setupNav() {
        tabMain.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        tabLogin.setOnClickListener(v -> startActivity(new Intent(this, login.class)));

        tabLogout.setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", (d, w) -> {
                            startActivity(new Intent(this, login.class));
                            finish();
                        })
                        .setNegativeButton("No", null)
                        .show());
    }


    private void addService() {
        String name = edtServiceName.getText().toString();
        String priceStr = edtServicePrice.getText().toString();
        String durationStr = edtServiceDuration.getText().toString();

        if (name.isEmpty() || priceStr.isEmpty() || durationStr.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        String duration = durationStr;

        new AlertDialog.Builder(this)
                .setTitle("Confirm Insert")
                .setMessage("Do you want to add this service?")
                .setPositiveButton("OK", (dialog, which) -> {

                    long insertedId = db.insertService(name, price, duration);

                    if (insertedId != -1) {
                        new AlertDialog.Builder(this)
                                .setTitle("Success")
                                .setMessage("Service added successfully!")
                                .setPositiveButton("OK", null)
                                .show();
                    } else {
                        new AlertDialog.Builder(this)
                                .setTitle("Error")
                                .setMessage("Failed to add service.")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    private void updateService() {
        if (edtServiceId.getText().toString().isEmpty()) {
            Toast.makeText(this, "Service ID is required", Toast.LENGTH_SHORT).show();
            return;
        }

        int id = Integer.parseInt(edtServiceId.getText().toString());

        String name = edtServiceName.getText().toString();
        String priceStr = edtServicePrice.getText().toString();
        String durationStr = edtServiceDuration.getText().toString();

        if (name.isEmpty() || priceStr.isEmpty() || durationStr.isEmpty()) {
            Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        String duration = durationStr;

        new AlertDialog.Builder(this)
                .setTitle("Confirm Update")
                .setMessage("Are you sure you want to update this service?")
                .setPositiveButton("OK", (dialog, which) -> {

                    boolean ok = db.updateService(id, name, price, duration);

                    if (ok) {
                        new AlertDialog.Builder(this)
                                .setTitle("Updated")
                                .setMessage("Service updated successfully!")
                                .setPositiveButton("OK", null)
                                .show();
                    } else {
                        new AlertDialog.Builder(this)
                                .setTitle("Error")
                                .setMessage("Failed to update service.")
                                .setPositiveButton("OK", null)
                                .show();
                    }

                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    private void deleteService() {
        if (edtServiceId.getText().toString().isEmpty()) {
            Toast.makeText(this, "Service ID required", Toast.LENGTH_SHORT).show();
            return;
        }

        int id = Integer.parseInt(edtServiceId.getText().toString());

        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this service?")
                .setPositiveButton("OK", (dialog, which) -> {

                    boolean ok = db.deleteService(id);

                    if (ok) {
                        new AlertDialog.Builder(this)
                                .setTitle("Deleted")
                                .setMessage("Service deleted!")
                                .setPositiveButton("OK", null)
                                .show();
                    } else {
                        new AlertDialog.Builder(this)
                                .setTitle("Error")
                                .setMessage("Failed to delete service.")
                                .setPositiveButton("OK", null)
                                .show();
                    }

                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void searchService() {

        if (edtServiceId.getText().toString().isEmpty()) {
            Toast.makeText(this, "Service ID required", Toast.LENGTH_SHORT).show();
            return;
        }

        int id = Integer.parseInt(edtServiceId.getText().toString());

        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT name, price, duration FROM services WHERE id=?",
                new String[]{String.valueOf(id)}
        );

        if (c.moveToFirst()) {
            edtServiceName.setText(c.getString(0));
            edtServicePrice.setText(c.getString(1));
            edtServiceDuration.setText(c.getString(2));

        } else {
            Toast.makeText(this, "Service not found", Toast.LENGTH_SHORT).show();
        }

        c.close();
    }


    private void loadAllServices() {
        serviceList = db.getAllServices();
        adapter = new ServicesAdapter(this, serviceList);
        recyclerServices.setLayoutManager(new LinearLayoutManager(this));
        recyclerServices.setAdapter(adapter);
    }
}
