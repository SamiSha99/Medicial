package com.example.medicial.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.medicial.R;

public class AdminActivity extends AppCompatActivity {
    Button _Create, _Delete, _Update, _Show_Users, _Show_Data;

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

        _Create = findViewById(R.id.btn_create_user);
        _Delete = findViewById(R.id.btn_delete_user);
        _Update = findViewById(R.id.btn_update_user);
        _Show_Users = findViewById(R.id.btn_show_users);
        _Show_Data = findViewById(R.id.btn_users_data);
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
}