package com.example.medicial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    Context context;
    ArrayList<User> arrayList;

    public ListAdapter(Context context, ArrayList<User> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return this.arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.item_profile_listview, null);

        EditText user_name = (EditText) view.findViewById(R.id.edt_prof_username);
        EditText first_name = (EditText) view.findViewById(R.id.edt_prof_firstname);
        EditText last_name = (EditText) view.findViewById(R.id.edt_prof_lastname);
        EditText email = (EditText) view.findViewById(R.id.edt_prof_email);
        EditText password = (EditText) view.findViewById(R.id.edt_prof_password);

        User user = arrayList.get(position);
        user_name.setText(user.getUsername());
        first_name.setText(user.getFirstname());
        last_name.setText(user.getLastname());
        email.setText(user.getEmail());
        password.setText(user.getPassword());

        return view;
    }
}
