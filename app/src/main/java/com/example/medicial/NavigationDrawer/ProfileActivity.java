package com.example.medicial.NavigationDrawer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ListView;

import com.example.medicial.Database.DBHelper;
import com.example.medicial.Adapter.ListAdapter;
import com.example.medicial.Model.User;
import com.example.medicial.R;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    Toolbar toolbar;
    ActionBar actionBar;
    ListView listView;
    DBHelper dbHelper = new DBHelper(this);
    ListAdapter listAdapter;
    ArrayList<User> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // {Full screen activity}
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // {Toolbar}
        toolbar = findViewById(R.id.prof_toolbar);
        if(toolbar != null) setSupportActionBar(toolbar);

        // {Setting up action bar}
        actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back));
            actionBar.setDisplayShowTitleEnabled(false);
        }

        listView = findViewById(R.id.profile_list);
        ShowUserData();
    }

    public void ShowUserData() {
        arrayList = dbHelper.GetUserData();
        listAdapter = new ListAdapter(this, arrayList);
        listView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return super.onSupportNavigateUp();
//    }
}