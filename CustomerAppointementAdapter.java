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

public class CustomerAppointementAdapter extends RecyclerView.Adapter<CustomerAppointementAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CustomerAppointementModel> list;
    private myDBClass db;

    public CustomerAppointementAdapter(Context context, ArrayList<CustomerAppointementModel> list) {
        this.context = context;
        this.list = list;
        this.db = new myDBClass(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.appointment_item_cust, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {

        CustomerAppointementModel a = list.get(pos);

        h.txtId.setText("ID: " + a.getId());
        h.txtService.setText("Service: " + a.getServiceName());
        h.txtEmployee.setText("Employee: " + a.getEmployeeName());
        h.txtCustomer.setText("Customer: " + a.getCustomerName());
        h.txtDate.setText("Date: " + a.getDate());
        h.txtTime.setText("Time: " + a.getTime());
        h.txtStatus.setText("Status: " + a.getStatus());
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

        }
    }
}
