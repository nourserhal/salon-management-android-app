package com.example.noor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class ViewAppointmentsActivity extends AppCompatActivity {

    RecyclerView recyclerAppointments;
    myDBClass db;
    ArrayList<AppointmentModel> list;
    AppointmentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointments);

        recyclerAppointments = findViewById(R.id.recyclerAppointments);
        db = new myDBClass(this);

        // initialize list
        list = new ArrayList<>();

        // setup adapter
        adapter = new AppointmentsAdapter(this, list);
        recyclerAppointments.setLayoutManager(new LinearLayoutManager(this));
        recyclerAppointments.setAdapter(adapter);

        // load data
        loadAppointments();
    }

    private void loadAppointments() {
        // clear old data
        list.clear();

        // get list directly from DB (because DB returns ArrayList already)
        ArrayList<AppointmentModel> temp = db.getAllAppointments();

        // add all loaded appointments to your list
        list.addAll(temp);

        // refresh adapter
        adapter.notifyDataSetChanged();
    }
}
