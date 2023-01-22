package com.example.medicial.Activity;

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
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

public class MedicineActivity extends AppCompatActivity {
    Toolbar toolbar;
    ActionBar actionBar;
    EditText med_name, med_amount;
    ImageView imgViewUpload, imageViewScanner;
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
    Bitmap bitmap;
    private boolean textExtracted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);
        textExtracted = false;
        init();
        imageViewScanner.setOnClickListener(view -> ImagePickDialog());
    }

    private void init() {

        // {Full screen activity}
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

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

        // {Upload image }
        imgViewUpload = findViewById(R.id.imgv_medicine_pic);
        imgViewUpload.setOnClickListener(view -> ImagePickDialog());

        // {Text scanning}
        imageViewScanner = findViewById(R.id.imageView_scanner);
    }

    private void ValidateAndPassData() {
        String _MedName = med_name.getText().toString();
        String _MedAmount = med_amount.getText().toString();
        String _MedImage = image_uri != null ? image_uri.toString() : "";

        if (_MedName.length() == 0) {
            med_name.requestFocus();
            med_name.setError("Field cannot be empty");

        } else if (_MedAmount.length() == 0) {
            med_amount.requestFocus();
            med_amount.setError("Field cannot be empty");

        } else {
            Intent intent = new Intent(this, ScheduleActivity.class);
            intent.putExtra("key_medName", _MedName);
            intent.putExtra("key_medAmount", _MedAmount);
            intent.putExtra("key_medImage", _MedImage);

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
        builder.setItems(options, (dialogInterface, i) -> {
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
        });

        // create/show dialog
        builder.create().show();
    }

    private void PickFromCamera() {
        // Intent to pick image from camera, the image will be return onActivityResult method
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image description");

        // Put image uri
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        // Intent to open camera for image
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
        if (resultCode != RESULT_OK) {
            Toast.makeText(MedicineActivity.this, "Error: Could not pick an image.", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (requestCode)
        {
            case IMAGE_PICK_GALLERY_CODE:
                // picked from gallery
                if (data == null) {
                    Toast.makeText(MedicineActivity.this, "Error: Data is null.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // image is picked
                CropImage.activity(data.getData()).setGuidelines(CropImageView.Guidelines.ON).start(this);
                break;
            case IMAGE_PICK_CAMERA_CODE:
                // picked from camera
                CropImage.activity(image_uri).setGuidelines(CropImageView.Guidelines.ON).start(this);
                break;
        }

        // get cropped image
        SetCroppedImage(requestCode, resultCode, data);
        ExtractTextFromImage(image_uri);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void SetCroppedImage(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode != CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) return;
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if (resultCode != RESULT_OK) return;

        Uri resultUri = null;
        if (result != null) resultUri = result.getUri();
        image_uri = resultUri;
        imgViewUpload.setImageURI(image_uri);
    }

    private void ExtractTextFromImage(Uri resultUri)
    {
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
            TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();

            if (recognizer.isOperational()) {
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

            //Toast.makeText(MedicineActivity.this, "Text Recognizer not operational.", Toast.LENGTH_SHORT).show();
            textExtracted = true;
        } catch (IOException e) {
            e.printStackTrace();
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
        if (item.getItemId() == R.id.action_next) {
            ValidateAndPassData();
        }
        return super.onOptionsItemSelected(item);
    }
}