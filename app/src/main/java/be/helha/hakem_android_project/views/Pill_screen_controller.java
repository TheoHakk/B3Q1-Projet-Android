package be.helha.hakem_android_project.views;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;


import be.helha.hakem_android_project.R;
import be.helha.hakem_android_project.db.PillsBaseHelper;
import be.helha.hakem_android_project.models.Pill;

public class Pill_screen_controller extends AppCompatActivity {

    Button upDays;
    Button downDays;
    Button validate;
    TextView tv_duration;
    EditText name;
    int duration;
    PartOfDay_fragment_controller partOfDayFragment;
    Pill pillToWorkOn;


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
            pillToWorkOn = (Pill) getIntent().getSerializableExtra("pill");
        if (pillToWorkOn != null)
            showPillInformations();
    }

    private void showPillInformations() {
        name.setText(pillToWorkOn.getName());
        duration = pillToWorkOn.getDuration();
        tv_duration.setText(String.valueOf(pillToWorkOn.getDuration()));

        //Because of the fragment commit, we need to wait the fragment has instantiate its views
        Thread thread = new Thread(() -> {
            while (!partOfDayFragment.checkBoxState()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            partOfDayFragment.setCheckBoxState(pillToWorkOn);
        });
        thread.start();
    }

    private void init() {
        tv_duration = findViewById(R.id.TV_duration);
        duration = 0;
        upDays = findViewById(R.id.B_up_days);
        downDays = findViewById(R.id.B_down_days);
        validate = findViewById(R.id.B_pill_validate);
        name = findViewById(R.id.E_pill_name);
        setActions();
    }
    private void putFragments() {
        FragmentManager fm = getSupportFragmentManager();
        partOfDayFragment = (PartOfDay_fragment_controller) fm.findFragmentById(R.id.fragment_container);
        if (partOfDayFragment == null) {
            partOfDayFragment = new PartOfDay_fragment_controller();
            fm.beginTransaction()
                    .add(R.id.fragment_container, partOfDayFragment)
                    .commit();
        }
    }
    private void setActions() {
        upDays.setOnClickListener(v -> {
            if (duration < 30)
                duration++;
            tv_duration.setText(String.valueOf(duration));
        });
        downDays.setOnClickListener(v -> {
            if (duration > 0)
                duration--;
            tv_duration.setText(String.valueOf(duration));
        });
        validate.setOnClickListener(v -> validatePill());
    }
    private void validatePill() {
        if(pillToWorkOn != null)
            updatePill();
        else
            createNewPill();
        finish();
    }
    private void createNewPill() {
        Pill pill = null;
        try {
            if (name.getText().toString().isEmpty() || duration == 0)
                throw new Exception("Name or duration is empty");
            pill = new Pill(name.getText().toString(), duration, partOfDayFragment.getPartsOfDay());
        } catch (Exception e) {
            Log.i("ERROR", "validatePill: " + e.getMessage());
            e.getStackTrace();
        }
        if (pill != null){
            PillsBaseHelper pillsBaseHelper = new PillsBaseHelper(this);
            try {
                pillsBaseHelper.insertPill(pill);
            } catch (Exception e) {
                Log.i("ERROR", "validatePill: " + e.getMessage());
            }
        }

    }

    private void updatePill() {
        PillsBaseHelper pillsBaseHelper = new PillsBaseHelper(this);
        try {
           pillToWorkOn.setName(name.getText().toString());
            pillToWorkOn.setDuration(duration);
            pillToWorkOn.setPartOfDays(partOfDayFragment.getPartsOfDay());
            pillsBaseHelper.updatePill(pillToWorkOn);
        } catch (Exception e) {
            Log.i("ERROR", "updatePill: " + e.getMessage());
        }
    }


    @Override
    protected void onDestroy() {
        //TODO s'il y a une db, fermer la db
        super.onDestroy();
    }
}
