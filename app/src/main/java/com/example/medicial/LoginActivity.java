package com.example.medicial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    DBHelper dbHelper = new DBHelper(this);
    Button login, CreateAccount;
    EditText username, password;
    TextView txtv_incorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//       {Full screen activity}
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Call register activity after on click create account button
        CreateAccount = findViewById(R.id.btn_createAccount);
        CreateAccount.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        });

        username = findViewById(R.id.edt_login_username);
        password = findViewById(R.id.edt_login_password);
        login = findViewById(R.id.btn_login);
        txtv_incorrect = findViewById(R.id.invalid);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });
    }

/* Login function
* check if all edittext empty or not
* check if username and password is valid or not
* if it does not exist then it is incorrect information
*/
    public void Login(){
        String Username = username.getText().toString();
        String Password = password.getText().toString();

        if (TextUtils.isEmpty(Username)){
            username.setHint("required field");
            username.setHintTextColor(getResources().getColor(R.color.red));
        }

        if (TextUtils.isEmpty(Password)){
            password.setHint("required field");
            password.setHintTextColor(getResources().getColor(R.color.red));
        }

        else{
            boolean check_name_pass = dbHelper.CheckUserPassword(Username, Password);
            if (check_name_pass){
                String getUsername = username.getText().toString();
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("key",getUsername);
                startActivity(intent);
                finish();
            }else{
                txtv_incorrect.setText("Incorrect information");
                txtv_incorrect.setTextColor(getResources().getColor(R.color.red));
            }
        }
    }

}