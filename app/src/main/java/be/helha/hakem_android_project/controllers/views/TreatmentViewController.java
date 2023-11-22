/**
 * The `TreatmentViewController` class represents the controller for managing the treatment view in the Android app.
 * It extends `AppCompatActivity` and implements `AdapterView.OnItemSelectedListener`.
 */
package be.helha.hakem_android_project.controllers.views;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

import be.helha.hakem_android_project.R;
import be.helha.hakem_android_project.controllers.adapters.PillAdapter;
import be.helha.hakem_android_project.controllers.fragments.PartOfDayFragmentController;
import be.helha.hakem_android_project.db.bank.BankTreatment;
import be.helha.hakem_android_project.db.schema.DBSchema;
import be.helha.hakem_android_project.db.cursorWrapper.PillsCursorWrapper;
import be.helha.hakem_android_project.db.baseHelper.ProjectBaseHelper;
import be.helha.hakem_android_project.models.PartOfDay;
import be.helha.hakem_android_project.models.Pill;
import be.helha.hakem_android_project.models.Treatment;

public class TreatmentViewController extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button mBOpenDatePickerButtonBeginning;
    private Button mBOpenDatePickerButtonEnd;
    private Button mBTreatmentValidation;
    private FloatingActionButton mFABAddPill;
    private FloatingActionButton mFABModifyPill;
    private TextView mTVBeginningDate;
    private TextView mTVEndDate;
    private TextView mTVRecommended_duration;
    private Spinner mSPillSpinner;
    private PartOfDayFragmentController mPartOfDayFragment;
    private ProjectBaseHelper mProjectBaseHelper;
    private Pill mActualPill;
    private Calendar mBeginning;
    private Calendar mEnd;
    private Treatment mTreatmentToWorkOn;


    /**
     * Called when the activity is starting. This is where most initialization should go.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down, this Bundle contains the data it most recently supplied
     *                           in onSaveInstanceState(Bundle). Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_treatment_activity);
        init();
    }

    /**
     * Called after the activity has been paused, typically before onResume().
     * Update the view when we come back to the screen.
     */
    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    /**
     * Initializes the view, pills, actions, fragments, and verifies the intent.
     */
    private void init() {
        initializeView();
        verifyIntent();
        initFragments();
        initializePills();
        setActions();
    }

    /**
     * Verifies the intent for an existing treatment and displays its information.
     */
    private void verifyIntent() {
        if (getIntent().getExtras() != null)
            mTreatmentToWorkOn = (Treatment) getIntent().getSerializableExtra("treatment");
        if (mTreatmentToWorkOn != null)
            showTreatmentInformation();
    }

    /**
     * Displays the information of an existing treatment.
     */
    @SuppressLint("SetTextI18n")
    private void showTreatmentInformation() {
        mBeginning = mTreatmentToWorkOn.getBeginning();
        mEnd = mTreatmentToWorkOn.getEnd();
        mActualPill = mTreatmentToWorkOn.getPill();
        mTVBeginningDate.setText(mBeginning.get(Calendar.DAY_OF_MONTH) + "/" + (mBeginning.get(Calendar.MONTH) + 1) + "/" + mBeginning.get(Calendar.YEAR));
        mTVEndDate.setText(mEnd.get(Calendar.DAY_OF_MONTH) + "/" + (mEnd.get(Calendar.MONTH) + 1) + "/" + mEnd.get(Calendar.YEAR));
        mTVRecommended_duration.setText(getResources().getString(R.string.recommandedDuration) + " " + mActualPill.getDuration() + " @string/days");
        mSPillSpinner.setSelection(mActualPill.getId() - 1);
    }

    /**
     * Puts the fragment for handling parts of the day into the layout.
     */
    private void initFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mPartOfDayFragment = (PartOfDayFragmentController) fragmentManager.findFragmentById(R.id.fragment_container_treatment);
        if (mPartOfDayFragment == null) {
            mPartOfDayFragment = new PartOfDayFragmentController();
            fragmentManager.beginTransaction().add(R.id.fragment_container_treatment, mPartOfDayFragment).commit();
        }
        if (mTreatmentToWorkOn != null) {
            mPartOfDayFragment.setArguments(getBundlePill(mTreatmentToWorkOn.getPill()));
        }
    }

    private Bundle getBundlePill(Pill pill) {
        Bundle bundle = new Bundle();
        Hashtable hashtable = new Hashtable();
        hashtable.put("PARTS_OF_DAY_DIC", pill.getPartsOfDay());
        bundle.putSerializable("PARTS_OF_DAY_DIC", hashtable);
        return bundle;
    }


    /**
     * Creates a new treatment or updates an existing one.
     */
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

    /**
     * Updates the details of an existing treatment.
     *
     * @param actualTreatmentSettings The updated treatment settings.
     */
    private void updateTreatment(Treatment actualTreatmentSettings) {
        ProjectBaseHelper projectBaseHelper = new ProjectBaseHelper(this);
        BankTreatment bankTreatment = new BankTreatment(projectBaseHelper.getWritableDatabase());

        mTreatmentToWorkOn.setBeginning(actualTreatmentSettings.getBeginning());
        mTreatmentToWorkOn.setEnd(actualTreatmentSettings.getEnd());
        mTreatmentToWorkOn.setPartsOfDay(actualTreatmentSettings.getPartsOfDay());
        mTreatmentToWorkOn.setPill(actualTreatmentSettings.getPill());

        bankTreatment.updateTreatment(mTreatmentToWorkOn);
    }

    /**
     * Gets the current treatment details from the view.
     *
     * @return The current treatment, or null if any field is empty or invalid.
     */
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
                treatmentToInsert = new Treatment(actualPill, partsOfDay, beginning, end);
        } catch (Exception e) {
            Log.i("Error ", Objects.requireNonNull(e.getMessage()));
        }
        return treatmentToInsert;
    }

    /**
     * Inserts a new treatment into the database.
     *
     * @param currentTreatment The treatment to be inserted.
     */
    private void insertNewTreatment(Treatment currentTreatment) {
        ProjectBaseHelper projectBaseHelper = new ProjectBaseHelper(this);
        BankTreatment bankTreatment = new BankTreatment(projectBaseHelper.getWritableDatabase());
        bankTreatment.insertTreatment(currentTreatment);
    }

    /**
     * Initializes the views in the layout.
     */
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

    /**
     * Initializes the pills by retrieving them from the database.
     */
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

    /**
     * Sets actions (event listeners) for various buttons and views in the layout.
     */
    private void setActions() {
        mBOpenDatePickerButtonBeginning.setOnClickListener(v -> showDatePickerDialog("beginning"));
        mBOpenDatePickerButtonEnd.setOnClickListener(v -> showDatePickerDialog("end"));
        mFABAddPill.setOnClickListener(v -> showPillScreen(null));
        mFABModifyPill.setOnClickListener(v -> showPillScreen(mSPillSpinner.getSelectedItem() != null ? (Pill) mSPillSpinner.getSelectedItem() : null));
        mBTreatmentValidation.setOnClickListener(v -> createNewTreatment());
    }

    /**
     * Shows the pill screen activity, allowing the user to add or modify a pill.
     *
     * @param pill The pill to be modified, or null if adding a new pill.
     */
    private void showPillScreen(Pill pill) {
        Intent intent = new Intent(this, PillViewController.class);
        intent.putExtra("pill", pill);
        startActivity(intent);
    }

    /**
     * Shows the date picker dialog for selecting the beginning or end date.
     *
     * @param sender A string indicating whether the date picker is for the beginning or end date.
     */
    private void showDatePickerDialog(String sender) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
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

                mTVBeginningDate.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear); // Add 1 to selectedMonth because it starts at 0.
            }
        }, year, month, day);
        datePickerDialog.show();
    }


    /**
     * Called when an item in the spinner is selected.
     *
     * @param adapterView The AdapterView where the selection happened.
     * @param view        The view within the AdapterView that was clicked.
     * @param i           The position of the view in the adapter.
     * @param l           The row id of the item that is selected.
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mActualPill = (Pill) adapterView.getItemAtPosition(i);
        mTVRecommended_duration.setText(getResources().getString(R.string.recommandedDuration) + " " + mActualPill.getDuration() + getResources().getString(R.string.days).toLowerCase());

        //Replace the fragment with the new parts of the day
        FragmentManager fragmentManager = getSupportFragmentManager();
        mPartOfDayFragment = (PartOfDayFragmentController) fragmentManager.findFragmentById(R.id.fragment_container_treatment);
        if (mPartOfDayFragment != null) {
            mPartOfDayFragment.setCheckBoxState(mActualPill.getPartsOfDay());
        }
    }


    /**
     * Called when nothing is selected in the spinner.
     *
     * @param adapterView The AdapterView where nothing is selected.
     */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Nothing to do
    }

    /**
     * Called when the activity is about to be destroyed.
     * Closes the database helper to prevent memory leaks.
     */
    @Override
    protected void onDestroy() {
        mProjectBaseHelper.close();
        super.onDestroy();
    }
}
