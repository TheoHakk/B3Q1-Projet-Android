package be.helha.hakem_android_project.controllers;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;

import be.helha.hakem_android_project.R;

public class treatment_screen_controller extends AppCompatActivity {
    Button openDatePickerButtonBeginning;
    Button openDatePickerButtonEnd;
    FloatingActionButton addPill;
    TextView beginningDate;
    TextView endDate;
    Date beginning;
    Date end;
    LinearLayout mContainer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_treatment_activity);
        init();
        putFragments();
    }

    private void init(){
        openDatePickerButtonBeginning = findViewById(R.id.B_DP_beginning);
        openDatePickerButtonEnd = findViewById(R.id.B_DP_end);
        beginningDate = findViewById(R.id.TV_beginning_date);
        endDate = findViewById(R.id.TV_end_date);
        addPill = findViewById(R.id.B_add_pill);
        mContainer = findViewById(R.id.LL_container);
        setActions();
    }

    private void setActions(){
        openDatePickerButtonBeginning.setOnClickListener(v -> showDatePickerDialog("beginning"));
        openDatePickerButtonEnd.setOnClickListener(v -> showDatePickerDialog("end"));
        addPill.setOnClickListener(v -> showPillScreen());
    }



    private void showPillScreen() {
        Intent intent = new Intent(this, pill_screen_controller.class);
        startActivity(intent);
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

    private void putFragments(){
        View partOfDay = getRbFragment();
        mContainer.addView(partOfDay);
    }

    private View getRbFragment(){
        View partOfDay = getLayoutInflater().inflate(R.layout.part_rb_fragment, null);
        return partOfDay;
    }


}
