package com.example.noor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.SerHolder> {

    Context context;
    ArrayList<ServiceModel> list;

    public ServicesAdapter(Context context, ArrayList<ServiceModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.service_item, parent, false);
        return new SerHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SerHolder h, int pos) {
        ServiceModel s = list.get(pos);

        h.txtId.setText("ID: " + s.getId());
        h.txtName.setText("Service: " + s.getName());
        h.txtPrice.setText("Price: " + s.getPrice());
        h.txtDuration.setText("Duration: " + s.getDuration());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class SerHolder extends RecyclerView.ViewHolder {
        TextView txtId, txtName, txtPrice, txtDuration;

        public SerHolder(@NonNull View v) {
            super(v);
            txtId = v.findViewById(R.id.txtServiceId);
            txtName = v.findViewById(R.id.txtServiceName);
            txtPrice = v.findViewById(R.id.txtServicePrice);
            txtDuration = v.findViewById(R.id.txtServiceDuration);
        }
    }
}
