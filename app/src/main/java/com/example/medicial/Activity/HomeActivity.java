package com.example.medicial.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicial.NavigationDrawer.CalendarActivity;
import com.example.medicial.Database.DBHelper;
import com.example.medicial.NavigationDrawer.ProfileActivity;
import com.example.medicial.R;
import com.example.medicial.Adapter.RecyclerAdapter;
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
    ArrayList<String> id, medicine_name, amount, time, date;
    DBHelper dbHelper = new DBHelper(this);
    RecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // {Full screen activity}
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // {ToolBar}
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // {DrawerLayout}
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.syncState();

        // {NavigationView}
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        // navigationView.setCheckedItem(R.id.nav_home);

        // {FloatingActionButton}
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ReminderActivity.class);
                startActivity(intent);
            }
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

        // {RecyclerView}
        id = new ArrayList<>();
        medicine_name = new ArrayList<>();
        amount = new ArrayList<>();
        time = new ArrayList<>();
        date = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerAdapter = new RecyclerAdapter(this, id, medicine_name, amount, time, date);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DisplayData();
    }


    private void DisplayData() {
        Cursor mCursor = dbHelper.getMedicineData();
        Cursor aCursor = dbHelper.getAlertData();

        if (mCursor.getCount() != 0) {
            while (mCursor.moveToNext()) {
                id.add(String.valueOf(mCursor.getInt(0)));
                medicine_name.add(mCursor.getString(1));
                amount.add(mCursor.getString(2));
            }
        }

        if (aCursor.getCount() != 0) {
            while (aCursor.moveToNext()) {
                time.add(aCursor.getString(1));
                date.add(aCursor.getString(2));
            }
        }

        mCursor.close();
        aCursor.close();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent intent_home = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent_home);
                break;

            case R.id.nav_calendar:
                Intent intent_calendar = new Intent(getApplicationContext(), CalendarActivity.class);
                startActivity(intent_calendar);
                break;

            case R.id.nav_settings:
                break;

            case R.id.nav_profile:
                Intent intent_profile = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent_profile);
                break;

            case R.id.nav_logout:
                Intent intent_logout = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent_logout);
                finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setSupportActionBar(Toolbar toolbar) {
    }

}