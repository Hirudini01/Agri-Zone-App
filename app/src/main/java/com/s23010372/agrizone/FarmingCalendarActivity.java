package com.s23010372.agrizone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FarmingCalendarActivity extends AppCompatActivity {

    private TextView tvCurrentMonth;
    private Calendar currentDate = Calendar.getInstance();
    private GridView calendarGrid;
    private LinearLayout tasksContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farming_calendar);

        // Initialize views
        tvCurrentMonth = findViewById(R.id.tvCurrentMonth);
        calendarGrid = findViewById(R.id.calendarGrid);
        tasksContainer = findViewById(R.id.tasksContainer);
        ImageButton btnPrevMonth = findViewById(R.id.btnPrevMonth);
        ImageButton btnNextMonth = findViewById(R.id.btnNextMonth);
        Button btnAddTask = findViewById(R.id.btnAddTask);

        // Set current month
        updateMonthHeader();
        setupCalendarGrid();

        // Month navigation
        btnPrevMonth.setOnClickListener(v -> {
            currentDate.add(Calendar.MONTH, -1);
            updateMonthHeader();
            setupCalendarGrid();
        });

        btnNextMonth.setOnClickListener(v -> {
            currentDate.add(Calendar.MONTH, 1);
            updateMonthHeader();
            setupCalendarGrid();
        });

        // Add task button
        btnAddTask.setOnClickListener(v -> showAddTaskDialog());

        // Load sample tasks
        loadSampleTasks();

        // Setup bottom navigation
        setupBottomNavigation();
    }

    private void updateMonthHeader() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        tvCurrentMonth.setText(sdf.format(currentDate.getTime()));
    }

    private void setupCalendarGrid() {
        List<Date> days = new ArrayList<>();
        Calendar month = (Calendar) currentDate.clone();

        // Set to first day of month
        month.set(Calendar.DAY_OF_MONTH, 1);

        // Add previous month's days
        int firstDayOfWeek = month.get(Calendar.DAY_OF_WEEK);
        Calendar prevMonth = (Calendar) month.clone();
        prevMonth.add(Calendar.MONTH, -1);
        int prevMonthDays = prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = firstDayOfWeek - 1; i > 0; i--) {
            prevMonth.set(Calendar.DAY_OF_MONTH, prevMonthDays - i + 1);
            days.add(prevMonth.getTime());
        }

        // Add current month's days
        int daysInMonth = month.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= daysInMonth; i++) {
            month.set(Calendar.DAY_OF_MONTH, i);
            days.add(month.getTime());
        }

        // Add next month's days
        int totalCells = 42; // 6 rows * 7 columns
        int nextMonthDays = totalCells - days.size();
        Calendar nextMonth = (Calendar) month.clone();
        nextMonth.add(Calendar.MONTH, 1);

        for (int i = 1; i <= nextMonthDays; i++) {
            nextMonth.set(Calendar.DAY_OF_MONTH, i);
            days.add(nextMonth.getTime());
        }

        calendarGrid.setAdapter(new CalendarAdapter(days, currentDate));
    }

    private void loadSampleTasks() {
        addTask("Fertilizer Application", "09:00 AM - Field A", false);
        addTask("Irrigation Check", "06:00 AM - All fields", true);
        addTask("Pest Inspection", "02:00 PM - Field B & C", false);
    }

    private void addTask(String title, String details, boolean completed) {
        View taskView = LayoutInflater.from(this).inflate(R.layout.task_item, tasksContainer, false);

        CheckBox cbTask = taskView.findViewById(R.id.cbTask);
        TextView tvTaskTitle = taskView.findViewById(R.id.tvTaskTitle);
        TextView tvTaskDetails = taskView.findViewById(R.id.tvTaskDetails);

        tvTaskTitle.setText(title);
        tvTaskDetails.setText(details);
        cbTask.setChecked(completed);

        cbTask.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle task completion status change
            if (isChecked) {
                tvTaskTitle.setAlpha(0.5f);
                tvTaskDetails.setAlpha(0.5f);
            } else {
                tvTaskTitle.setAlpha(1f);
                tvTaskDetails.setAlpha(1f);
            }
        });

        tasksContainer.addView(taskView);
    }

    private void showAddTaskDialog() {
        // Implement dialog for adding new tasks
        // This would show a dialog where user can enter task details
        // For simplicity, we'll just add a sample task
        addTask("New Custom Task", "Time and location", false);
    }

    private void setupBottomNavigation() {
        findViewById(R.id.nav_home).setOnClickListener(v -> finish());
        findViewById(R.id.nav_training).setOnClickListener(v -> {
            // Navigate to Training
        });
        findViewById(R.id.nav_market).setOnClickListener(v -> {
            // Navigate to Market
        });
        findViewById(R.id.nav_profile).setOnClickListener(v -> {
            // Navigate to Profile
        });
    }

    // Calendar Adapter
    private class CalendarAdapter extends BaseAdapter {
        private final List<Date> days;
        private final Calendar currentMonth;

        public CalendarAdapter(List<Date> days, Calendar currentMonth) {
            this.days = days;
            this.currentMonth = currentMonth;
        }

        @Override
        public int getCount() {
            return days.size();
        }

        @Override
        public Object getItem(int position) {
            return days.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.calendar_day_item, parent, false);
            }

            Date date = days.get(position);
            Calendar dayCal = Calendar.getInstance();
            dayCal.setTime(date);

            TextView tvDay = convertView.findViewById(R.id.tvDay);
            View indicator = convertView.findViewById(R.id.activityIndicator);

            // Set day number
            tvDay.setText(String.valueOf(dayCal.get(Calendar.DAY_OF_MONTH)));

            // Style for different months
            if (dayCal.get(Calendar.MONTH) != currentMonth.get(Calendar.MONTH)) {
                tvDay.setTextColor(getResources().getColor(R.color.light_gray));
            } else {
                tvDay.setTextColor(getResources().getColor(R.color.gray));

                // Highlight today
                Calendar today = Calendar.getInstance();
                if (dayCal.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                        dayCal.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                        dayCal.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
                    convertView.setBackgroundResource(R.drawable.today_bg);
                }
            }

            // Show indicator if there are activities
            indicator.setVisibility(position % 7 == 0 ? View.VISIBLE : View.GONE);

            return convertView;
        }
    }
}