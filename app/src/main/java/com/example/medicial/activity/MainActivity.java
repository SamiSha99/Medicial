package com.example.medicial.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.example.medicial.BuildConfig;
import com.example.medicial.R;

public class MainActivity extends AppCompatActivity {
    Handler handler = new Handler();
    TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        // {Splash screen activity}
        Runnable runnable = this::Call_Login_Activity;
        handler.postDelayed(runnable, 2500);

        // {To get version}
        version = findViewById(R.id.version);
        version.setText(BuildConfig.VERSION_NAME);
    }

    public void Call_Login_Activity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}