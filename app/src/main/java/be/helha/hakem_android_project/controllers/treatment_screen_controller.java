package be.helha.hakem_android_project.controllers;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;

import be.helha.hakem_android_project.R;

public class treatment_screen_controller extends AppCompatActivity {
    Button openDatePickerButtonBeginning;
    Button openDatePickerButtonEnd;

    TextView beginningDate;
    TextView endDate;

    Date beginning;
    Date end;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_treatment_screen);
        openDatePickerButtonBeginning = findViewById(R.id.B_DP_beginning);
        openDatePickerButtonBeginning.setOnClickListener(v -> showDatePickerDialog("beginning"));
        openDatePickerButtonEnd = findViewById(R.id.B_DP_end);
        openDatePickerButtonEnd.setOnClickListener(v -> showDatePickerDialog("end"));
        beginningDate = findViewById(R.id.TV_beginning_date);
        endDate = findViewById(R.id.TV_end_date);
    }

    private void showDatePickerDialog(String Sender) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                //Il faut gérer la date ici

                if (Sender.equals("end")) {
                    end = new Date(selectedYear, selectedMonth, selectedDay);
                    endDate.setText(selectedDay + "/" + selectedMonth + "/" + selectedYear);
                } else {
                    beginning = new Date(selectedYear, selectedMonth, selectedDay);
                    beginningDate.setText(selectedDay + "/" + selectedMonth + "/" + selectedYear);
                }


            }
        }, year, month, day);

        datePickerDialog.show();
    }


}
