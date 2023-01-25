package com.example.medicial.Database;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.medicial.Activity.ImageActivity;
import com.example.medicial.R;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

public class UpdateActivity extends AppCompatActivity {
    Toolbar toolbar;
    ActionBar actionBar;
    EditText med_name, med_amount;
    ImageView imageViewScanner;
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
    Bitmap bitmap;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        init();
        setImageViewScanner();
    }

    private void init() {
        // {Toolbar}
        toolbar = findViewById(R.id.med_toolbar);
        setSupportActionBar(toolbar);

        // {Setting up action bar}
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back));
        }

        // {Hook Edittext filed}
        med_name = findViewById(R.id.edt_medName);
        med_amount = findViewById(R.id.edt_amount);

        // init permissions arrays
        camera_permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storage_permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    private void setImageViewScanner() {
        imageViewScanner = findViewById(R.id.imageView_scanner);
        imageViewScanner.setOnClickListener(view -> imagePickDialog());
    }

    private void validateAndPassData() {
        String _MedName = med_name.getText().toString();
        String _MedAmount = med_amount.getText().toString();

        if (_MedName.length() == 0) {
            med_name.requestFocus();
            med_name.setError(getResources().getString(R.string.empty));

        } else if (_MedAmount.length() == 0) {
            med_amount.requestFocus();
            med_amount.setError(getResources().getString(R.string.empty));

        } else {
            Intent intent = new Intent(this, ImageActivity.class);
            intent.putExtra("key_medName", _MedName);
            intent.putExtra("key_medAmount", _MedAmount);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    private void imagePickDialog() {
        // Option to display in dialog
        String[] options = {"Camera", "Gallery"};
        // Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Title
        builder.setTitle("Pick image from");
        // set items/options
        builder.setItems(options, (dialogInterface, i) -> {
            // handle click
            if (i == 0) {
                // Camera clicked
                if (!checkCameraPermission()) {
                    requestCameraPermission();
                } else {
                    // permission already granted
                    pickFromCamera();
                }
            } else if (i == 1) {
                if (!checkStoragePermission()) {
                    requestStoragePermission();
                } else {
                    // permission already granted
                    pickFromGallery();
                }
            }
        }).show();
    }

    private void pickFromCamera() {
        // Intent to pick image from camera, the image will be return onActivityResult method
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image description");
        // Put image uri
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        // Intent to open camera for image
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private void pickFromGallery() {
        // Intent to pick image from gallery, the image will be return onActivityResult method
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*"); // we want only images
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storage_permissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result_camera;
        result_camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);

        boolean result_storage;
        result_storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);

        return result_camera && result_storage;
    }

    private void requestCameraPermission() {
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
                        pickFromCamera();
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
                        pickFromGallery();
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
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                // picked from gallery
                if (data != null) {
                    CropImage.activity(data.getData())
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(this);
                }

            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                // picked from camera
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
        }
        // get cropped image
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = null;
                if (result != null) resultUri = result.getUri();

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();

                    if (!recognizer.isOperational()) {
                        Toast.makeText(UpdateActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    } else {
                        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                        SparseArray<TextBlock> items = recognizer.detect(frame);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < items.size(); i++) {
                            TextBlock myItem = items.valueAt(i);
                            sb.append(myItem.getValue());
                            sb.append("\n");
                        }
                        med_name.setText(sb.toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
        if (item.getItemId() == R.id.action_next) {
            validateAndPassData();
        }
        return super.onOptionsItemSelected(item);
    }
}