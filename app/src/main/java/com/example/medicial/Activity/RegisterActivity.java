package com.example.medicial.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.medicial.Database.DBHelper;
import com.example.medicial.R;
import com.example.medicial.Fragment.SuccessFragment;

public class RegisterActivity extends AppCompatActivity {
    DBHelper dbHelper = new DBHelper(this);
    EditText username, firstName, lastName, email, password, re_password;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // {Full screen activity}
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // {Hook Edittext fields}
        username = findViewById(R.id.edt_reg_userName);
        firstName = findViewById(R.id.edt_reg_firstName);
        lastName = findViewById(R.id.edt_reg_lastName);
        email = findViewById(R.id.edt_reg_email);
        password = findViewById(R.id.edt_reg_password);
        re_password = findViewById(R.id.edt_reg_repassword);

        register = findViewById(R.id.btn_signUp);
        register.setOnClickListener(view -> Register());
    }

    public void Register() {
        String _Username = username.getText().toString();
        String _Firstname = firstName.getText().toString();
        String _Lastname = lastName.getText().toString();
        String _Email = email.getText().toString();
        String _Password = password.getText().toString();
        String _Re_password = re_password.getText().toString();
        boolean emptyField = false;
        if(TextUtils.isEmpty(_Username))
        {
            username.setHint("required field");
            username.setHintTextColor(getResources().getColor(R.color.red));
            emptyField = true;
        }

        if(TextUtils.isEmpty(_Firstname))
        {
            firstName.setHint("required field");
            firstName.setHintTextColor(getResources().getColor(R.color.red));
            emptyField = true;
        }

        if(TextUtils.isEmpty(_Lastname))
        {
            lastName.setHint("required field");
            lastName.setHintTextColor(getResources().getColor(R.color.red));
            emptyField = true;
        }

        if(TextUtils.isEmpty(_Email))
        {
            email.setHint("required field");
            email.setHintTextColor(getResources().getColor(R.color.red));
            emptyField = true;
        }

        if(TextUtils.isEmpty(_Password))
        {
            password.setHint("required field");
            password.setHintTextColor(getResources().getColor(R.color.red));
            emptyField = true;
        }

        if(TextUtils.isEmpty(_Re_password))
        {
            re_password.setHint("required field");
            re_password.setHintTextColor(getResources().getColor(R.color.red));
            emptyField = true;
        }

        // Empty Fields
        if(emptyField)
        {
            Toast.makeText(this, "Some fields are empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Password verification mismatch
        if(!_Password.equals(_Re_password))
        {
            Toast.makeText(this, "Password not matching", Toast.LENGTH_SHORT).show();
            return;
        }

        // Username already exists
        if(dbHelper.CheckUserName(_Username))
        {
            Toast.makeText(this, "User already exists!", Toast.LENGTH_SHORT).show();
            return;
        }

        // DB insertion failure
        if(!dbHelper.insertUserData(_Username, _Firstname, _Lastname, _Password, _Email))
        {
            Toast.makeText(this, "REGISTRATION FAILED", Toast.LENGTH_SHORT).show();
            return;
        }

        replaceFragment(new SuccessFragment());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}