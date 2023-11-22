package be.helha.hakem_android_project.controllers.views;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import be.helha.hakem_android_project.R;
import be.helha.hakem_android_project.controllers.fragments.CalendarFragmentController;
import be.helha.hakem_android_project.db.schema.DBSchema;
import be.helha.hakem_android_project.db.baseHelper.ProjectBaseHelper;
import be.helha.hakem_android_project.db.cursorWrapper.TreatmentsCursorWrapper;
import be.helha.hakem_android_project.models.DayOfTreatment;
import be.helha.hakem_android_project.models.PartOfDay;
import be.helha.hakem_android_project.models.Treatment;

/**
 * The CalendarViewController class represents the main activity for displaying the treatment calendar.
 */
public class CalendarViewController extends AppCompatActivity {

    public static final int NB_DAYS_CALENDAR = 30;
    private FloatingActionButton mFABAddTreatment;
    private ProjectBaseHelper mProjectBaseHelper;
    private List<Treatment> mTreatmentList;
    private List<DayOfTreatment> calendar;
    private LinearLayout mContainer;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down, then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity);
        init();
    }

    /**
     * Called after onStart() when the activity is becoming visible to the user.
     */
    @Override
    protected void onResume() {
        // We will update the calendar when we come back to the screen
        super.onResume();
        init();
    }

    private void init() {
        mProjectBaseHelper = new ProjectBaseHelper(this);
        getTreatments();
        mFABAddTreatment = findViewById(R.id.FB_add_treatment);
        mContainer = findViewById(R.id.container);
        initializeCalendar();
        updateUI();
        setActions();
    }

    /**
     * Updates the user interface with information about the treatment calendar.
     */
    private void updateUI() {
        mContainer.removeAllViews();
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Insert for each day a fragment
        for (DayOfTreatment d : calendar) {
            if (d.hasTreatment()) {
                CalendarFragmentController fragment = new CalendarFragmentController(d);
                Fragment existingFragment = fragmentManager.findFragmentByTag(fragment.getTag());
                if (existingFragment == null) {
                    fragmentManager.beginTransaction()
                            .add(mContainer.getId(), fragment, fragment.getTag())
                            .commit();
                }
            }
        }
    }

    /**
     * Initializes the treatment calendar.
     */
    private void initializeCalendar() {
        calendar = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        // We will create a calendar of 30 days from today
        for (int i = 0; i <= NB_DAYS_CALENDAR; i++) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + i);
            calendar.add(new DayOfTreatment(cal));
        }
        initializePartsOfDay();
    }

    /**
     * Initializes the parts of the day for each treatment in the calendar.
     */
    private void initializePartsOfDay() {
        // We will search for every treatment that is in the calendar
        // and add it to the corresponding day, and the corresponded part of the day
        for (DayOfTreatment d : calendar)
            for (Treatment t : mTreatmentList)
                if (t.containsTheDate(d.getDate())) {
                    if (t.getPartsOfDay().contains(PartOfDay.MORNING))
                        d.addTreatForMorning(t);
                    if (t.getPartsOfDay().contains(PartOfDay.NOON))
                        d.addTreatForNoon(t);
                    if (t.getPartsOfDay().contains(PartOfDay.EVENING))
                        d.addTreatForEvening(t);
                }
    }

    /**
     * Retrieves the list of treatments from the database.
     */
    private void getTreatments() {
        try {
            SQLiteDatabase db = mProjectBaseHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + DBSchema.TreatmentsTable.NAME, null);

            TreatmentsCursorWrapper cursorWrapper = new TreatmentsCursorWrapper(cursor, db);
            mTreatmentList = cursorWrapper.getTreatments();
        } catch (Exception e) {
            Log.i("Treatment: ", "Not OK! " + e.getMessage());
        }
    }

    /**
     * Sets actions for the FABAddTreatment button.
     */
    private void setActions() {
        mFABAddTreatment.setOnClickListener(v -> showTreatmentScreen());
    }

    /**
     * Shows the treatment screen.
     */
    private void showTreatmentScreen() {
        Intent intent = new Intent(this, TreatmentViewController.class);
        startActivity(intent);
    }

    /**
     * Called as part of the activity lifecycle when an activity is going into the background, but has not (yet) been killed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProjectBaseHelper.close();
        FragmentManager fm = getSupportFragmentManager();
        for (Fragment fragment : fm.getFragments()) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.remove(fragment);
            ft.commitAllowingStateLoss();
        }
    }
}
