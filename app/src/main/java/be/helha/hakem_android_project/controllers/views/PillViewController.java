package be.helha.hakem_android_project.controllers.views;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import be.helha.hakem_android_project.R;
import be.helha.hakem_android_project.controllers.fragments.PartOfDayFragmentController;
import be.helha.hakem_android_project.db.bank.BankPill;
import be.helha.hakem_android_project.db.baseHelper.ProjectBaseHelper;
import be.helha.hakem_android_project.models.PartOfDay;
import be.helha.hakem_android_project.models.Pill;

/**
 * The PillViewController class represents the controller for managing the view to add or edit a pill.
 */
public class PillViewController extends AppCompatActivity {

    private Button mBUpDays;
    private Button mBDownDays;
    private Button mBValidate;
    private TextView mTVDuration;
    private EditText mETName;
    private PartOfDayFragmentController mPartOfDayFragment;
    private Pill mPillToWorkOn;
    private int mDuration;

    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_pill_activity);
        init();
        verifyIntent();
    }

    /**
     * Verifies the intent for any extras, and shows pill information if available.
     */
    private void verifyIntent() {
        if (getIntent().getExtras() != null)
            mPillToWorkOn = (Pill) getIntent().getSerializableExtra("pill");
        if (mPillToWorkOn != null)
            showPillInformation();
        initFragments();
    }

    /**
     * Shows information for the existing pill.
     */
    private void showPillInformation() {
        mETName.setText(mPillToWorkOn.getName());
        mDuration = mPillToWorkOn.getDuration();
        mTVDuration.setText(String.valueOf(mPillToWorkOn.getDuration()));
    }

    /**
     * Initializes the UI components.
     */
    private void init() {
        mTVDuration = findViewById(R.id.TV_duration);
        mDuration = 0;
        mBUpDays = findViewById(R.id.B_up_days);
        mBDownDays = findViewById(R.id.B_down_days);
        mBValidate = findViewById(R.id.B_pill_validate);
        mETName = findViewById(R.id.E_pill_name);
        setActions();
    }

    /**
     * Adds the PartOfDayFragmentController to the fragment container.
     */
    private void initFragments() {
        FragmentManager fm = getSupportFragmentManager();
        mPartOfDayFragment = (PartOfDayFragmentController) fm.findFragmentById(R.id.fragment_container);
        if (mPartOfDayFragment == null) {
            if (mPillToWorkOn != null) {
                mPartOfDayFragment = new PartOfDayFragmentController();
                fm.beginTransaction()
                        .add(
                                R.id.fragment_container,
                                PartOfDayFragmentController.class,
                                getBundlePartsOfDay(mPillToWorkOn))
                        .commit();

            }
        }
    }

    private Bundle getBundlePartsOfDay(Pill pill) {
        Bundle bundle = new Bundle();
        List<PartOfDay> partsOfDay = pill.getPartsOfDay();
        Hashtable hashtable = new Hashtable();
        String PARTS_OF_DAY_DIC = "PARTS_OF_DAY_DIC";
        hashtable.put(PARTS_OF_DAY_DIC, partsOfDay);
        bundle.putSerializable(PARTS_OF_DAY_DIC, hashtable);
        return bundle;
    }

    /**
     * Sets the actions for the UI components.
     */
    private void setActions() {
        mBUpDays.setOnClickListener(v -> {
            if (mDuration < 30)
                mDuration++;
            mTVDuration.setText(String.valueOf(mDuration));
        });
        mBDownDays.setOnClickListener(v -> {
            if (mDuration > 0)
                mDuration--;
            mTVDuration.setText(String.valueOf(mDuration));
        });
        mBValidate.setOnClickListener(v -> validatePill());
    }

    /**
     * Validates the pill, either by creating a new one or updating the existing one.
     */
    private void validatePill() {
        if (mPillToWorkOn != null)
            updatePill();
        else
            createNewPill();
        finish();
    }

    /**
     * Creates a new pill and inserts it into the database.
     */
    private void createNewPill() {
        Pill pill = null;
        try {
            if (mETName.getText().toString().isEmpty() || mDuration == 0)
                throw new Exception("Name or duration is empty");
            pill = new Pill(mETName.getText().toString(), mDuration, mPartOfDayFragment.getPartsOfDay());
        } catch (Exception e) {
            Log.i("ERROR", "validatePill: " + e.getMessage());
        }
        if (pill != null) {
            ProjectBaseHelper projectBaseHelper = new ProjectBaseHelper(this);
            BankPill bankPill = new BankPill(projectBaseHelper.getWritableDatabase());
            try {
                bankPill.insertPill(pill);
            } catch (Exception e) {
                Log.i("ERROR", "validatePill: " + e.getMessage());
            }
        }
    }

    /**
     * Updates the existing pill in the database.
     */
    private void updatePill() {
        ProjectBaseHelper projectBaseHelper = new ProjectBaseHelper(this);
        BankPill bankPill = new BankPill(projectBaseHelper.getWritableDatabase());
        try {
            mPillToWorkOn.setName(mETName.getText().toString());
            mPillToWorkOn.setDuration(mDuration);
            mPillToWorkOn.setPartsOfDay(mPartOfDayFragment.getPartsOfDay());
            bankPill.updatePill(mPillToWorkOn);
        } catch (Exception e) {
            Log.i("ERROR", "updatePill: " + e.getMessage());
        }
    }

    /**
     * Called when the activity is being destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
