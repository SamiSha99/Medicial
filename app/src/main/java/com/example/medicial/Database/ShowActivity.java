package com.example.medicial.Database;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.medicial.R;

public class ShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

//        Uri imageUri = Uri.parse(getIntent().getStringExtra("image"));
//        ImageView imageView = findViewById(R.id.show);
//        imageView.setImageURI(imageUri);
    }
}