package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;


import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "ThemePreferences";
    private static final String PREF_THEME = "isDarkTheme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load the theme before setting the content view
        if (isDarkTheme()) {
            setTheme(R.style.Theme_MyApp_Dark);
        } else {
            setTheme(R.style.Theme_MyApp_Light);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        EditText etDob = findViewById(R.id.etDob);
        Button btnCalculate = findViewById(R.id.btnCalculate);
        TextView tvResult = findViewById(R.id.tvResult);
        CheckBox cbYears = findViewById(R.id.cbYears);
        CheckBox cbMonths = findViewById(R.id.cbMonths);
        CheckBox cbDays = findViewById(R.id.cbDays);
        CheckBox cbHours = findViewById(R.id.cbHours);
        ToggleButton themeToggle = findViewById(R.id.themeToggle);

        // Set the initial state of the theme toggle
        themeToggle.setChecked(isDarkTheme());

        // Set theme toggle listener
        themeToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setDarkTheme(isChecked);
            recreate(); // Recreate the activity to apply the theme
        });

        // Set button click listener
        btnCalculate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String dobString = etDob.getText().toString();

                if (!dobString.isEmpty()) {
                    try {
                        // Parse the date in the DD/MM/YYYY format
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate dob = LocalDate.parse(dobString, formatter);

                        LocalDate currentDate = LocalDate.now();

                        // Check if DOB is in the future
                        if (dob.isAfter(currentDate)) {
                            tvResult.setText("Date of birth cannot be in the future.");
                        } else {
                            // Calculate age
                            Period age = Period.between(dob, currentDate);
                            long totalDays = ChronoUnit.DAYS.between(dob, currentDate);
                            long totalHours = totalDays * 24;

                            // Determine selected checkboxes
                            boolean isYearsSelected = cbYears.isChecked();
                            boolean isMonthsSelected = cbMonths.isChecked();
                            boolean isDaysSelected = cbDays.isChecked();
                            boolean isHoursSelected = cbHours.isChecked();

                            StringBuilder result = new StringBuilder();

                            if (isYearsSelected) {
                                result.append(age.getYears()).append(" years ");
                            }
                            if (isMonthsSelected) {
                                result.append(age.getMonths()).append(" months ");
                            }
                            if (isDaysSelected) {
                                result.append(totalDays).append(" days ");
                            }
                            if (isHoursSelected) {
                                result.append(totalHours).append(" hours ");
                            }

                            if (result.length() == 0) {
                                tvResult.setText("Please select at least one option.");
                            } else {
                                result.append("old.");
                                tvResult.setText(result.toString());
                            }
                        }
                    } catch (DateTimeParseException e) {
                        tvResult.setText("Invalid date format. Use DD/MM/YYYY.");
                    }
                } else {
                    tvResult.setText("Please enter your date of birth.");
                }
            }
        });
    }

    // Method to check if the dark theme is enabled
    private boolean isDarkTheme() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return preferences.getBoolean(PREF_THEME, false);
    }

    // Method to save the dark theme preference
    private void setDarkTheme(boolean isDarkTheme) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(PREF_THEME, isDarkTheme);
        editor.apply();
    }
}
