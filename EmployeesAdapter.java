package com.example.noor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.EmpHolder> {

    Context context;
    ArrayList<EmployeeModel> list;

    public EmployeesAdapter(Context context, ArrayList<EmployeeModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public EmpHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.employee_item, parent, false);
        return new EmpHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EmpHolder h, int pos) {
        EmployeeModel e = list.get(pos);

        h.txtId.setText("ID: " + e.getId());
        h.txtName.setText("Name: " + e.getName());
        h.txtEmail.setText("Email: " + e.getEmail());
        h.txtSpec.setText("Specialty: " + e.getSpecialty());
        h.txtPhone.setText("Phone: " + e.getPhone());
        h.txtGender.setText("Gender: " + e.getGender());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class EmpHolder extends RecyclerView.ViewHolder {
        TextView txtId, txtName, txtEmail, txtSpec, txtPhone, txtGender;

        public EmpHolder(@NonNull View v) {
            super(v);

            txtId = v.findViewById(R.id.txtEmpId);
            txtName = v.findViewById(R.id.txtEmpName);
            txtEmail = v.findViewById(R.id.txtEmpEmail);   // NEW
            txtSpec = v.findViewById(R.id.txtEmpSpecialty);
            txtPhone = v.findViewById(R.id.txtEmpPhone);
            txtGender = v.findViewById(R.id.txtEmpGender);
        }
    }
}
