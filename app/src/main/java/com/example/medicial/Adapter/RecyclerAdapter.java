package com.example.medicial.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicial.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private Context context;
    private ArrayList medicine_name, amount, time, date;
    int monthInt;

    public RecyclerAdapter(Context context, ArrayList medicine_name, ArrayList amount, ArrayList time, ArrayList date) {
        this.context = context;
        this.medicine_name = medicine_name;
        this.amount = amount;
        this.time = time;
        this.date = date;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reminder_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.med_name.setText(String.valueOf(medicine_name.get(position)));
        holder.med_amount.setText(String.valueOf(amount.get(position)));
        holder.med_time.setText(String.valueOf(time.get(position)));

        try {
            String currentString = String.valueOf(date.get(position));
            String[] separated = currentString.split("/");

            String day = separated[0];
            String month = separated[1]; // get month from date string
            monthInt = Integer.parseInt(month); // convert month string to integer
            String setMonth = getMonthFormat(monthInt); // get month format string

            String year = separated[2];

            holder.date_day.setText(day);
            holder.date_month.setText(setMonth);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return medicine_name.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView med_name, med_amount, med_time, date_day, date_month, date_year;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            med_name = itemView.findViewById(R.id.txtv_medName);
            med_amount = itemView.findViewById(R.id.txtv_amount);
            med_time = itemView.findViewById(R.id.txtv_time);
//            date_day = itemView.findViewById(R.id.txtv_day_name);
            date_day = itemView.findViewById(R.id.txtv_day);
            date_month = itemView.findViewById(R.id.txtv_month);
        }
    }

    private String getMonthFormat(int month) {
        if (month == 1)
            return "Jan";
        if (month == 2)
            return "Feb";
        if (month == 3)
            return "Mar";
        if (month == 4)
            return "Apr";
        if (month == 5)
            return "May";
        if (month == 6)
            return "Jun";
        if (month == 7)
            return "Jul";
        if (month == 8)
            return "Aug";
        if (month == 9)
            return "Sep";
        if (month == 10)
            return "Oct";
        if (month == 11)
            return "Nov";
        if (month == 12)
            return "Dec";

        //default should never happen
        return "Jan";
    }
}
