package com.example.noor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomersAdapter extends RecyclerView.Adapter<CustomersAdapter.MyViewHolder> {

    Context context;
    ArrayList<CustomerModel> list;

    public CustomersAdapter(Context context, ArrayList<CustomerModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.customer_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CustomerModel c = list.get(position);

        holder.txtId.setText("ID: " + c.getId());
        holder.txtName.setText("Name: " + c.getName());
        holder.txtEmail.setText("Email: " + c.getEmail());
        holder.txtPhone.setText("Phone: " + c.getPhone());
        holder.txtGender.setText("Gender: " + c.getGender());
        holder.txtDob.setText("DOB: " + c.getDob());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtId, txtName, txtEmail, txtPhone, txtGender, txtDob;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtId = itemView.findViewById(R.id.txtCustId);
            txtName = itemView.findViewById(R.id.txtCustName);
            txtEmail = itemView.findViewById(R.id.txtCustEmail);
            txtPhone = itemView.findViewById(R.id.txtCustPhone);
            txtGender = itemView.findViewById(R.id.txtCustGender);
            txtDob = itemView.findViewById(R.id.txtCustDob);
        }
    }
}
