package be.helha.hakem_android_project.views;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import be.helha.hakem_android_project.db.BankTreatment;
import be.helha.hakem_android_project.db.DBSchema;
import be.helha.hakem_android_project.db.PillsCursorWrapper;
import be.helha.hakem_android_project.db.ProjectBaseHelper;
import be.helha.hakem_android_project.db.TreatmentsCursorWrapper;
import be.helha.hakem_android_project.models.PartOfDay;
import be.helha.hakem_android_project.models.Pill;
import be.helha.hakem_android_project.models.Treatment;

public class Treatment_view_controller extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button mBOpenDatePickerButtonBeginning;
    private Button mBOpenDatePickerButtonEnd;
    private Button mBTreatmentValidation;
    private FloatingActionButton mFABAddPill;
    private FloatingActionButton mFABModifyPill;
    private TextView mTVBeginningDate;
    private TextView mTVEndDate;
    private TextView mTVRecommended_duration;
    private Spinner mSPillSpinner;
    private PartOfDay_fragment_controller mPartOfDayFragment;
    private ProjectBaseHelper mProjectBaseHelper;
    private Pill mActualPill;
    private int mDuration;
    private Calendar mBeginning;
    private Calendar mEnd;
    private Treatment mTreatmentToWorkOn;

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
            mTreatmentToWorkOn = (Treatment) getIntent().getSerializableExtra("treatment");
        if (mTreatmentToWorkOn != null)
            showTreatmentInformations();
    }

    private void showTreatmentInformations() {
        mBeginning = mTreatmentToWorkOn.getBeginning();
        mEnd = mTreatmentToWorkOn.getEnd();
        mActualPill = mTreatmentToWorkOn.getPill();
        mDuration = mActualPill.getDuration();
        mTVBeginningDate.setText(mBeginning.get(Calendar.DAY_OF_MONTH) + "/" + (mBeginning.get(Calendar.MONTH) + 1) + "/" + mBeginning.get(Calendar.YEAR));
        mTVEndDate.setText(mEnd.get(Calendar.DAY_OF_MONTH) + "/" + (mEnd.get(Calendar.MONTH) + 1) + "/" + mEnd.get(Calendar.YEAR));
        mTVRecommended_duration.setText(getResources().getString(R.string.recommanded_duration) + " " + mActualPill.getDuration() + " jours");

        mSPillSpinner.setSelection(mActualPill.getId() - 1);
        launchThreadForBoxChecking();
    }

    private void launchThreadForBoxChecking() {
        //Because of the fragment commit, we need to wait the fragment has instantiate its views
        Thread thread = new Thread(() -> {
            while (mPartOfDayFragment.checkBoxState()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            mPartOfDayFragment.setCheckBoxState(mTreatmentToWorkOn.getPartsOfDay());
        });
        thread.start();
    }

    private void createNewTreatment() {
        Treatment treatmentToInsert = getCurrentTreatment();
        if (treatmentToInsert != null) {
            if (mTreatmentToWorkOn != null)
                updateTreatment(treatmentToInsert);
            else
                insertNewTreatment(treatmentToInsert);
            finish();
        } else
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.noEmptyField), Toast.LENGTH_SHORT).show();
    }

    private void updateTreatment(Treatment actualTreatmentSettings) {
        ProjectBaseHelper projectBaseHelper = new ProjectBaseHelper(this);
        BankTreatment bankTreatment = new BankTreatment(projectBaseHelper.getWritableDatabase());

        mTreatmentToWorkOn.setBeginning(actualTreatmentSettings.getBeginning());
        mTreatmentToWorkOn.setEnd(actualTreatmentSettings.getEnd());
        mTreatmentToWorkOn.setPartsOfDay(actualTreatmentSettings.getPartsOfDay());
        mTreatmentToWorkOn.setPill(actualTreatmentSettings.getPill());

        bankTreatment.updateTreatment(mTreatmentToWorkOn);
    }

    private Treatment getCurrentTreatment() {
        Treatment treatmentToInsert = null;
        try {
            Pill actualPill = (Pill) mSPillSpinner.getSelectedItem();
            List<PartOfDay> partsOfDay = mPartOfDayFragment.getPartsOfDay();
            Calendar beginning = this.mBeginning;
            Calendar end = this.mEnd;
            if (actualPill != null
                    && partsOfDay != null
                    && beginning != null
                    && end != null
                    && end.after(beginning)
                    && !partsOfDay.isEmpty())
                treatmentToInsert = new Treatment(actualPill, mPartOfDayFragment.getPartsOfDay(), beginning, end);
        } catch (Exception e) {
            Log.i("Error ", Objects.requireNonNull(e.getMessage()));
        }
        return treatmentToInsert;
    }


    private void insertNewTreatment(Treatment currentTreatment) {
        ProjectBaseHelper projectBaseHelper = new ProjectBaseHelper(this);
        BankTreatment bankTreatment = new BankTreatment(projectBaseHelper.getWritableDatabase());
        bankTreatment.insertTreatment(currentTreatment);
    }


    private void initializeView() {
        mBOpenDatePickerButtonBeginning = findViewById(R.id.B_DP_begin);
        mBOpenDatePickerButtonEnd = findViewById(R.id.B_DP_end);
        mTVBeginningDate = findViewById(R.id.TV_begin_date);
        mTVEndDate = findViewById(R.id.TV_end_date);
        mTVRecommended_duration = findViewById(R.id.TV_recommanded_duration);

        mFABAddPill = findViewById(R.id.B_add_pill);
        mFABModifyPill = findViewById(R.id.B_modify_pill);
        mBTreatmentValidation = findViewById(R.id.B_treatment_validate);

        mSPillSpinner = findViewById(R.id.SP_pills_items);

        mBeginning = null;
        mEnd = null;
    }

    private void initializePills() {
        mProjectBaseHelper = new ProjectBaseHelper(this);

        SQLiteDatabase db = mProjectBaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBSchema.PillsTable.NAME, null);

        PillsCursorWrapper pillsCursorWrapper = new PillsCursorWrapper(cursor);
        List<Pill> pills = pillsCursorWrapper.getPills();

        PillAdapter adapter = new PillAdapter(this, pills);
        mSPillSpinner.setAdapter(adapter);

        mSPillSpinner.setOnItemSelectedListener(this);
    }

    private void setActions() {
        mBOpenDatePickerButtonBeginning.setOnClickListener(v -> showDatePickerDialog("beginning"));
        mBOpenDatePickerButtonEnd.setOnClickListener(v -> showDatePickerDialog("end"));
        mFABAddPill.setOnClickListener(v -> showPillScreen(null));
        mFABModifyPill.setOnClickListener(v -> showPillScreen(mSPillSpinner.getSelectedItem() != null ? (Pill) mSPillSpinner.getSelectedItem() : null));
        mBTreatmentValidation.setOnClickListener(v -> createNewTreatment());
    }


    private void showPillScreen(Pill pill) {
        Intent intent = new Intent(this, Pill_view_controller.class);
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
                        if (mBeginning != null) {
                            Calendar endCalendar = Calendar.getInstance();
                            endCalendar.set(selectedYear, selectedMonth, selectedDay);
                            mEnd = endCalendar;

                            if (mEnd.before(mBeginning) || mEnd.equals(mBeginning)) {
                                mEnd = null;
                                throw new Exception(getResources().getString(R.string.errorBeginDateAfterEnd));
                            } else
                                mTVEndDate.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear); // add 1 to selectedMonth because it starts at 0.
                        }
                    } catch (Exception e) {
                        mTVEndDate.setText(getResources().getString(R.string.dateError));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errorBeginDateAfterEnd), Toast.LENGTH_SHORT).show();
                    }
                } else if (sender.equals("beginning")) {
                    Calendar beginningCalendar = Calendar.getInstance();
                    beginningCalendar.set(selectedYear, selectedMonth, selectedDay);
                    mBeginning = beginningCalendar;

                    mTVBeginningDate.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear); // Ajoutez 1 à selectedMonth car il commence à 0.
                }
            }
        }, year, month, day);
        datePickerDialog.show();
    }


    private void putFragments() {
        FragmentManager fm = getSupportFragmentManager();
        mPartOfDayFragment = (PartOfDay_fragment_controller) fm.findFragmentById(R.id.fragment_container_treatment);
        if (mPartOfDayFragment == null) {
            mPartOfDayFragment = new PartOfDay_fragment_controller();
            fm.beginTransaction()
                    .add(R.id.fragment_container_treatment, mPartOfDayFragment)
                    .commit();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mActualPill = (Pill) adapterView.getItemAtPosition(i);
        mTVRecommended_duration.setText(getResources().getString(R.string.recommanded_duration) + " " + mActualPill.getDuration() + getResources().getString(R.string.days));
        mDuration = mActualPill.getDuration();
        if (mTreatmentToWorkOn == null)
            mPartOfDayFragment.setCheckBoxState(mActualPill);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Nothing to do
    }

    @Override
    protected void onDestroy() {
        mProjectBaseHelper.close();
        super.onDestroy();
    }

}
