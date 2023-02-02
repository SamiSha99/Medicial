package com.example.medicial.admin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.medicial.R;
import com.example.medicial.database.DBHelper;

public class DeleteUserActivity extends AppCompatActivity {
    Toolbar toolbar;
    ActionBar actionBar;
    EditText delete_user;
    Button _Delete;
    DBHelper dbHelper = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);

        init();
        _Delete.setOnClickListener(v -> DeleteUser());
    }

    private void init() {
        // {Toolbar}
        toolbar = findViewById(R.id.de_toolbar);
        setSupportActionBar(toolbar);

        // {Setting up action bar}
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back));
        }

        delete_user = findViewById(R.id.edt_del_byId);
        _Delete = findViewById(R.id.btn_delete_user);
    }

    private void DeleteUser() {
        String _Str_Id = delete_user.getText().toString();
        int _Id = Integer.parseInt(_Str_Id);
        boolean check = validateInfo(_Str_Id);

        if (check) {
            boolean delete = dbHelper.deleteUser(_Id);
            if (!delete) {
                delete_user.requestFocus();
                delete_user.setError("Wrong Id");

            } else {
                dbHelper.deleteUser(_Id);
                delete_user.setText("");
                Toast.makeText(DeleteUserActivity.this, "User Deleted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateInfo(String _Str_Id) {
        if (_Str_Id.length() == 0) {
            delete_user.requestFocus();
            delete_user.setError(getResources().getString(R.string.empty));
            return false;
        } else {
            return true;
        }
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