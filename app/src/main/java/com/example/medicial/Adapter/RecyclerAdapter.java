package com.example.medicial.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.example.medicial.Database.ShowActivity;
import com.example.medicial.Database.UpdateActivity;
import com.example.medicial.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private Context context;
    private ArrayList id, medicine_name, amount, image, time, date;
    DBHelper dbHelper;
    int monthInt;
    Dialog dialog;

    public RecyclerAdapter(Context context, ArrayList id, ArrayList medicine_name, ArrayList amount, ArrayList image, ArrayList time, ArrayList date) {
        this.context = context;
        this.id = id;
        this.medicine_name = medicine_name;
        this.amount = amount;
        this.image = image;
        this.time = time;
        this.date = date;
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
        holder.med_id.setText(String.valueOf(id.get(position)));
        holder.med_name.setText(String.valueOf(medicine_name.get(position)));
        holder.med_amount.setText(String.valueOf(amount.get(position)));
        holder.med_time.setText(String.valueOf(time.get(position)));
        holder.image.setImageURI(Uri.parse(image.get(position).toString()));

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

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView med_id, med_name, med_amount, med_time, date_day, date_month, date_year;
        ImageButton popup_Menu;
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // med_id hidden in the activity and set to "gone"
            med_id = itemView.findViewById(R.id.txtv_medId);
            med_name = itemView.findViewById(R.id.txtv_medName);
            med_amount = itemView.findViewById(R.id.txtv_amount);
            med_time = itemView.findViewById(R.id.txtv_time);
            date_day = itemView.findViewById(R.id.txtv_day);
            date_month = itemView.findViewById(R.id.txtv_month);
            image = itemView.findViewById(R.id.imgv);

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

            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.action_show:
                        Intent show_intent = new Intent(context, ShowActivity.class);
//                        show_intent.putExtra("image", Uri.parse(image.get(pos)));
                        context.startActivity(show_intent);
                        return true;

                    case R.id.action_delete:
                        int position = getLayoutPosition();
                        // Remove the item
                        int i = Integer.parseInt(med_id.getText().toString());
                        dbHelper.removeMedicineData(i);
                        medicine_name.remove(position);
                        // Notify the adapter that an item has been removed
                        notifyItemRemoved(position);
                        return true;

                    case R.id.action_update:
                        Intent intent = new Intent(context, UpdateActivity.class);
                        intent.putExtra("med_id", String.valueOf(med_id));
                        context.startActivity(intent);
                        return true;

                    default:
                        return false;
                }
            });
            popupMenu.show();
        }
    }

//    private void ShowImageDialog() {
//        dialog.setContentView(R.layout.dialog_show_image);
//        ImageView med_image = dialog.findViewById(R.id.imgv_med_img);
//        Uri imguri = Uri.parse(image.getDrawable().toString());
//        med_image.setImageURI(imguri);
//
//        ImageView img_close = dialog.findViewById(R.id.imgv_close);
//
//        img_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.show();
//
//    }

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
