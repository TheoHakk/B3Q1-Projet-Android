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
import java.util.List;
import java.util.Objects;

import be.helha.hakem_android_project.R;
import be.helha.hakem_android_project.db.PillsBaseHelper;
import be.helha.hakem_android_project.db.TreatmentBaseHelper;
import be.helha.hakem_android_project.models.PartOfDay;
import be.helha.hakem_android_project.models.Pill;
import be.helha.hakem_android_project.models.Treatment;

public class Treatment_screen_controller extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button openDatePickerButtonBeginning;
    Button openDatePickerButtonEnd;
    Button treatmentValidation;
    FloatingActionButton addPill;
    FloatingActionButton modifyPill;
    TextView beginningDate;
    TextView endDate;
    TextView recommended_duration;
    Calendar beginning;
    Calendar end;
    Treatment treatmentToWorkOn;
    Spinner pillSpinner;
    PartOfDay_fragment_controller partOfDayFragment;
    PillsBaseHelper pillsBaseHelper;
    Pill actualPill;
    int duration;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_treatment_activity);
        init();
    }

    @Override
    protected void onResume() {
        //Update the view when we come back to the screen
        super.onResume();
        init();
    }

    private void init() {
        initializeView();
        initializePills();
        setActions();
        putFragments();
        verifyIntent();
    }

    private void verifyIntent() {
        if (getIntent().getExtras() != null)
            treatmentToWorkOn = (Treatment) getIntent().getSerializableExtra("treatment");
        if (treatmentToWorkOn != null)
            showTreatmentInformations();


    }

    private void showTreatmentInformations() {
        beginning = treatmentToWorkOn.getBeginning();
        end = treatmentToWorkOn.getEnd();
        actualPill = treatmentToWorkOn.getPill();
        duration = actualPill.getDuration();
        beginningDate.setText(beginning.get(Calendar.DAY_OF_MONTH) + "/" + (beginning.get(Calendar.MONTH) + 1) + "/" + beginning.get(Calendar.YEAR));
        endDate.setText(end.get(Calendar.DAY_OF_MONTH) + "/" + (end.get(Calendar.MONTH) + 1) + "/" + end.get(Calendar.YEAR));
        recommended_duration.setText(getResources().getString(R.string.recommanded_duration) + " " + actualPill.getDuration() + " jours");

        pillSpinner.setSelection(actualPill.getId() - 1);
        //Because of the fragment commit, we need to wait the fragment has instantiate its views
        Thread thread = new Thread(() -> {
            while (!partOfDayFragment.checkBoxState()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            partOfDayFragment.setCheckBoxState(treatmentToWorkOn.getPartsOfDay());
        });
        thread.start();
    }

    private void createNewTreatment() {
        Treatment treatmentToInsert = getCurrentTreatment();
        if (treatmentToInsert != null) {
            if (treatmentToWorkOn != null)
                updateTreatment(treatmentToInsert);
            else
                insertNewTreatment(treatmentToInsert);
            finish();
        } else
            Toast.makeText(getApplicationContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
    }

    private void updateTreatment(Treatment actualTreatmentSettings) {
        TreatmentBaseHelper treatmentBaseHelper = new TreatmentBaseHelper(this);
        treatmentToWorkOn.setBeginning(actualTreatmentSettings.getBeginning());
        treatmentToWorkOn.setEnd(actualTreatmentSettings.getEnd());
        treatmentToWorkOn.setPartsOfDay(actualTreatmentSettings.getPartsOfDay());
        treatmentToWorkOn.setPill(actualTreatmentSettings.getPill());

        treatmentBaseHelper.updateTreatment(treatmentToWorkOn);
    }

    private Treatment getCurrentTreatment() {
        Treatment treatmentToInsert = null;
        try {
            Pill actualPill = (Pill) pillSpinner.getSelectedItem();
            List<PartOfDay> partsOfDay = partOfDayFragment.getPartsOfDay();
            Calendar beginning = this.beginning;
            Calendar end = this.end;
            if (actualPill != null
                    && partsOfDay != null
                    && beginning != null
                    && end != null
                    && end.after(beginning)
                    && !partsOfDay.isEmpty())
                treatmentToInsert = new Treatment(actualPill, partOfDayFragment.getPartsOfDay(), beginning, end);
        } catch (Exception e) {
            Log.i("Error ", Objects.requireNonNull(e.getMessage()));
        }
        return treatmentToInsert;
    }


    private void insertNewTreatment(Treatment currentTreatment) {
       TreatmentBaseHelper treatmentBaseHelper = new TreatmentBaseHelper(this);
        treatmentBaseHelper.insertTreatment(currentTreatment);
    }


    private void initializeView() {
        openDatePickerButtonBeginning = findViewById(R.id.B_DP_begin);
        openDatePickerButtonEnd = findViewById(R.id.B_DP_end);
        beginningDate = findViewById(R.id.TV_begin_date);
        endDate = findViewById(R.id.TV_end_date);
        recommended_duration = findViewById(R.id.TV_recommanded_duration);

        addPill = findViewById(R.id.B_add_pill);
        modifyPill = findViewById(R.id.B_modify_pill);
        treatmentValidation = findViewById(R.id.B_treatment_validate);

        pillSpinner = findViewById(R.id.SP_pills_items);

        beginning = null;
        end = null;
    }

    private void initializePills() {
        pillsBaseHelper = new PillsBaseHelper(this);
        List<Pill> pills = new ArrayList<>();
        try {
            pills = pillsBaseHelper.getPills();
        } catch (Exception e) {
            Log.i("Pills ! ", "problème : " + e.getMessage());
        }

        PillAdapter adapter = new PillAdapter(this, pills);
        pillSpinner.setAdapter(adapter);

        pillSpinner.setOnItemSelectedListener(this);
    }

    private void setActions() {
        openDatePickerButtonBeginning.setOnClickListener(v -> showDatePickerDialog("beginning"));
        openDatePickerButtonEnd.setOnClickListener(v -> showDatePickerDialog("end"));
        addPill.setOnClickListener(v -> showPillScreen(null));
        modifyPill.setOnClickListener(v -> showPillScreen(pillSpinner.getSelectedItem() != null ? (Pill) pillSpinner.getSelectedItem() : null));
        treatmentValidation.setOnClickListener(v -> createNewTreatment());
    }


    private void showPillScreen(Pill pill) {
        Intent intent = new Intent(this, Pill_screen_controller.class);
        intent.putExtra("pill", pill);
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
                            Calendar endCalendar = Calendar.getInstance();
                            endCalendar.set(selectedYear, selectedMonth, selectedDay);
                            end = endCalendar;

                            Calendar dt = Calendar.getInstance();
                            dt.setTime(beginning.getTime());
                            dt.add(Calendar.DATE, duration);

                            if (end.before(beginning) || end.equals(beginning) || end.before(dt)) {
                                end = null;
                                throw new Exception("La date de fin ne peut pas être avant la date de début, et doit respecter la durée du traitement");
                            } else {
                                endDate.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear); // Ajoutez 1 à selectedMonth car il commence à 0.
                            }
                        }
                    } catch (Exception e) {
                        endDate.setText("Date error");
                        Toast.makeText(getApplicationContext(), "La date de fin ne peut pas être avant la date de début", Toast.LENGTH_SHORT).show();
                    }
                } else if (sender.equals("beginning")) {
                    Calendar beginningCalendar = Calendar.getInstance();
                    beginningCalendar.set(selectedYear, selectedMonth, selectedDay);
                    beginning = beginningCalendar;

                    beginningDate.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear); // Ajoutez 1 à selectedMonth car il commence à 0.
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        actualPill = (Pill) adapterView.getItemAtPosition(i);
        recommended_duration.setText(getResources().getString(R.string.recommanded_duration) + " " + actualPill.getDuration() + " jours");
        duration = actualPill.getDuration();
        partOfDayFragment.setCheckBoxState(actualPill);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    protected void onDestroy() {
        pillsBaseHelper.close();
        super.onDestroy();
    }

}
