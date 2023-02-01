package com.example.medicial.admin;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.medicial.R;
import com.example.medicial.database.DBHelper;
import com.example.medicial.model.User;

import java.util.ArrayList;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class ShowUsersActivity extends AppCompatActivity {
    Toolbar toolbar;
    ActionBar actionBar;
    DBHelper dbHelper;
    ArrayList<User> arraylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_users);

        init();
        showUsers();
    }

    private void init() {
        // {Toolbar}
        toolbar = findViewById(R.id.shu_toolbar);
        setSupportActionBar(toolbar);

        // {Setting up action bar}
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back));
        }
    }

    private void showUsers() {
        TableView tableView = findViewById(R.id.table_data_view);
        String[] headers = {"ID", "User Name", "First Name", "Last Name", "Email", "Password"};
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, headers));

        // To customize column width
        tableView.setColumnModel(new TableColumnModel() {
            @Override
            public int getColumnCount() {
                return headers.length;
            }

            @Override
            public void setColumnCount(int columnCount) {
            }

            @Override
            public int getColumnWidth(int columnIndex, int tableWidthInPx) {
                int _Width_1 = (int) (tableWidthInPx * 0.05);
                int _Width_2 = (int) (tableWidthInPx * 0.15);
                int _Width_3 = (int) (tableWidthInPx * 0.3);

                switch (columnIndex) {
                    case 0:
                        return _Width_1;
                    case 1:
                    case 2:
                    case 3:
                        return _Width_2;
                    case 4:
                        return _Width_3;
                    case 5:
                        return _Width_2;
                    default:
                        return _Width_1;
                }
            }
        });

        dbHelper = new DBHelper(this);
        arraylist = dbHelper.getAllUserData();
        String[][] userData = new String[arraylist.size()][6];

        for (int i = 0; i < arraylist.size(); i++) {
            User user = arraylist.get(i);
            userData[i][0] = String.valueOf(user.getId());
            userData[i][1] = user.getUsername();
            userData[i][2] = user.getFirstname();
            userData[i][3] = user.getLastname();
            userData[i][4] = user.getEmail();
            userData[i][5] = user.getPassword();
        }
        tableView.setDataAdapter(new SimpleTableDataAdapter(this, userData));
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
}