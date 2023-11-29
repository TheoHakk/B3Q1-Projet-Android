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

import be.helha.hakem_android_project.R;
import be.helha.hakem_android_project.controllers.adapters.PillAdapter;
import be.helha.hakem_android_project.controllers.fragments.PartOfDayFragmentController;
import be.helha.hakem_android_project.db.bank.BankTreatment;
import be.helha.hakem_android_project.db.schema.DBSchema;
import be.helha.hakem_android_project.db.cursorWrapper.PillsCursorWrapper;
import be.helha.hakem_android_project.db.baseHelper.ProjectBaseHelper;
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
    private Treatment mTransmittedTreatment;

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
        showTreatmentInformation();
        initFragments();
        initializePills();
        setActions();
    }

    /**
     * Verifies the intent for an existing treatment and displays its information.
     */
    private void verifyIntent() {
        if (getIntent().getExtras() != null)
            mTransmittedTreatment = (Treatment) getIntent().getSerializableExtra("treatment");
    }

    /**
     * Displays the information of an existing treatment.
     */
    @SuppressLint("SetTextI18n")
    private void showTreatmentInformation() {
        if (mTransmittedTreatment != null) {
            mBeginning = mTransmittedTreatment.getBeginning();
            mEnd = mTransmittedTreatment.getEnd();
            mActualPill = mTransmittedTreatment.getPill();
            mTVBeginningDate.setText(mBeginning.get(Calendar.DAY_OF_MONTH) + "/" + (mBeginning.get(Calendar.MONTH) + 1) + "/" + mBeginning.get(Calendar.YEAR));
            mTVEndDate.setText(mEnd.get(Calendar.DAY_OF_MONTH) + "/" + (mEnd.get(Calendar.MONTH) + 1) + "/" + mEnd.get(Calendar.YEAR));
            mTVRecommended_duration.setText(getResources().getString(R.string.recommandedDuration) + " " + mActualPill.getDuration() + " @string/days");
            mSPillSpinner.setSelection(mActualPill.getId() - 1);
        }
    }

    /**
     * Puts the fragment for handling parts of the day into the layout.
     */
    private void initFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        //Get the fragment if it exists
        mPartOfDayFragment = (PartOfDayFragmentController) fragmentManager.findFragmentById(R.id.fragment_container_treatment);
        if (mPartOfDayFragment == null) {
            //If the fragment doesn't exist, we have to create it
            mPartOfDayFragment = new PartOfDayFragmentController();
            //Then we have to add it to the container
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container_treatment, mPartOfDayFragment)
                    .commit();
        }
        if (mTransmittedTreatment != null)
            //If the transmitted treatment is not null, we have to pass the list of parts of the day to the fragment
            mPartOfDayFragment.setArguments(getBundlePill(mTransmittedTreatment.getPill()));
    }

    /**
     * Gets the bundle for the parts of the day.
     *
     * @param pill The pill to get the parts of the day from.
     * @return The bundle for the parts of the day.
     */
    private Bundle getBundlePill(Pill pill) {
        //We have to use a bundle to pass the list of parts of the day to the fragment
        Bundle bundle = new Bundle();
        //We will use a hashtable to pass the list of parts of the day
        //Hashtable is used because it implements Serializable
        Hashtable hashtable = new Hashtable();
        hashtable.put("PARTS_OF_DAY_DIC", pill.getPartsOfDay());
        bundle.putSerializable("PARTS_OF_DAY_DIC", hashtable);
        return bundle;
    }

    /**
     * Creates a new treatment or updates an existing one.
     */
    private void createNewTreatment() {
        //Get the current treatment obtained from the view's fields
        Treatment treatmentToInsert = getCurrentTreatment();
        if (treatmentToInsert != null) {
            //if we obtained a treatment from the calendar, we can update it into the database
            //otherwise, we will insert it
            if (mTransmittedTreatment != null)
                updateTreatment(treatmentToInsert);
            else
                insertNewTreatment(treatmentToInsert);
            //close the activity
            finish();
        }
    }

    /**
     * Updates the details of an existing treatment.
     *
     * @param actualTreatmentSettings The updated treatment settings.
     */
    private void updateTreatment(Treatment actualTreatmentSettings) {
        //We have to update the treatment in the database
        ProjectBaseHelper projectBaseHelper = new ProjectBaseHelper(this);
        BankTreatment bankTreatment = new BankTreatment(projectBaseHelper.getWritableDatabase());
        //we get the settings of the actual treatment obtained from the view, and set them to the transmitted treatment
        mTransmittedTreatment.setBeginning(actualTreatmentSettings.getBeginning());
        mTransmittedTreatment.setEnd(actualTreatmentSettings.getEnd());
        mTransmittedTreatment.setPartsOfDay(actualTreatmentSettings.getPartsOfDay());
        mTransmittedTreatment.setPill(actualTreatmentSettings.getPill());
        //Then we update the treatment in the database
        bankTreatment.updateTreatment(mTransmittedTreatment);
    }

    /**
     * Gets the current treatment details from the view.
     *
     * @return The current treatment, or null if any field is empty or invalid.
     */
    private Treatment getCurrentTreatment() {
        Treatment treatmentToInsert = null;
        try {
            //Verify if the fields are valid
            verifyFields();
            treatmentToInsert = new Treatment(
                    (Pill) mSPillSpinner.getSelectedItem(),
                    mPartOfDayFragment.getPartsOfDay(),
                    mBeginning,
                    mEnd);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return treatmentToInsert;
    }

    /**
     * Verifies that all fields are filled.
     *
     * @throws IllegalArgumentException If any field is empty.
     */
    private void verifyFields() {
        if ((Pill) mSPillSpinner.getSelectedItem() == null)
            throw new IllegalArgumentException(getResources().getString(R.string.errorNoPillSelected));
        if (mPartOfDayFragment.getPartsOfDay().isEmpty())
            throw new IllegalArgumentException(getResources().getString(R.string.errorNoPartOfDaySelected));
        if (mBeginning == null)
            throw new IllegalArgumentException(getResources().getString(R.string.errorNoBeginningDateSelected));
        if (mEnd == null)
            throw new IllegalArgumentException(getResources().getString(R.string.errorNoEndDateSelected));
        if (mBeginning.after(mEnd))
            throw new IllegalArgumentException(getResources().getString(R.string.errorBeginDateAfterEnd));
    }

    /**
     * Inserts a new treatment into the database.
     *
     * @param currentTreatment The treatment to be inserted.
     */
    private void insertNewTreatment(Treatment currentTreatment) {
        //Open the database
        ProjectBaseHelper projectBaseHelper = new ProjectBaseHelper(this);
        BankTreatment bankTreatment = new BankTreatment(projectBaseHelper.getWritableDatabase());
        //Insert the treatment into the database
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
        //Get all the pills from the database
        //We will use a cursor to get the pills, based on Select * from pills
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBSchema.PillsTable.NAME, null);
        PillsCursorWrapper pillsCursorWrapper = new PillsCursorWrapper(cursor);
        List<Pill> pills = pillsCursorWrapper.getPills();
        //We will set the adapter for the spinner
        //I've create a custom adapter for the spinner, because we have to display the name of the pill, and not the toString method
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
        //Because I want to use the same view as seen in the mockups, I have to use a DatePickerDialog
        Calendar calendar = Calendar.getInstance();
        //If I click on the button for the beginning date, I open the DatePickerDialog for the beginning date
        //If I click on the button for the end date, I open the DatePickerDialog for the end date
        @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            if (sender.equals("end")) {
                Calendar endCalendar = Calendar.getInstance();
                endCalendar.set(selectedYear, selectedMonth, selectedDay);
                mEnd = endCalendar;
                //I have to add 1 to the selectedMonth because it starts at 0
                mTVEndDate.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear); // add 1 to selectedMonth because it starts at 0.
            } else {
                Calendar beginningCalendar = Calendar.getInstance();
                beginningCalendar.set(selectedYear, selectedMonth, selectedDay);
                mBeginning = beginningCalendar;
                //I have to add 1 to the selectedMonth because it starts at 0
                mTVBeginningDate.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear); // Add 1 to selectedMonth because it starts at 0.
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
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
        mTVRecommended_duration.setText(getResources().getString(R.string.recommandedDuration) + " " + mActualPill.getDuration() + " " + getResources().getString(R.string.days).toLowerCase());
        //We have to update the fragment with the new pill
        FragmentManager fragmentManager = getSupportFragmentManager();
        //Get the fragment if it exists
        mPartOfDayFragment = (PartOfDayFragmentController) fragmentManager.findFragmentById(R.id.fragment_container_treatment);
        if (mPartOfDayFragment != null)
            mPartOfDayFragment.setCheckBoxState(mActualPill.getPartsOfDay());
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
