package com.example.medicial.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicial.model.User;
import com.example.medicial.R;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    private final Context context;
    ArrayList<User> arrayList;

    public UserAdapter(Context context, ArrayList<User> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder._UserName.setText(arrayList.get(position).getUsername());
        holder._FirstName.setText(arrayList.get(position).getFirstname());
        holder._LastName.setText(arrayList.get(position).getLastname());
        holder._Email.setText(arrayList.get(position).getEmail());
        holder._Password.setText(arrayList.get(position).getPassword());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public Context getContext() {
        return context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        EditText _UserName, _FirstName, _LastName, _Email, _Password;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            _UserName = itemView.findViewById(R.id.edt_prof_username);
            _FirstName = itemView.findViewById(R.id.edt_prof_firstname);
            _LastName = itemView.findViewById(R.id.edt_prof_lastname);
            _Email = itemView.findViewById(R.id.edt_prof_email);
            _Password = itemView.findViewById(R.id.edt_prof_password);
        }
    }
}
