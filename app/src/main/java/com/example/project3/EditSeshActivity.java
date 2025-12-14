package com.example.project3;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditSeshActivity extends AppCompatActivity {

    private StudyDao dao;

    private EditText edtName;
    private EditText edtStartTime;
    private EditText edtEndTime;

    private CheckBox cbMon, cbTue, cbWed, cbThu, cbFri, cbSat, cbSun;
    private CheckBox cbRecurring;

    private Button btnSave, btnDelete, btnBackHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sesh);

        dao = StudySeshDatabase.getInstance(this).studyDao();

        edtName = findViewById(R.id.edtSeshNameEdit);
        edtStartTime = findViewById(R.id.edtSeshStartTimeEdit);
        edtEndTime = findViewById(R.id.edtSeshEndTimeEdit);

        cbMon = findViewById(R.id.cbSeshMonEdit);
        cbTue = findViewById(R.id.cbSeshTueEdit);
        cbWed = findViewById(R.id.cbSeshWedEdit);
        cbThu = findViewById(R.id.cbSeshThuEdit);
        cbFri = findViewById(R.id.cbSeshFriEdit);
        cbSat = findViewById(R.id.cbSeshSatEdit);
        cbSun = findViewById(R.id.cbSeshSunEdit);
        cbRecurring = findViewById(R.id.cbRecurringEdit);

        btnSave = findViewById(R.id.btnSaveSesh);
        btnDelete = findViewById(R.id.btnDeleteSesh);
        btnBackHome = findViewById(R.id.btnBackHome);

        btnSave.setOnClickListener(v -> saveSesh());
        btnDelete.setOnClickListener(v -> deleteSesh());
        btnBackHome.setOnClickListener(v -> {
            finish();
            overridePendingTransition(0, 0);
        });
    }

    private void saveSesh() {
        String name = edtName.getText().toString().trim();
        String start = edtStartTime.getText().toString().trim();
        String end = edtEndTime.getText().toString().trim();
        String weekdays = buildWeekdayString();
        boolean recurring = cbRecurring.isChecked();

        if (TextUtils.isEmpty(name) ||
                TextUtils.isEmpty(start) ||
                TextUtils.isEmpty(end) ||
                TextUtils.isEmpty(weekdays)) {

            Toast.makeText(this, "Enter name, start/end time, and at least one weekday.", Toast.LENGTH_SHORT).show();
            return;
        }

        StudySession existing = dao.findSessionByName(name);
        if (existing == null) {
            Toast.makeText(this, "No sesh found with that name.", Toast.LENGTH_SHORT).show();
            return;
        }

        existing.startTime = start;
        existing.endTime = end;
        existing.weekdays = weekdays;
        existing.recurring = recurring;

        dao.updateSession(existing);
        Toast.makeText(this, "Sesh updated.", Toast.LENGTH_SHORT).show();
    }

    private void deleteSesh() {
        String name = edtName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Enter sesh name to delete.", Toast.LENGTH_SHORT).show();
            return;
        }

        StudySession existing = dao.findSessionByName(name);
        if (existing == null) {
            Toast.makeText(this, "No sesh found with that name.", Toast.LENGTH_SHORT).show();
            return;
        }

        dao.deleteSession(existing);
        Toast.makeText(this, "Sesh deleted.", Toast.LENGTH_SHORT).show();
    }

    private String buildWeekdayString() {
        StringBuilder sb = new StringBuilder();
        if (cbMon.isChecked()) sb.append("Mon,");
        if (cbTue.isChecked()) sb.append("Tue,");
        if (cbWed.isChecked()) sb.append("Wed,");
        if (cbThu.isChecked()) sb.append("Thu,");
        if (cbFri.isChecked()) sb.append("Fri,");
        if (cbSat.isChecked()) sb.append("Sat,");
        if (cbSun.isChecked()) sb.append("Sun,");
        if (sb.length() == 0) return "";
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }
}