package be.helha.hakem_android_project.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import be.helha.hakem_android_project.R;
import be.helha.hakem_android_project.db.TreatmentBaseHelper;
import be.helha.hakem_android_project.models.Treatment;

public class Calendar_screen_controller extends AppCompatActivity {

    FloatingActionButton addTreatment;
    TreatmentBaseHelper treatmentBaseHelper;
    List<Treatment> treatmentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity);
        try {
            init();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void init() throws ParseException {
        getTreatments();
        addTreatment = findViewById(R.id.FB_add_treatment);
        setActions();
    }

    private void getTreatments() {
        treatmentBaseHelper = new TreatmentBaseHelper(this);
        try {
            treatmentList = treatmentBaseHelper.getTreatments(this);
            Log.i("Traitement : ", "C'est ok !");

            for(Treatment t : treatmentList)
                Log.i("Traitement : ", t.getPill().getName());

        } catch (Exception e) {
            Log.i("Traitement : ", "C'est pas ok !");
            e.printStackTrace();
        }
    }

    private void setActions() {
        addTreatment.setOnClickListener(v -> showTreatmentScreen());
    }

    private void showTreatmentScreen() {
        Intent intent = new Intent(this, Treatment_screen_controller.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        treatmentBaseHelper.close();
        super.onDestroy();
    }

}
