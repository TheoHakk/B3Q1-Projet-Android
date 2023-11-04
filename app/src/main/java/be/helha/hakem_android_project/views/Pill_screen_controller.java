package be.helha.hakem_android_project.views;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import be.helha.hakem_android_project.R;
import be.helha.hakem_android_project.models.PartOfDay;
import be.helha.hakem_android_project.models.Pill;

public class Pill_screen_controller extends AppCompatActivity {

    Button upDays;
    Button downDays;
    TextView tv_duration;
    int duration;
    LinearLayout mContainer;
    EditText name;
    Button validate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_pill_activity);
        putFragments();
        init();
    }

    private void init() {
        tv_duration = findViewById(R.id.TV_duration);
        duration = 0;
        upDays = findViewById(R.id.B_up_days);
        downDays = findViewById(R.id.B_down_days);
        mContainer = findViewById(R.id.LL_container);
        validate = findViewById(R.id.B_pill_validate);
        name = findViewById(R.id.E_pill_name);
        setActions();
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
        validate.setOnClickListener(v -> {
            if (!(name.getText().toString().isEmpty()) || !(duration == 0))
                validatePill();
        });
    }

    private void validatePill() {
        Pill pill = null;
        List<PartOfDay> partsOfDay = null;
        try {
            pill = new Pill(name.getText().toString(), duration, null);
        } catch (Exception e) {
            Log.i("ERROR", "validatePill: " + e.getMessage());
            e.getStackTrace();
        }




        if (pill != null)
            Log.i("Pill", "Name : " + pill.getName() + " , Duration : " + pill.getDuration() + " , Parts of day : " + pill.getPartsOfDay());
        else Log.i("Pill", "Pill is null");
    }


    private void putFragments() {


    }



}
