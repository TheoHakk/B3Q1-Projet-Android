package be.helha.hakem_android_project.views;

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
import be.helha.hakem_android_project.models.Treatment;

public class Treatment_screen_controller extends AppCompatActivity {
    Button openDatePickerButtonBeginning;
    Button openDatePickerButtonEnd;
    Button treatmentValidation;
    FloatingActionButton addPill;
    TextView beginningDate;
    TextView endDate;
    Date beginning;
    Date end;
    LinearLayout mContainer;
    Treatment treatmentToWorkOn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_treatment_activity);
        createNewTreatment();

        if (getIntent().getExtras() != null) {
            treatmentToWorkOn = (Treatment) getIntent().getSerializableExtra("treatment");
        }


        init();
        putFragments();
    }

    private void createNewTreatment() {
        //TODO

    }

    private void init(){
        openDatePickerButtonBeginning = findViewById(R.id.B_DP_beginning);
        openDatePickerButtonEnd = findViewById(R.id.B_DP_end);
        beginningDate = findViewById(R.id.TV_beginning_date);
        endDate = findViewById(R.id.TV_end_date);
        addPill = findViewById(R.id.B_add_pill);
        mContainer = findViewById(R.id.LL_container);
        treatmentValidation = findViewById(R.id.B_treatment_validate);
        setActions();
    }

    private void setActions(){
        openDatePickerButtonBeginning.setOnClickListener(v -> showDatePickerDialog("beginning"));
        openDatePickerButtonEnd.setOnClickListener(v -> showDatePickerDialog("end"));
        addPill.setOnClickListener(v -> showPillScreen());
        treatmentValidation.setOnClickListener(v -> registeringTreatment());
    }

    private void registeringTreatment() {
        if(treatmentToWorkOn == null){
            //TODO
        }else{
            //TODO
        }
    }

    private void showPillScreen() {
        Intent intent = new Intent(this, Pill_screen_controller.class);
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
