package com.example.project3;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ScheduleActivity extends AppCompatActivity {

    private StudyDao dao;
    private CalendarView calendarView;
    private TextView txtDayHeader;
    private LinearLayout listContainer;
    private Button btnBackHome;

    private final DateFormat headerFormat =
            new SimpleDateFormat("MMMM d", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        dao = StudySeshDatabase.getInstance(this).studyDao();

        calendarView = findViewById(R.id.calendarView);
        txtDayHeader = findViewById(R.id.txtDayHeader);
        listContainer = findViewById(R.id.listContainer);
        btnBackHome = findViewById(R.id.btnBackHome);

        btnBackHome.setOnClickListener(v -> {
            finish();
            overridePendingTransition(0, 0);
        });

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar c = Calendar.getInstance();
            c.set(year, month, dayOfMonth);
            updateForDate(c);
        });

        updateForDate(Calendar.getInstance());
    }

    private void updateForDate(Calendar date) {
        txtDayHeader.setText(headerFormat.format(date.getTime()));

        int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
        String dayString = dayOfWeekToShort(dayOfWeek);

        listContainer.removeAllViews();

        List<StudyClass> classes = dao.getAllClasses();
        List<StudySession> sessions = dao.getAllSessions();

        for (StudyClass c : classes) {
            if (c.weekdays != null &&
                    c.weekdays.toLowerCase(Locale.ROOT).contains(dayString.toLowerCase(Locale.ROOT))) {

                TextView tv = new TextView(this);
                tv.setText(String.format(Locale.getDefault(),
                        "Class: %s - %s–%s",
                        c.name,
                        c.startTime,
                        c.endTime));
                tv.setPadding(16, 16, 16, 16);
                tv.setBackgroundResource(R.drawable.schedule_item_background);
                listContainer.addView(tv);
            }
        }

        for (StudySession s : sessions) {
            if (s.weekdays != null &&
                    s.weekdays.toLowerCase(Locale.ROOT).contains(dayString.toLowerCase(Locale.ROOT))) {

                TextView tv = new TextView(this);
                String recurText = s.recurring ? " (recurring)" : "";
                tv.setText(String.format(Locale.getDefault(),
                        "Sesh: %s - %s–%s%s",
                        s.name,
                        s.startTime,
                        s.endTime,
                        recurText));
                tv.setPadding(16, 16, 16, 16);
                tv.setBackgroundResource(R.drawable.schedule_item_background);
                listContainer.addView(tv);
            }
        }

        if (listContainer.getChildCount() == 0) {
            TextView empty = new TextView(this);
            empty.setText("No classes or seshes for this day.");
            empty.setPadding(16, 16, 16, 16);
            listContainer.addView(empty);
        }
    }

    private String dayOfWeekToShort(int day) {
        switch (day) {
            case Calendar.MONDAY: return "Mon";
            case Calendar.TUESDAY: return "Tue";
            case Calendar.WEDNESDAY: return "Wed";
            case Calendar.THURSDAY: return "Thu";
            case Calendar.FRIDAY: return "Fri";
            case Calendar.SATURDAY: return "Sat";
            case Calendar.SUNDAY: return "Sun";
            default: return "";
        }
    }
}