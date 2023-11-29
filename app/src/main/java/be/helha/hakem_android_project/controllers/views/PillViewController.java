package be.helha.hakem_android_project.controllers.views;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.util.Hashtable;

import be.helha.hakem_android_project.R;
import be.helha.hakem_android_project.controllers.fragments.PartOfDayFragmentController;
import be.helha.hakem_android_project.db.bank.BankPill;
import be.helha.hakem_android_project.db.baseHelper.ProjectBaseHelper;
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
    private Pill mTransmittedPill;
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
    }

    /**
     * Initializes the UI components.
     */
    private void init() {
        initializeVars();
        setActions();
        verifyIntent();
        showPillInformation();
        initFragments();
    }

    /**
     * Verifies the intent for any extras, and shows pill information if available.
     */
    private void verifyIntent() {
        //Getting the intent when we start the activity
        if (getIntent().getExtras() != null)
            mTransmittedPill = (Pill) getIntent().getSerializableExtra("pill");
    }

    /**
     * Shows information for the existing pill.
     */
    private void showPillInformation() {
        //If we have a pill, we can show the information
        if (mTransmittedPill != null) {
            mETName.setText(mTransmittedPill.getName());
            mDuration = mTransmittedPill.getDuration();
            mTVDuration.setText(String.valueOf(mTransmittedPill.getDuration()));
        }
    }

    /**
     * Initializes the UI components.
     */
    private void initializeVars() {
        mDuration = 0;
        mTVDuration = findViewById(R.id.TV_duration);
        mBUpDays = findViewById(R.id.B_up_days);
        mBDownDays = findViewById(R.id.B_down_days);
        mBValidate = findViewById(R.id.B_pill_validate);
        mETName = findViewById(R.id.E_pill_name);
    }

    /**
     * Adds the PartOfDayFragmentController to the fragment container.
     */
    private void initFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mPartOfDayFragment = new PartOfDayFragmentController();
        if (mTransmittedPill != null)
            //If the transmitted pill is not null, we have to pass the list of parts of the day to the fragment
            mPartOfDayFragment.setArguments(getBundlePill(mTransmittedPill));
        fragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, mPartOfDayFragment)
                .commit();
    }

    /**
     * Gets the bundle for the specified pill.
     *
     * @param pill The pill for which to get the bundle.
     * @return The bundle for the specified pill.
     */
    private Bundle getBundlePill(Pill pill) {
        Bundle bundle = new Bundle();
        //We will use a hashtable to pass the list of parts of the day
        //Hashtable is used because it implements Serializable
        Hashtable hashtable = new Hashtable();
        hashtable.put("PARTS_OF_DAY_DIC", pill.getPartsOfDay());
        bundle.putSerializable("PARTS_OF_DAY_DIC", hashtable);
        return bundle;
    }

    /**
     * Sets the actions for the UI components.
     */
    private void setActions() {
        //Personnalized component, so we have to set the actions
        //if we upper the number of days, we have to increment the duration
        mBUpDays.setOnClickListener(v -> {
            if (mDuration <= 30)
                mDuration++;
            mTVDuration.setText(String.valueOf(mDuration));
        });
        //if we lower the number of days, we have to decrement the duration
        mBDownDays.setOnClickListener(v -> {
            if (mDuration > 0)
                mDuration--;
            mTVDuration.setText(String.valueOf(mDuration));
        });
        //If we click on the validate button, we have to validate the pill and get back to the previous screen
        mBValidate.setOnClickListener(v -> validatePill());
    }

    /**
     * Validates the pill, either by creating a new one or updating the existing one.
     */
    private void validatePill() {
        //If we have a transmitted pill, we have to update it, otherwise we have to create a new one
        if (mTransmittedPill != null)
            updatePill();
        else
            createNewPill();
    }

    /**
     * Creates a new pill and inserts it into the database.
     */
    private void createNewPill() {
        Pill pill;
        try {
            verifyFields();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
        pill = new Pill(mETName.getText().toString(), mDuration, mPartOfDayFragment.getPartsOfDay());
        //Insert the created pill into the database
        ProjectBaseHelper projectBaseHelper = new ProjectBaseHelper(this);
        BankPill bankPill = new BankPill(projectBaseHelper.getWritableDatabase());
        bankPill.insertPill(pill);
        //Close the screen
        finish();
    }

    /**
     * Verifies that all fields are filled.
     *
     * @throws Exception If any field is empty.
     */
    private void verifyFields() throws Exception {
        //Verify all fields are filled
        if (mETName.getText().toString().isEmpty())
            throw new Exception(String.valueOf(R.string.emptyName));
        if (mDuration == 0)
            throw new Exception(String.valueOf(R.string.durationZero));
        if (mPartOfDayFragment.getPartsOfDay().isEmpty())
            throw new Exception(String.valueOf(R.string.errorNoPartOfDaySelected));
    }

    /**
     * Updates the existing pill in the database.
     */
    private void updatePill() {
        ProjectBaseHelper projectBaseHelper = new ProjectBaseHelper(this);
        BankPill bankPill = new BankPill(projectBaseHelper.getWritableDatabase());
        try {
            verifyFields();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
        mTransmittedPill.setName(mETName.getText().toString());
        mTransmittedPill.setDuration(mDuration);
        mTransmittedPill.setPartsOfDay(mPartOfDayFragment.getPartsOfDay());
        //Update the pill in the database
        bankPill.updatePill(mTransmittedPill);
        //Close the screen
        finish();
    }

    /**
     * Called when the activity is being destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
