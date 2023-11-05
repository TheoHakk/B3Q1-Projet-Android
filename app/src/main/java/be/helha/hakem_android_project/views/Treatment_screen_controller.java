package be.helha.hakem_android_project.views;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import be.helha.hakem_android_project.R;
import be.helha.hakem_android_project.models.PartOfDay;
import be.helha.hakem_android_project.models.Pill;
import be.helha.hakem_android_project.models.Treatment;

public class Treatment_screen_controller extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button openDatePickerButtonBeginning;
    Button openDatePickerButtonEnd;
    Button treatmentValidation;
    FloatingActionButton addPill;
    TextView beginningDate;
    TextView endDate;
    Date beginning;
    Date end;
    Treatment treatmentToWorkOn;
    Spinner pillSpinner;
    PartOfDay_fragment_controller partOfDayFragment;

    Pill actualPill;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_treatment_activity);
        if (getIntent().getExtras() != null) {
            treatmentToWorkOn = (Treatment) getIntent().getSerializableExtra("treatment");
        }
        init();
        putFragments();
    }

    private void createNewTreatment() {
        try {
            Pill actualPill = (Pill) pillSpinner.getSelectedItem();
            List<PartOfDay> partsOfDay = partOfDayFragment.getPartsOfDay();
            Date beginning = this.beginning;
            Date end = this.end;
            if (actualPill != null
                    && partsOfDay != null
                    && beginning != null
                    && end != null
                    && end.after(beginning)
                    && !partsOfDay.isEmpty())
                treatmentToWorkOn = new Treatment(actualPill, partOfDayFragment.getPartsOfDay(), beginning, end);
            else {
                treatmentToWorkOn = null;
                Toast.makeText(getApplicationContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.i("Error", Objects.requireNonNull(e.getMessage()));
        }
    }

    private void init() {
        openDatePickerButtonBeginning = findViewById(R.id.B_DP_begin);
        openDatePickerButtonEnd = findViewById(R.id.B_DP_end);
        beginningDate = findViewById(R.id.TV_begin_date);
        endDate = findViewById(R.id.TV_end_date);

        addPill = findViewById(R.id.B_add_pill);
        treatmentValidation = findViewById(R.id.B_treatment_validate);

        pillSpinner = findViewById(R.id.SP_pills_items);

        beginning = null;
        end = null;

        initializePills();
        setActions();
    }

    private void initializePills() {
        List<PartOfDay> partOfDays = new ArrayList<>();
        partOfDays.add(PartOfDay.MORNING);
        partOfDays.add(PartOfDay.EVENING);

        List<PartOfDay> partOfDays2 = new ArrayList<>();
        partOfDays2.add(PartOfDay.NOON);
        partOfDays2.add(PartOfDay.EVENING);

        Pill pill1 = new Pill("Doliprane", 7, partOfDays);
        Pill pill2 = new Pill("Aspirine", 5, partOfDays2);

        List<Pill> pills = new ArrayList<>();
        pills.add(pill1);
        pills.add(pill2);

        PillAdapter adapter = new PillAdapter(this, pills);
        pillSpinner.setAdapter(adapter);

        pillSpinner.setOnItemSelectedListener(this);
    }

    private void setActions() {
        openDatePickerButtonBeginning.setOnClickListener(v -> showDatePickerDialog("beginning"));
        openDatePickerButtonEnd.setOnClickListener(v -> showDatePickerDialog("end"));
        addPill.setOnClickListener(v -> showPillScreen());
        treatmentValidation.setOnClickListener(v -> createNewTreatment());
    }

    private void showPillScreen() {
        Intent intent = new Intent(this, Pill_screen_controller.class);
        startActivity(intent);
    }

    private void showDatePickerDialog(String sender) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                if (sender.equals("end")) {
                    try {
                        if (beginning != null) {
                            end = new Date(selectedYear, selectedMonth, selectedDay);
                            if (end.before(beginning)) {
                                end = null;
                                throw new Exception("La date de fin ne peut pas être avant la date de début");
                            } else {
                                end = new Date(selectedYear, selectedMonth, selectedDay);
                                endDate.setText(selectedDay + "/" + selectedMonth + "/" + selectedYear);
                            }
                        }
                    } catch (Exception e) {
                        endDate.setText("Date error");
                        Toast.makeText(getApplicationContext(), "La date de fin ne peut pas être avant la date de début", Toast.LENGTH_SHORT).show();
                    }
                } else if (sender.equals("beginning")) {
                    beginning = new Date(selectedYear, selectedMonth, selectedDay);
                    beginningDate.setText(selectedDay + "/" + selectedMonth + "/" + selectedYear);
                }
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void putFragments() {
        FragmentManager fm = getSupportFragmentManager();
        partOfDayFragment = (PartOfDay_fragment_controller) fm.findFragmentById(R.id.fragment_container_treatment);
        if (partOfDayFragment == null) {
            partOfDayFragment = new PartOfDay_fragment_controller();
            fm.beginTransaction()
                    .add(R.id.fragment_container_treatment, partOfDayFragment)
                    .commit();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        actualPill = (Pill) adapterView.getItemAtPosition(i);
        partOfDayFragment.setCheckBoxState(actualPill);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
