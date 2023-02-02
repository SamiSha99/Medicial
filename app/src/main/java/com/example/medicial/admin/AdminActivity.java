package com.example.medicial.admin;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.medicial.R;
import com.example.medicial.activity.LoginActivity;
import com.google.android.material.navigation.NavigationView;

public class AdminActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Button _Create, _Delete, _Update, _Show_Users, _Show_Data;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        init();
        create();
        delete();
        update();
        showUsers();
        showData();
    }

    private void init() {
        // {Hook Id}
        _Create = findViewById(R.id.btn_create_user);
        _Delete = findViewById(R.id.btn_delete_user);
        _Update = findViewById(R.id.btn_update_user);
        _Show_Users = findViewById(R.id.btn_show_users);
        _Show_Data = findViewById(R.id.btn_users_data);

        drawerLayout = findViewById(R.id.admin_drawerLayout);
        navigationView = findViewById(R.id.admin_nav_view);

        // {ToolBar}
        toolbar = findViewById(R.id.ad_toolbar);
        setSupportActionBar(toolbar);

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
    }

    private void create() {
        _Create.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, CreateUserActivity.class);
            startActivity(intent);
        });
    }

    private void delete() {
        _Delete.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, DeleteUserActivity.class);
            startActivity(intent);
        });
    }

    private void update() {
        _Update.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, UpdateUserActivity.class);
            startActivity(intent);
        });
    }

    private void showUsers() {
        _Show_Users.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, ShowUsersActivity.class);
            startActivity(intent);
        });
    }

    private void showData() {
        _Show_Data.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, ShowDataActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(AdminActivity.this)
                    .setMessage("Are you sure to Logout?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, i) -> logout())
                    .setNegativeButton("No", null)
                    .show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_logout) {
            logout();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        Intent intent_logout = new Intent(AdminActivity.this, LoginActivity.class);
        startActivity(intent_logout);
        finish();
    }
}