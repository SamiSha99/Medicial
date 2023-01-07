package com.example.medicial.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.medicial.R;

public class ReminderActivity extends AppCompatActivity {
    Toolbar toolbar;
    ActionBar actionBar;
    EditText med_name, med_amount;
    ImageView imgViewUpload;
    // Permissions constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;
    // Image pick constants
    private static final int IMAGE_PICK_CAMERA_CODE = 102;
    private static final int IMAGE_PICK_GALLERY_CODE = 103;
    // Arrays camera permissions
    private String[] camera_permissions; // camera and storage
    private String[] storage_permissions; // only storage
    // Variables (will contain data to save)
    private Uri image_uri;

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

        // init permissions arrays
        camera_permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storage_permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

//        {Upload image }
        imgViewUpload = findViewById(R.id.imgv_medicine_pic);

        imgViewUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePickDialog();
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
            String get_image = image_uri.toString();

            Intent intent = new Intent(this, ScheduleActivity.class);
            intent.putExtra("key_medName", get_med_name);
            intent.putExtra("key_medAmount", get_med_amount);
            intent.putExtra("key_image", get_image);

            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    private void ImagePickDialog() {
        // Option to display in dialog
        String[] options = {"Camera", "Gallery"};
        // Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Title
        builder.setTitle("Pick image from");
        // set items/options
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // handle click
                if (i == 0) {
                    // Camera clicked
                    if (!CheckCameraPermission()) {
                        RequestCameraPermission();
                    } else {
                        // permission already granted
                        PickFromCamera();
                    }
                } else if (i == 1) {
                    if (!CheckStoragePermission()) {
                        RequestStoragePermission();
                    } else {
                        // permission already granted
                        PickFromGallery();
                    }
                }
            }
        });

        // create/show dialog
        builder.create().show();
    }

    private void PickFromCamera() {
        // Intent to pick image from camera, the image will be return onActivityResult method
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private void PickFromGallery() {
        // Intent to pick image from gallery, the image will be return onActivityResult method
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*"); // we want only images
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    private boolean CheckStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    private void RequestStoragePermission() {
        ActivityCompat.requestPermissions(this, storage_permissions, STORAGE_REQUEST_CODE);
    }

    private boolean CheckCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void RequestCameraPermission() {
        ActivityCompat.requestPermissions(this, camera_permissions, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Result of permission allowed/denied
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean camera_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storage_accepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (camera_accepted && storage_accepted) {
                        // Both permission allowed
                        PickFromCamera();
                    } else {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;

            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    // If allowed return true otherwise return false
                    boolean storage_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (storage_accepted) {
                        // Storage permission allowed
                        PickFromGallery();
                    } else {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Image picked from camera or gallery will be receive here
        if (resultCode == RESULT_OK) {
            // image is picked
            if (requestCode == IMAGE_PICK_GALLERY_CODE && resultCode == RESULT_OK && data!= null) {
                // picked from gallery
                image_uri = data.getData();
                imgViewUpload.setImageURI(image_uri);

            } else if (requestCode == IMAGE_PICK_CAMERA_CODE && resultCode == RESULT_OK && data!= null) {
                // picked from camera
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgViewUpload.setImageBitmap(bitmap);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
