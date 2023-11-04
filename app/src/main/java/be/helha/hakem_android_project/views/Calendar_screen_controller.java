package be.helha.hakem_android_project.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import be.helha.hakem_android_project.R;
import be.helha.hakem_android_project.controllers.MainController;

public class Calendar_screen_controller extends AppCompatActivity {

    FloatingActionButton addTreatment;
    MainController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity);
        controller = new MainController();
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
        Intent intent = new Intent(this, Treatment_screen_controller.class);
        startActivity(intent);
    }

}
