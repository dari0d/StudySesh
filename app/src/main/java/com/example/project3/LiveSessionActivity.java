package com.example.project3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LiveSessionActivity extends AppCompatActivity {

    public static final String EXTRA_PICKUPS = "extra_pickups";

    private TextView txtSessionName;
    private TextView txtPickupCount;
    private Button btnEndSession;

    private BroadcastReceiver screenReceiver;
    private int phonePickups = 0;
    private String seshName = "Current Sesh";

    // same prefs that LogStatsActivity will use
    private static final String PREFS_STATS = "sesh_stats_prefs";
    private static final String KEY_LAST_PHONE_PICKUPS = "last_phone_pickups";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_session);

        txtSessionName = findViewById(R.id.txtSessionName);
        txtPickupCount = findViewById(R.id.txtPickupCount);
        btnEndSession  = findViewById(R.id.btnEndSession);

        // name comes from LogStatsActivity (optional)
        String extraName = getIntent().getStringExtra("sesh_name");
        if (extraName != null && !extraName.trim().isEmpty()) {
            seshName = extraName.trim();
        }
        txtSessionName.setText("Tracking: " + seshName);

        txtPickupCount.setText("0");

        // count “pickups” whenever the screen turns on / user unlocks
        screenReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (Intent.ACTION_SCREEN_ON.equals(action)
                        || Intent.ACTION_USER_PRESENT.equals(action)) {
                    phonePickups++;
                    txtPickupCount.setText(String.valueOf(phonePickups));
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(screenReceiver, filter);

        btnEndSession.setOnClickListener(v -> endSession());
    }

    private void endSession() {
        // Save last pickup count so LogStatsActivity can reuse it
        SharedPreferences prefs = getSharedPreferences(PREFS_STATS, MODE_PRIVATE);
        prefs.edit().putInt(KEY_LAST_PHONE_PICKUPS, phonePickups).apply();

        // Also return it to the caller (LogStatsActivity)
        Intent data = new Intent();
        data.putExtra(EXTRA_PICKUPS, phonePickups);
        setResult(RESULT_OK, data);

        Toast.makeText(this,
                "Live session ended. Pickups: " + phonePickups,
                Toast.LENGTH_SHORT).show();

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (screenReceiver != null) {
            unregisterReceiver(screenReceiver);
        }
    }
}
