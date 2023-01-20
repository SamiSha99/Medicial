package com.example.medicial.Database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.medicial.Activity.HomeActivity;
import com.example.medicial.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class UpdateActivity extends AppCompatActivity {
    Toolbar toolbar;
    ActionBar actionBar;
    EditText med_name, med_amount;
    DBHelper dbHelper;
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
    ImageView imgViewUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        init();
        imgViewUpload.setOnClickListener(view -> ImagePickDialog());
    }

    private void init() {
        // {Full screen activity}
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // {Toolbar}
        toolbar = findViewById(R.id.up_toolbar);
        if (toolbar != null) setSupportActionBar(toolbar);

        // {Setting up action bar}
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back));
            actionBar.setDisplayShowTitleEnabled(true);
        }

        // {Hook id}
        med_name = findViewById(R.id.edt_medName);
        med_amount = findViewById(R.id.edt_amount);

        // {DBHelper}
        dbHelper = new DBHelper(this);

        // init permissions arrays
        camera_permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storage_permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        // {Upload image }
        imgViewUpload = findViewById(R.id.imgv_medicine_pic);
    }

    private void update() {
        boolean emptyInputs = false;
        Bundle bundle = getIntent().getExtras();
        String receive_med_id = bundle.getString("med_id");
        String receive_med_name = bundle.getString("med_name");
        String receive_med_amount = bundle.getString("med_amount");
        // To get the prev data and put in edittext
//        med_name.setText(receive_med_name);
//        med_amount.setText(receive_med_amount);

        int _Med_Id = Integer.parseInt(receive_med_id);
        String _Med_Name = med_name.getText().toString();
        String _Med_Amount = med_amount.getText().toString();

        int redColor = ContextCompat.getColor(this, R.color.red);

        if (_Med_Name.isEmpty()) {
            med_name.setHint("required");
            med_name.setHintTextColor(redColor);
            emptyInputs = true;
        }

        if (_Med_Amount.isEmpty()) {
            med_amount.setHint("required");
            med_amount.setHintTextColor(redColor);
            emptyInputs = true;
        }

        if(emptyInputs) return;

        String _Med_Image = image_uri != null ? image_uri.toString() : "";
        int _Med_Amount_Int = Integer.parseInt(med_amount.getText().toString());
        dbHelper.updateMedicineData(_Med_Id, _Med_Name, _Med_Amount_Int, _Med_Image);
        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(UpdateActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void ImagePickDialog() {
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick image from");
        builder.setItems(options, (dialogInterface, i) -> {
            if (i == 0) {
                if (!CheckCameraPermission()) {
                    RequestCameraPermission();
                } else {
                    PickFromCamera();
                }
            } else if (i == 1) {
                if (!CheckStoragePermission()) {
                    RequestStoragePermission();
                } else {
                    PickFromGallery();
                }
            }
        });
        builder.create().show();
    }

    private void PickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image description");

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private void PickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*"); // we want only images
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    private boolean CheckStoragePermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
    }

    private void RequestStoragePermission() {
        ActivityCompat.requestPermissions(this, storage_permissions, STORAGE_REQUEST_CODE);
    }

    private boolean CheckCameraPermission() {
        boolean result_camera;
        result_camera = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);

        boolean result_storage;
        result_storage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);

        return result_camera && result_storage;
    }

    private void RequestCameraPermission() {
        ActivityCompat.requestPermissions(this, camera_permissions, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

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
                    boolean storage_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (storage_accepted) {
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
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                if (data != null) {
                    CropImage.activity(data.getData())
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1, 1)
                            .start(this);
                }

            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                CropImage.activity(image_uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult activityResult = CropImage.getActivityResult(data);

                if (resultCode == RESULT_OK) {
                    if (activityResult != null) {
                        image_uri = activityResult.getUri();
                        imgViewUpload.setImageURI(image_uri);
                    }
                }

                if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = activityResult.getError();
                    Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            update();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}