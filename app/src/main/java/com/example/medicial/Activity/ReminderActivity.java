package com.example.medicial.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medicial.R;

public class ReminderActivity extends AppCompatActivity {
    Toolbar toolbar;
    ActionBar actionBar;
    EditText med_name, med_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

//        {Full screen activity}
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        {Toolbar}
        toolbar = findViewById(R.id.rem_toolbar);
        setSupportActionBar(toolbar);

//        {Setting up action bar}
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_baseline_arrow_back));

//        {Hook Edittext filed}
        med_name = findViewById(R.id.edt_medName);
        med_amount = findViewById(R.id.edt_amount);
    }

    private void ValidateAndSendData() {
        if (med_name.getText().toString().isEmpty() || med_amount.getText().toString().isEmpty()) {
            med_name.setHint("required");
            med_name.setHintTextColor(getResources().getColor(R.color.red));

            med_amount.setHint("required");
            med_amount.setHintTextColor(getResources().getColor(R.color.red));

        } else {
            String get_med_name = med_name.getText().toString();
            String get_med_amount = med_amount.getText().toString();

            Intent intent = new Intent(this, ScheduleActivity.class);
            intent.putExtra("key_medName", get_med_name);
            intent.putExtra("key_medAmount", get_med_amount);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.medicine_det_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_next:
                ValidateAndSendData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
