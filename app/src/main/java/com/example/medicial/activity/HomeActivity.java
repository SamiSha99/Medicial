package com.example.medicial.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicial.adapter.ReminderAdapter;
import com.example.medicial.database.DBHelper;
import com.example.medicial.model.Data;
import com.example.medicial.navigationDrawer.CalendarActivity;
import com.example.medicial.navigationDrawer.ProfileActivity;
import com.example.medicial.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    ArrayList<Data> arrayList;
    DBHelper dbHelper = new DBHelper(this);
    ReminderAdapter reminderAdapter;
    TextView displayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
        displayData();
    }

    private void init() {
        // {ToolBar}
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // {Hook id}
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        floatingActionButton = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recyclerView);

        // {DrawerLayout}
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.syncState();

        // {NavigationView}
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();

        // {FloatingActionButton}
        floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, MedicineActivity.class);
            startActivity(intent);
        });

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                // Translate the FAB horizontally by the slide offset multiplied by the width of the FAB
                floatingActionButton.setTranslationX(slideOffset * 250);
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                // Hide/Disable the FAB
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                // Show/Enable the FAB
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                // Nothing to do
            }
        });

        // {Receive username and set it in nav header}
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("username", Context.MODE_PRIVATE);
        String _StrUser = sharedPreferences.getString("key_user", "");
        View headerView = navigationView.getHeaderView(0);
        displayName = headerView.findViewById(R.id.txtv_setUserName);
        displayName.setText(String.format("Welcome %s", _StrUser));
    }

    @SuppressLint("NotifyDataSetChanged")
    private void displayData() {
        arrayList = dbHelper.getReminderData();
        reminderAdapter = new ReminderAdapter(this, arrayList);
        recyclerView.setAdapter(reminderAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reminderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(HomeActivity.this)
                    .setMessage("Are you sure to Logout?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, i) -> logout())
                    .setNegativeButton("No", null)
                    .show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_home) {
            Intent intent_home = new Intent(HomeActivity.this, HomeActivity.class);
            startActivity(intent_home);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        } else if (item.getItemId() == R.id.nav_calendar) {
            Intent intent_calendar = new Intent(HomeActivity.this, CalendarActivity.class);
            startActivity(intent_calendar);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        } else if (item.getItemId() == R.id.nav_profile) {
            Intent intent_profile = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent_profile);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        } else if (item.getItemId() == R.id.nav_logout) {
            logout();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent_logout = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent_logout);
        finish();
    }
}