package com.example.medicial;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;
import com.example.medicial.BuildConfig;

public class MainActivity extends AppCompatActivity {

    Handler handler = new Handler();
    TextView version;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //This code to show activity in full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Call_Login_Activity();
            }
        };

        handler.postDelayed(runnable,2*1000);

        version = findViewById(R.id.version);
        version.setText(BuildConfig.VERSION_NAME);
    }

    public void Call_Login_Activity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }



}