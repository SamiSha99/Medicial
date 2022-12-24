package com.example.medicial;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context context;
    private ArrayList medicine_name, amount;


    public MyAdapter(Context context, ArrayList medicine_name, ArrayList amount) {
        this.context = context;
        this.medicine_name = medicine_name;
        this.amount = amount;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reminder_card,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.med_name.setText(String.valueOf(medicine_name.get(position)));
        holder.med_amount.setText(String.valueOf(amount.get(position)));
    }

    @Override
    public int getItemCount() {
        return medicine_name.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView med_name, med_amount;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            med_name = itemView.findViewById(R.id.txtv_medName);
            med_amount = itemView.findViewById(R.id.txtv_amount);
        }
    }
}
