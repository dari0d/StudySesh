package com.example.project3;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StatsSummaryActivity extends AppCompatActivity {

    // Same prefs + keys used in LogStatsActivity / LiveSessionActivity
    private static final String PREFS_STATS = "sesh_stats_prefs";
    private static final String KEY_LAST_SESH_NAME = "last_sesh_name";
    private static final String KEY_LAST_FOCUS_SCORE = "last_focus_score";
    private static final String KEY_LAST_PHONE_PICKUPS = "last_phone_pickups";
    private static final String KEY_LAST_SESH_STREAK = "last_sesh_streak";
    private static final String KEY_LAST_CONCENTRATION_MIN = "last_concentration_min";

    // ⚠️ Change these IDs ONLY if your XML uses different ones
    private TextView txtSummarySeshName;
    private TextView txtSummaryFocus;
    private TextView txtSummaryPhonePickups;
    private TextView txtSummarySeshStreak;
    private TextView txtSummaryConcentration;
    private Button   btnBackHomeStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_summary);

        // Hook up to the TextViews that are already in your layout
        txtSummarySeshName       = findViewById(R.id.txtSummarySeshName);
        txtSummaryFocus          = findViewById(R.id.txtSummaryFocus);
        txtSummaryPhonePickups   = findViewById(R.id.txtSummaryPhonePickups);
        txtSummarySeshStreak     = findViewById(R.id.txtSummarySeshStreak);
        txtSummaryConcentration  = findViewById(R.id.txtSummaryConcentration);
        btnBackHomeStats         = findViewById(R.id.btnBackHomeStats);

        // Read the latest values saved by LogStatsActivity
        SharedPreferences prefs = getSharedPreferences(PREFS_STATS, MODE_PRIVATE);

        String seshName   = prefs.getString(KEY_LAST_SESH_NAME, "No logs saved yet");
        int focusScore    = prefs.getInt(KEY_LAST_FOCUS_SCORE, -1);
        int phonePickups  = prefs.getInt(KEY_LAST_PHONE_PICKUPS, -1);
        int seshStreak    = prefs.getInt(KEY_LAST_SESH_STREAK, -1);
        int concentration = prefs.getInt(KEY_LAST_CONCENTRATION_MIN, -1);

        txtSummarySeshName.setText(seshName);

        txtSummaryFocus.setText(
                (focusScore >= 0) ? (focusScore + " / 5") : "No data"
        );

        txtSummaryPhonePickups.setText(
                (phonePickups >= 0) ? String.valueOf(phonePickups) : "No data"
        );

        txtSummarySeshStreak.setText(
                (seshStreak >= 0) ? (seshStreak + " days") : "No data"
        );

        txtSummaryConcentration.setText(
                (concentration >= 0) ? (concentration + " min") : "No data"
        );

        btnBackHomeStats.setOnClickListener(v -> finish());
    }
}
