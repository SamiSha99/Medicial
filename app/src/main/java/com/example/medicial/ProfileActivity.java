package com.example.medicial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    ListView listView;
    DBHelper dbHelper = new DBHelper(this);
    ListAdapter listAdapter;
    ArrayList<User> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

//       {Full screen activity}
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        listView = findViewById(R.id.profile_list);
        ArrayList<User> arrayList = new ArrayList<>();
        ShowUserData();

    }

    public void ShowUserData() {
        arrayList = dbHelper.GetUserData();
        listAdapter = new ListAdapter(this, arrayList);
        listView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }
}