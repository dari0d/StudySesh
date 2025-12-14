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

public class NewSeshActivity extends AppCompatActivity {

    private EditText edtExistingCode;
    private EditText edtSeshName;
    private EditText edtStartTime;
    private EditText edtEndTime;

    private CheckBox chkMon, chkTue, chkWed, chkThu, chkFri, chkSat, chkSun;
    private CheckBox chkRecurring;

    private Button btnCreateSesh;
    private Button btnBackHome;
    private TextView txtGeneratedCode;

    private StudyDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sesh);

        dao = StudySeshDatabase.getInstance(this).studyDao();

        bindViews();
        setupListeners();
    }

    private void bindViews() {
        edtExistingCode = findViewById(R.id.edtExistingSeshCode);
        edtSeshName     = findViewById(R.id.edtNewSeshName);
        edtStartTime    = findViewById(R.id.edtSeshStartTime);
        edtEndTime      = findViewById(R.id.edtSeshEndTime);

        chkMon = findViewById(R.id.chkMon);
        chkTue = findViewById(R.id.chkTue);
        chkWed = findViewById(R.id.chkWed);
        chkThu = findViewById(R.id.chkThu);
        chkFri = findViewById(R.id.chkFri);
        chkSat = findViewById(R.id.chkSat);
        chkSun = findViewById(R.id.chkSun);

        chkRecurring = findViewById(R.id.chkRecurring);

        btnCreateSesh  = findViewById(R.id.btnCreateSesh);
        btnBackHome    = findViewById(R.id.btnBackHome);
        txtGeneratedCode = findViewById(R.id.txtGeneratedSeshCode);
    }

    private void setupListeners() {
        btnCreateSesh.setOnClickListener(v -> handleCreateOrJoin());

        btnBackHome.setOnClickListener(v -> {
            finish();
            overridePendingTransition(0, 0);
        });
    }

    private void handleCreateOrJoin() {
        String existingCode = edtExistingCode.getText().toString().trim();

        if (!TextUtils.isEmpty(existingCode)) {
            joinExistingSession(existingCode);
            return;
        }

        createNewSession();
    }

    private void joinExistingSession(String code) {
        try {
            StudySession existing = dao.findSessionByCode(code);
            if (existing == null) {
                Toast.makeText(this,
                        "No sesh found with that code.",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,
                        "Joined sesh: " + existing.name,
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (Exception e) {
            Toast.makeText(this,
                    "Error joining sesh: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void createNewSession() {
        String name      = edtSeshName.getText().toString().trim();
        String startTime = edtStartTime.getText().toString().trim();
        String endTime   = edtEndTime.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            edtSeshName.setError("Enter a sesh name");
            edtSeshName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(startTime)) {
            edtStartTime.setError("Enter a start time (e.g., 3:00 PM)");
            edtStartTime.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(endTime)) {
            edtEndTime.setError("Enter an end time (e.g., 4:30 PM)");
            edtEndTime.requestFocus();
            return;
        }

        String weekdayString = buildWeekdayString();
        if (TextUtils.isEmpty(weekdayString)) {
            Toast.makeText(this,
                    "Select at least one weekday.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        boolean recurring = chkRecurring.isChecked();
        String generatedCode = generateCode();

        StudySession s = new StudySession();
        s.name = name;
        s.startTime = startTime;
        s.endTime = endTime;
        s.weekdays = weekdayString;
        s.recurring = recurring;
        s.code = generatedCode;

        s.focusScore = 0;
        s.phonePickups = 0;
        s.seshStreakDays = 0;
        s.concentrationMinutes = 0;

        try {
            dao.insertSession(s);
            txtGeneratedCode.setText(
                    String.format(Locale.getDefault(),
                            "Generated sesh code: %s", generatedCode));
            Toast.makeText(this,
                    "New study sesh created!",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this,
                    "Error saving sesh: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private String buildWeekdayString() {
        StringBuilder sb = new StringBuilder();
        if (chkMon.isChecked()) appendDay(sb, "Mon");
        if (chkTue.isChecked()) appendDay(sb, "Tue");
        if (chkWed.isChecked()) appendDay(sb, "Wed");
        if (chkThu.isChecked()) appendDay(sb, "Thu");
        if (chkFri.isChecked()) appendDay(sb, "Fri");
        if (chkSat.isChecked()) appendDay(sb, "Sat");
        if (chkSun.isChecked()) appendDay(sb, "Sun");
        return sb.toString();
    }

    private void appendDay(StringBuilder sb, String day) {
        if (sb.length() > 0) {
            sb.append(",");
        }
        sb.append(day);
    }

    private String generateCode() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
}