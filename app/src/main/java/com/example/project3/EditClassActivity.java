package com.example.project3;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class EditClassActivity extends AppCompatActivity {

    private StudyDao dao;

    private EditText edtName;
    private EditText edtStartTime;
    private EditText edtEndTime;

    private CheckBox cbMon, cbTue, cbWed, cbThu, cbFri, cbSat, cbSun;
    private Button btnSave, btnDelete, btnBackHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_class);

        dao = StudySeshDatabase.getInstance(this).studyDao();

        edtName = findViewById(R.id.edtClassNameEdit);
        edtStartTime = findViewById(R.id.edtClassStartTimeEdit);
        edtEndTime = findViewById(R.id.edtClassEndTimeEdit);

        cbMon = findViewById(R.id.cbClassMonEdit);
        cbTue = findViewById(R.id.cbClassTueEdit);
        cbWed = findViewById(R.id.cbClassWedEdit);
        cbThu = findViewById(R.id.cbClassThuEdit);
        cbFri = findViewById(R.id.cbClassFriEdit);
        cbSat = findViewById(R.id.cbClassSatEdit);
        cbSun = findViewById(R.id.cbClassSunEdit);

        btnSave = findViewById(R.id.btnSaveClass);
        btnDelete = findViewById(R.id.btnDeleteClass);
        btnBackHome = findViewById(R.id.btnBackHome);

        btnSave.setOnClickListener(v -> saveClass());
        btnDelete.setOnClickListener(v -> deleteClass());
        btnBackHome.setOnClickListener(v -> {
            finish();
            overridePendingTransition(0, 0);
        });
    }

    private void saveClass() {
        String name = edtName.getText().toString().trim();
        String start = edtStartTime.getText().toString().trim();
        String end = edtEndTime.getText().toString().trim();
        String weekdays = buildWeekdayString();

        if (TextUtils.isEmpty(name) ||
                TextUtils.isEmpty(start) ||
                TextUtils.isEmpty(end) ||
                TextUtils.isEmpty(weekdays)) {

            Toast.makeText(this, "Enter name, start/end time, and at least one weekday.", Toast.LENGTH_SHORT).show();
            return;
        }

        StudyClass existing = dao.findClassByName(name);
        if (existing == null) {
            Toast.makeText(this, "No class found with that name.", Toast.LENGTH_SHORT).show();
            return;
        }

        existing.startTime = start;
        existing.endTime = end;
        existing.weekdays = weekdays;

        dao.updateClass(existing);
        Toast.makeText(this, "Class updated.", Toast.LENGTH_SHORT).show();
    }

    private void deleteClass() {
        String name = edtName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Enter class name to delete.", Toast.LENGTH_SHORT).show();
            return;
        }

        StudyClass existing = dao.findClassByName(name);
        if (existing == null) {
            Toast.makeText(this, "No class found with that name.", Toast.LENGTH_SHORT).show();
            return;
        }

        dao.deleteClass(existing);
        Toast.makeText(this, "Class deleted.", Toast.LENGTH_SHORT).show();
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