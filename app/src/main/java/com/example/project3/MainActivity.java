package com.example.project3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnNew;
    private Button btnSchedule;
    private Button btnEdit;
    private Button btnStats;
    private Button btnLogStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNew = findViewById(R.id.btnNew);
        btnSchedule = findViewById(R.id.btnSchedule);
        btnEdit = findViewById(R.id.btnEdit);
        btnStats = findViewById(R.id.btnStats);
        btnLogStats = findViewById(R.id.btnLogStats);

        btnNew.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, NewItemsActivity.class)));

        btnSchedule.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ScheduleActivity.class)));

        btnEdit.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, EditItemsActivity.class)));

        btnStats.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, StatsSummaryActivity.class)));

        btnLogStats.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, LogStatsActivity.class)));
    }
}