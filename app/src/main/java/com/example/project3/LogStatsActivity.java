package com.example.project3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LogStatsActivity extends AppCompatActivity {

    private static final int REQ_LIVE_SESSION = 1001;

    // SharedPreferences for stats (also used by LiveSessionActivity)
    private static final String PREFS_STATS = "sesh_stats_prefs";
    private static final String KEY_LAST_SESH_NAME = "last_sesh_name";
    private static final String KEY_LAST_FOCUS_SCORE = "last_focus_score";
    private static final String KEY_LAST_PHONE_PICKUPS = "last_phone_pickups";
    private static final String KEY_LAST_SESH_STREAK = "last_sesh_streak";
    private static final String KEY_LAST_CONCENTRATION_MIN = "last_concentration_min";

    private EditText edtSeshNameForStats;
    private EditText edtFocusScoreInput;
    private EditText edtPhonePickupsInput;
    private EditText edtSeshStreakInput;
    private EditText edtConcentrationInput;
    private Button btnLiveTrackSession;
    private Button btnSaveStats;
    private Button btnBackHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_stats);   // keep your XML as-is

        edtSeshNameForStats   = findViewById(R.id.edtSeshNameForStats);
        edtFocusScoreInput    = findViewById(R.id.edtFocusScoreInput);
        edtPhonePickupsInput  = findViewById(R.id.edtPhonePickupsInput);
        edtSeshStreakInput    = findViewById(R.id.edtSeshStreakInput);
        edtConcentrationInput = findViewById(R.id.edtConcentrationInput);
        btnLiveTrackSession   = findViewById(R.id.btnLiveTrackSession);
        btnSaveStats          = findViewById(R.id.btnSaveStats);
        btnBackHome           = findViewById(R.id.btnBackHome);

        // Prefill hints/values from last saved stats (optional)
        SharedPreferences prefs = getSharedPreferences(PREFS_STATS, MODE_PRIVATE);

        String lastName = prefs.getString(KEY_LAST_SESH_NAME, "");
        if (!TextUtils.isEmpty(lastName)) {
            edtSeshNameForStats.setText(lastName);
        }

        int lastFocus = prefs.getInt(KEY_LAST_FOCUS_SCORE, -1);
        if (lastFocus >= 0) {
            edtFocusScoreInput.setText(String.valueOf(lastFocus));
        }

        int lastPickups = prefs.getInt(KEY_LAST_PHONE_PICKUPS, -1);
        if (lastPickups >= 0) {
            // user can still overwrite, this is just a hint
            edtPhonePickupsInput.setHint("From last live: " + lastPickups);
        }

        int lastStreak = prefs.getInt(KEY_LAST_SESH_STREAK, -1);
        if (lastStreak >= 0) {
            edtSeshStreakInput.setText(String.valueOf(lastStreak));
        }

        int lastConc = prefs.getInt(KEY_LAST_CONCENTRATION_MIN, -1);
        if (lastConc >= 0) {
            edtConcentrationInput.setText(String.valueOf(lastConc));
        }

        // Start live tracking – layout stays EXACTLY as you posted
        btnLiveTrackSession.setOnClickListener(v -> {
            String seshName = edtSeshNameForStats.getText().toString().trim();
            Intent liveIntent = new Intent(LogStatsActivity.this, LiveSessionActivity.class);
            liveIntent.putExtra("sesh_name", seshName);
            startActivityForResult(liveIntent, REQ_LIVE_SESSION);
        });

        btnSaveStats.setOnClickListener(v -> saveStats());

        btnBackHome.setOnClickListener(v -> finish());
    }

    private void saveStats() {
        String seshName = edtSeshNameForStats.getText().toString().trim();
        String focusStr = edtFocusScoreInput.getText().toString().trim();
        String pickupsStr = edtPhonePickupsInput.getText().toString().trim();
        String streakStr = edtSeshStreakInput.getText().toString().trim();
        String concStr = edtConcentrationInput.getText().toString().trim();

        if (seshName.isEmpty()
                || focusStr.isEmpty()
                || streakStr.isEmpty()
                || concStr.isEmpty()) {
            Toast.makeText(this,
                    "Please fill in all fields (phone pickups can be left blank).",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        int focusScore, seshStreak, concentration, phonePickups;

        try {
            focusScore = Integer.parseInt(focusStr);
            seshStreak = Integer.parseInt(streakStr);
            concentration = Integer.parseInt(concStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this,
                    "Focus, streak, and concentration must be numbers.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences(PREFS_STATS, MODE_PRIVATE);

        // Phone pickups: use typed value if provided; otherwise auto-use last live session
        if (!pickupsStr.isEmpty()) {
            try {
                phonePickups = Integer.parseInt(pickupsStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this,
                        "Phone pickups must be a number.",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            phonePickups = prefs.getInt(KEY_LAST_PHONE_PICKUPS, -1);
            if (phonePickups < 0) {
                Toast.makeText(this,
                        "No live-session pickup count found. Please enter a value.",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Save stats so StatsSummaryActivity can read them (and for next time)
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_LAST_SESH_NAME, seshName);
        editor.putInt(KEY_LAST_FOCUS_SCORE, focusScore);
        editor.putInt(KEY_LAST_PHONE_PICKUPS, phonePickups);
        editor.putInt(KEY_LAST_SESH_STREAK, seshStreak);
        editor.putInt(KEY_LAST_CONCENTRATION_MIN, concentration);
        editor.apply();

        Toast.makeText(this, "Stats saved.", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_LIVE_SESSION && resultCode == RESULT_OK && data != null) {
            int pickups = data.getIntExtra(LiveSessionActivity.EXTRA_PICKUPS, -1);
            if (pickups >= 0) {
                // populate the field and persist for later
                edtPhonePickupsInput.setText(String.valueOf(pickups));
                SharedPreferences prefs = getSharedPreferences(PREFS_STATS, MODE_PRIVATE);
                prefs.edit().putInt(KEY_LAST_PHONE_PICKUPS, pickups).apply();
            }
        }
    }
}
