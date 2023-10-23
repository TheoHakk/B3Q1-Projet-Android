package be.helha.hakem_android_project.controllers;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

import be.helha.hakem_android_project.R;

public class pill_screen_controller extends AppCompatActivity {

    Button upDays;
    Button downDays;
    NumberPicker days;
    LinearLayout mContainer;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_pill_activity);
        init();
        putFragments();
    }

    private void init() {
        upDays = findViewById(R.id.B_up_days);
        downDays = findViewById(R.id.B_down_days);
        days = findViewById(R.id.NP_days);
        mContainer = findViewById(R.id.LL_container);
        setActions();
    }

    private void setActions() {
        upDays.setOnClickListener(v -> {
            days.setValue(days.getValue() + 1);
        });
        downDays.setOnClickListener(v -> {
            days.setValue(days.getValue() - 1);
        });
    }


    private void putFragments(){
        View partOfDay = getRbFragment();
        mContainer.addView(partOfDay);
    }

    private View getRbFragment(){
        View partOfDay = getLayoutInflater().inflate(R.layout.part_rb_fragment, null);
        return partOfDay;
    }




}
