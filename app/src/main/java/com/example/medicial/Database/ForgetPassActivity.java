package com.example.medicial.Database;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.medicial.Activity.LoginActivity;
import com.example.medicial.R;

public class ForgetPassActivity extends AppCompatActivity {
    Toolbar toolbar;
    ActionBar actionBar;
    EditText edt_Username, edt_Pass, edt_RePass;
    Button btn_Reset;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);

        init();
        btn_Reset.setOnClickListener(v -> resetPassword());
    }

    private void init() {
        // {Toolbar}
        toolbar = findViewById(R.id.fp_toolbar);
        setSupportActionBar(toolbar);

        // {Setting up action bar}
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back));
        }

        edt_Username = findViewById(R.id.edt_fp_username);
        edt_Pass = findViewById(R.id.edt_fp_pass);
        edt_RePass = findViewById(R.id.edt_fp_repass);
        btn_Reset = findViewById(R.id.btn_reset);
        dbHelper = new DBHelper(this);
    }

    private void resetPassword() {
        String _Username = edt_Username.getText().toString();
        String _Pass = edt_Pass.getText().toString();
        String _RePass = edt_RePass.getText().toString();

        boolean check = validateInfo(_Username, _Pass, _RePass);
        if (check) {
            boolean reset = dbHelper.resetPassword(_Username, _Pass);
            if (!reset) {
                edt_Username.requestFocus();
                edt_Username.setError("Wrong Username");
            } else {
                dbHelper.resetPassword(_Username, _Pass);
                Toast.makeText(ForgetPassActivity.this, "Password Reset Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ForgetPassActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private boolean validateInfo(String _Username, String _Pass, String _RePass) {
        if (_Username.length() == 0) {
            edt_Username.requestFocus();
            edt_Username.setError(getString(R.string.empty));
            return false;

        } else if (_Pass.length() == 0) {
            edt_Pass.requestFocus();
            edt_Pass.setError(getString(R.string.empty));
            return false;

        } else if (_Pass.length() < 6) {
            edt_Pass.requestFocus();
            edt_Pass.setError(getResources().getString(R.string.password_length));
            return false;

        } else if (_RePass.length() == 0) {
            edt_RePass.requestFocus();
            edt_RePass.setError(getString(R.string.empty));
            return false;

        } else if (!_RePass.equals(_Pass)) {
            edt_RePass.requestFocus();
            edt_RePass.setError(getResources().getString(R.string.password_notMatching));
            return false;

        } else {
            return true;
        }
    }
}