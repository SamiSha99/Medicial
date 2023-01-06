package com.example.medicial.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medicial.R;
import com.github.dhaval2404.imagepicker.ImagePicker;

public class ReminderActivity extends AppCompatActivity {
    Toolbar toolbar;
    ActionBar actionBar;
    EditText med_name, med_amount;
    ImageView imgViewUpload;
    private static final int IMAG_PICK_CODE = 1000;
    Uri image_uri;
    Bitmap img_map;
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

//        {Upload image }
        imgViewUpload = findViewById(R.id.imgv_medicine_pic);

        imgViewUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(ReminderActivity.this)
                        .crop()	    			//Crop image
                        .compress(1024)			//Final image size will be less than 1 MB
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080
                        .start();
            }
        });
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
//            String get_image = image_uri.toString();

            Intent intent = new Intent(this, ScheduleActivity.class);
            intent.putExtra("key_medName", get_med_name);
            intent.putExtra("key_medAmount", get_med_amount);
//            intent.putExtra("key_image", get_image);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            // Set image to ImageView
        if (resultCode == Activity.RESULT_OK){
            image_uri = data.getData();
            imgViewUpload.setImageURI(image_uri);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
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
