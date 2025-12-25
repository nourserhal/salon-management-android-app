package com.example.noor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<AppointmentModel> list;
    private myDBClass db;

    public AppointmentsAdapter(Context context, ArrayList<AppointmentModel> list) {
        this.context = context;
        this.list = list;
        this.db = new myDBClass(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.appointment_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {

        AppointmentModel a = list.get(pos);

        h.txtId.setText("ID: " + a.getId());
        h.txtService.setText("Service: " + a.getServiceName());
        h.txtEmployee.setText("Employee: " + a.getEmployeeName());
        h.txtCustomer.setText("Customer: " + a.getCustomerName());
        h.txtDate.setText("Date: " + a.getDate());
        h.txtTime.setText("Time: " + a.getTime());
        h.txtStatus.setText("Status: " + a.getStatus());

        // 🔥 Update status immediately when button is clicked
        h.btnMarkCompleted.setOnClickListener(v -> {

            boolean ok = db.updateAppointmentStatus(a.getId(), "Completed");

            if (ok) {
                a.status = "Completed";  // Update model
                h.txtStatus.setText("Status: Completed"); // Update UI immediately

                Toast.makeText(context, "Marked as Completed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtId, txtService, txtEmployee, txtCustomer, txtDate, txtTime, txtStatus;
        Button btnMarkCompleted;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtId = itemView.findViewById(R.id.txtId);
            txtService = itemView.findViewById(R.id.txtService);
            txtEmployee = itemView.findViewById(R.id.txtEmployee);
            txtCustomer = itemView.findViewById(R.id.txtCustomer);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtStatus = itemView.findViewById(R.id.txtStatus);

            btnMarkCompleted = itemView.findViewById(R.id.btnMarkCompleted);
        }
    }
}
