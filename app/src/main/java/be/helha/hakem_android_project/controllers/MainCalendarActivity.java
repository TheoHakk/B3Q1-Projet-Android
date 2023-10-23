package be.helha.hakem_android_project.controllers;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import be.helha.hakem_android_project.R;

public class MainCalendarActivity extends AppCompatActivity {

    FloatingActionButton addTreatment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity);
        init();
    }

    private void init(){
        addTreatment = findViewById(R.id.FB_add_treatment);
        setActions();
    }

    private void setActions() {
        addTreatment.setOnClickListener(v -> showTreatmentScreen());
    }

    private void showTreatmentScreen() {
        Intent intent = new Intent(this, treatment_screen_controller.class);
        startActivity(intent);
    }

}
