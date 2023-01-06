package com.example.medicial.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicial.Database.DBHelper;
import com.example.medicial.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private Context context;
    private ArrayList id, medicine_name, amount, time, date;
    DBHelper dbHelper;
    int monthInt;

    public RecyclerAdapter(Context context, ArrayList id, ArrayList medicine_name, ArrayList amount, ArrayList time, ArrayList date) {
        this.context = context;
        this.id = id;
        this.medicine_name = medicine_name;
        this.amount = amount;
        this.time = time;
        this.date = date;
        dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reminder_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.med_id.setText(String.valueOf(id.get(position)));
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

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        TextView med_id, med_name, med_amount, med_time, date_day, date_month, date_year;
        ImageButton popup_option;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // med_id hidden in the activity and set to "gone"
            med_id = itemView.findViewById(R.id.txtv_medId);
            med_name = itemView.findViewById(R.id.txtv_medName);
            med_amount = itemView.findViewById(R.id.txtv_amount);
            med_time = itemView.findViewById(R.id.txtv_time);
            date_day = itemView.findViewById(R.id.txtv_day);
            date_month = itemView.findViewById(R.id.txtv_month);

            popup_option = itemView.findViewById(R.id.options);
            popup_option.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            ShowPopupMenu(view);
        }

        private void ShowPopupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.popup_option_menu);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_show:

                    break;
                case R.id.action_delete:
                    // Log.d("NAME OF MEDICINE", "med name? " + med_name.getText());
                    int position = getLayoutPosition();
                    // Remove the item
                    int i = Integer.parseInt(med_id.getText().toString());
                    dbHelper.removeMedicineData(i);
                    medicine_name.remove(position);
                    // Notify the adapter that an item has been removed
                    notifyItemRemoved(position);
                    return true;

                case R.id.action_update:
                    break;

                default:
                    return false;
            }
            return true;
        }
    }

    private String getMonthFormat(int month) {
        switch (month) {
            default:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
        }
    }
}
