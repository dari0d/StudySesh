package com.example.project3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class NewItemsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_items);

        Button btnNewClass = findViewById(R.id.btnNewClass);
        Button btnNewSesh = findViewById(R.id.btnNewSesh);
        Button btnBackHome = findViewById(R.id.btnBackHome);

        btnNewClass.setOnClickListener(v ->
                startActivity(new Intent(NewItemsActivity.this, NewClassActivity.class)));

        btnNewSesh.setOnClickListener(v ->
                startActivity(new Intent(NewItemsActivity.this, NewSeshActivity.class)));

        btnBackHome.setOnClickListener(v -> {
            Intent i = new Intent(NewItemsActivity.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        });
    }
}