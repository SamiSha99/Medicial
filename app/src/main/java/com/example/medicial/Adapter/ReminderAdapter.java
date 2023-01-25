package com.example.medicial.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicial.Database.DBHelper;
import com.example.medicial.Database.UpdateActivity;
import com.example.medicial.Model.Data;
import com.example.medicial.R;

import java.util.ArrayList;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.MyViewHolder> {
    private final Context context;
    ArrayList<Data> arrayList;
    DBHelper dbHelper;
    int monthInt;
    Dialog dialog;

    public ReminderAdapter(Context context, ArrayList<Data> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        dbHelper = new DBHelper(context);
        dialog = new Dialog(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reminder_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.med_id.setText(String.valueOf(arrayList.get(position).get_Med_Id()));
        holder.med_name.setText(arrayList.get(position).get_Med_Name());
        holder.med_amount.setText(String.valueOf(arrayList.get(position).get_Med_Amount()));
        holder.med_desc.setText(arrayList.get(position).get_Med_Desc());
        holder.med_image.setImageURI(Uri.parse(arrayList.get(position).get_Med_Image()));
        holder.med_time.setText(arrayList.get(position).get_Time());

        try {
            String currentString = String.valueOf(arrayList.get(position).get_Date());
            String[] separated = currentString.split("-");

            String day = separated[2];
            String month = separated[1]; // get month from date string
            monthInt = Integer.parseInt(month); // convert month string to integer
            String setMonth = getMonthFormat(monthInt); // get month format string

            String year = separated[0];

            holder.date_day.setText(day);
            holder.date_month.setText(setMonth);
            holder.date_year.setText(year);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView med_id, med_name, med_amount, med_desc, med_time, date_day, date_month, date_year;
        ImageButton popup_Menu;
        ImageView med_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // med_id hidden in the activity and set to "gone"
            med_id = itemView.findViewById(R.id.txtv_medId);
            med_name = itemView.findViewById(R.id.txtv_medName);
            med_amount = itemView.findViewById(R.id.txtv_amount);
            med_desc = itemView.findViewById(R.id.txtv_desc);
            med_image = itemView.findViewById(R.id.imgv);

            med_time = itemView.findViewById(R.id.txtv_time);
            date_day = itemView.findViewById(R.id.txtv_day);
            date_month = itemView.findViewById(R.id.txtv_month);
            date_year = itemView.findViewById(R.id.txtv_year);

            popup_Menu = itemView.findViewById(R.id.options);
            popup_Menu.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            ShowPopupMenu(view);
        }

        private void ShowPopupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.popup_option_menu);
            int position = getLayoutPosition();

            popupMenu.setOnMenuItemClickListener(menuItem -> {

                if (menuItem.getItemId() == R.id.action_show) {
                    dialog.setContentView(R.layout.dialog_show_image);
                    ImageView med_image = dialog.findViewById(R.id.imgv_med_img);

                    med_image.setImageURI(Uri.parse(arrayList.get(position).get_Med_Image()));

                    ImageView img_close = dialog.findViewById(R.id.imgv_close);
                    img_close.setOnClickListener(view1 -> dialog.dismiss());

                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                    return true;

                } else if (menuItem.getItemId() == R.id.action_delete) {
                    // Remove the item
                    int i = Integer.parseInt(med_id.getText().toString());
                    dbHelper.removeMedicineData(i);
                    arrayList.remove(position);
                    // Notify the adapter that an item has been removed
                    notifyItemRemoved(position);
                    return true;

                } else if (menuItem.getItemId() == R.id.action_update) {
                    Intent intent_update = new Intent(context, UpdateActivity.class);
                    intent_update.putExtra("med_id", String.valueOf(med_id.getText()));
                    intent_update.putExtra("med_name", med_name.getText().toString());
                    intent_update.putExtra("med_amount", med_amount.getText().toString());
                    context.startActivity(intent_update);
                    return true;

                }
                return false;
            });
            popupMenu.show();
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
