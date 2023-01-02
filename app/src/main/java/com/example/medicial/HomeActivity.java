package com.example.medicial;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    ArrayList<String> medicine_name, amount, time, date;
    DBHelper dbHelper = new DBHelper(this);
    RecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//       {Full screen activity}
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        {ToolBar}
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        {DrawerLayout}
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.syncState();

//        {NavigationView}
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        // navigationView.setCheckedItem(R.id.nav_home);

//        {To set username in navigation header}    // Not optimal some cases will disappear
//        Bundle bundle = getIntent().getExtras();
//
//        if (bundle != null) {
//            String setUsername = bundle.getString("key");
//            boolean chk = dbHelper.CheckUserName(setUsername);
//            if (setUsername.equals(chk)){
//                View headerView = navigationView.getHeaderView(0);
//                TextView navUsername = headerView.findViewById(R.id.txtv_setUserName);
//                navUsername.setText(setUsername);
//            }
//        }

//        {FloatingActionButton}
        floatingActionButton = findViewById(R.id.extended_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddReminderActivity.class);
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

//        {RecyclerView}
        medicine_name = new ArrayList<>();
        amount = new ArrayList<>();
        time = new ArrayList<>();
        date = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerAdapter = new RecyclerAdapter(this, medicine_name, amount, time, date);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displayData();
    }


    private void displayData() {
        Cursor cursor1 = dbHelper.getMedicineData();
        Cursor cursor2 = dbHelper.getAlertData();
        if (cursor1.getCount() == 0) {
            // Toast.makeText(this,"No data entiry",Toast.LENGTH_SHORT).show();
            return;
        } else {
            while (cursor1.moveToNext()) {
                medicine_name.add(cursor1.getString(1));
                amount.add(cursor1.getString(2));
            }
        }

        if (cursor2.getCount() == 0) {
            // Toast.makeText(this,"No data entiry",Toast.LENGTH_SHORT).show();
            return;
        } else {
            while (cursor2.moveToNext()) {
                time.add(cursor2.getString(1));
                date.add(cursor2.getString(2));
            }
        }
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
                Intent intent_calendar = new Intent(getApplicationContext(), CalendarNavigation.class);
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