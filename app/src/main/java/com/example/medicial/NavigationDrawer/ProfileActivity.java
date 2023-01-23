package com.example.medicial.NavigationDrawer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.medicial.Adapter.UserAdapter;
import com.example.medicial.Database.DBHelper;
import com.example.medicial.Model.User;
import com.example.medicial.R;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    Toolbar toolbar;
    ActionBar actionBar;
    RecyclerView recyclerView;
    DBHelper dbHelper = new DBHelper(this);
    UserAdapter userAdapter;
    ArrayList<User> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        ShowUserData();
    }

    private void init() {
        // {Toolbar}
        toolbar = findViewById(R.id.prof_toolbar);
        if (toolbar != null) setSupportActionBar(toolbar);

        // {Setting up action bar}
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back));
            actionBar.setDisplayShowTitleEnabled(false);
        }

        // {Hook id}
        recyclerView = findViewById(R.id.user_recyclerView);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void ShowUserData() {
        arrayList = dbHelper.getUserData();
        userAdapter = new UserAdapter(this, arrayList);
        recyclerView.setAdapter(userAdapter);
        userAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}