package com.example.medicial.admin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.os.Bundle;

import com.example.medicial.R;
import com.example.medicial.database.DBHelper;
import com.example.medicial.model.Data;

import java.util.ArrayList;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class ShowDataActivity extends AppCompatActivity {
    Toolbar toolbar;
    ActionBar actionBar;
    DBHelper dbHelper;
    ArrayList<Data> arraylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);

        init();
        showData();
    }

    private void init() {
        // {Toolbar}
        toolbar = findViewById(R.id.shd_toolbar);
        setSupportActionBar(toolbar);

        // {Setting up action bar}
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back));
        }
    }

    private void showData() {
        TableView tableView = findViewById(R.id.table_data_view);
        String[] headers = {"ID", "Name", "Amount", "Time", "Date", "Description"};
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, headers));

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
                int _Width_3 = (int) (tableWidthInPx * 0.17);
                int _Width_4 = (int) (tableWidthInPx * 0.3);

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
                        return _Width_4;
                    default:
                        return _Width_1;
                }
            }
        });

        dbHelper = new DBHelper(this);
        arraylist = dbHelper.getReminderData();
        String[][] reminderData = new String[arraylist.size()][6];

        for (int i = 0; i < arraylist.size(); i++) {
            Data data = arraylist.get(i);

            reminderData[i][0] = String.valueOf(data.get_Med_Id());
            reminderData[i][1] = data.get_Med_Name();
            reminderData[i][2] = String.valueOf(data.get_Med_Amount());
            reminderData[i][3] = data.get_Time();
            reminderData[i][4] = data.get_Date();
            reminderData[i][5] = data.get_Med_Desc();
        }
        tableView.setDataAdapter(new SimpleTableDataAdapter(this, reminderData));
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