package com.example.medicial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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

        TextView first_name = (TextView) view.findViewById(R.id.txtv_prof_firstname);
        TextView last_name = (TextView) view.findViewById(R.id.txtv_prof_lastname);
        TextView email = (TextView) view.findViewById(R.id.txtv_prof_email);
        TextView password = (TextView) view.findViewById(R.id.txtv_prof_password);

        User user = arrayList.get(position);
            first_name.setText(user.getFirstname());
            last_name.setText(user.getLastname());
            email.setText(user.getEmail());
            password.setText(user.getPassword());

        return view;
    }
}
