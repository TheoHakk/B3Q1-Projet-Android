package be.helha.hakem_android_project.views;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;


import be.helha.hakem_android_project.R;
import be.helha.hakem_android_project.db.BankPill;
import be.helha.hakem_android_project.db.ProjectBaseHelper;
import be.helha.hakem_android_project.models.Pill;

public class Pill_view_controller extends AppCompatActivity {

    private Button mBUpDays;
    private Button mBDownDays;
    private Button mBValidate;
    private  TextView mTVDuration;
    private EditText mETName;
    private PartOfDay_fragment_controller mPartOfDayFragment;
    private Pill mPillToWorkOn;
    private int mDuration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_pill_activity);
        init();
        putFragments();
        verifyIntent();
    }

    private void verifyIntent() {
        if (getIntent().getExtras() != null)
            mPillToWorkOn = (Pill) getIntent().getSerializableExtra("pill");
        if (mPillToWorkOn != null)
            showPillInformations();
    }

    private void showPillInformations() {
        mETName.setText(mPillToWorkOn.getName());
        mDuration = mPillToWorkOn.getDuration();
        mTVDuration.setText(String.valueOf(mPillToWorkOn.getDuration()));

        //Because of the fragment commit, we need to wait the fragment has instantiate its views
        Thread thread = new Thread(() -> {
            while (mPartOfDayFragment.checkBoxState()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            mPartOfDayFragment.setCheckBoxState(mPillToWorkOn);
        });
        thread.start();
    }

    private void init() {
        mTVDuration = findViewById(R.id.TV_duration);
        mDuration = 0;
        mBUpDays = findViewById(R.id.B_up_days);
        mBDownDays = findViewById(R.id.B_down_days);
        mBValidate = findViewById(R.id.B_pill_validate);
        mETName = findViewById(R.id.E_pill_name);
        setActions();
    }

    private void putFragments() {
        FragmentManager fm = getSupportFragmentManager();
        mPartOfDayFragment = (PartOfDay_fragment_controller) fm.findFragmentById(R.id.fragment_container);
        if (mPartOfDayFragment == null) {
            mPartOfDayFragment = new PartOfDay_fragment_controller();
            fm.beginTransaction()
                    .add(R.id.fragment_container, mPartOfDayFragment)
                    .commit();
        }
    }

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

    private void validatePill() {
        if (mPillToWorkOn != null)
            updatePill();
        else
            createNewPill();
        finish();
    }

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


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
