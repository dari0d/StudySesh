package com.example.project3;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Random;

public class NewClassActivity extends AppCompatActivity {

    private StudyDao dao;
    private final Random random = new Random();

    private EditText edtExistingClassCode;
    private EditText edtClassName;
    private EditText edtStartTime;
    private EditText edtEndTime;

    private CheckBox cbMon, cbTue, cbWed, cbThu, cbFri, cbSat, cbSun;

    private TextView txtGeneratedClassCode;
    private Button btnCreate;
    private Button btnBackHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_class);

        dao = StudySeshDatabase.getInstance(this).studyDao();

        edtExistingClassCode = findViewById(R.id.edtExistingClassCode);
        edtClassName = findViewById(R.id.edtClassName);
        edtStartTime = findViewById(R.id.edtClassStartTime);
        edtEndTime = findViewById(R.id.edtClassEndTime);

        cbMon = findViewById(R.id.cbClassMon);
        cbTue = findViewById(R.id.cbClassTue);
        cbWed = findViewById(R.id.cbClassWed);
        cbThu = findViewById(R.id.cbClassThu);
        cbFri = findViewById(R.id.cbClassFri);
        cbSat = findViewById(R.id.cbClassSat);
        cbSun = findViewById(R.id.cbClassSun);

        txtGeneratedClassCode = findViewById(R.id.txtGeneratedClassCode);
        btnCreate = findViewById(R.id.btnCreateClass);
        btnBackHome = findViewById(R.id.btnBackHome);

        btnCreate.setOnClickListener(v -> handleCreateOrJoin());
        btnBackHome.setOnClickListener(v -> {
            finish();
            overridePendingTransition(0, 0);
        });
    }

    private void handleCreateOrJoin() {
        String existingCode = edtExistingClassCode.getText().toString().trim();
        if (!existingCode.isEmpty()) {
            StudyClass existing = dao.findClassByCode(existingCode);
            if (existing != null) {
                edtClassName.setText(existing.name);
                edtStartTime.setText(existing.startTime);
                edtEndTime.setText(existing.endTime);
                setWeekdayChecks(existing.weekdays);
                txtGeneratedClassCode.setText(existing.code);
                Toast.makeText(this, "Joined existing class.", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(this, "No class found with that code.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        String name = edtClassName.getText().toString().trim();
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

        StudyClass c = new StudyClass();
        c.name = name;
        c.startTime = start;
        c.endTime = end;
        c.weekdays = weekdays;
        c.code = generateCode();

        dao.insertClass(c);
        txtGeneratedClassCode.setText(c.code);
        Toast.makeText(this, "Class created.", Toast.LENGTH_SHORT).show();
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

    private void setWeekdayChecks(String weekdays) {
        if (weekdays == null) return;
        String lower = weekdays.toLowerCase(Locale.ROOT);
        cbMon.setChecked(lower.contains("mon"));
        cbTue.setChecked(lower.contains("tue"));
        cbWed.setChecked(lower.contains("wed"));
        cbThu.setChecked(lower.contains("thu"));
        cbFri.setChecked(lower.contains("fri"));
        cbSat.setChecked(lower.contains("sat"));
        cbSun.setChecked(lower.contains("sun"));
    }

    private String generateCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}