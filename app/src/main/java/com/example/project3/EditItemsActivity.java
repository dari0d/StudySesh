package com.example.project3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class EditItemsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_items);

        Button btnEditClass = findViewById(R.id.btnEditClass);
        Button btnEditSesh = findViewById(R.id.btnEditSesh);
        Button btnBackHome = findViewById(R.id.btnBackHome);

        btnEditClass.setOnClickListener(v ->
                startActivity(new Intent(EditItemsActivity.this, EditClassActivity.class)));

        btnEditSesh.setOnClickListener(v ->
                startActivity(new Intent(EditItemsActivity.this, EditSeshActivity.class)));

        btnBackHome.setOnClickListener(v -> {
            Intent i = new Intent(EditItemsActivity.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        });
    }
}