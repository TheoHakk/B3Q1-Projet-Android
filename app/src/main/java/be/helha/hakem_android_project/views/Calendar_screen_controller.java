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
        init();
    }

    private void init() {
        Thread thread = new Thread(() -> {
            treatmentBaseHelper = new TreatmentBaseHelper(this);
            try {
                //TODO C'est ici mon soucis !
                treatmentList = treatmentBaseHelper.getTreatments(this);
                Log.i("Traitement : ", "C'est ok !");

                while(treatmentList.isEmpty())
                    Thread.sleep(1000);

                for(Treatment t : treatmentList)
                    Log.i("Traitement : ", t.toString());

            } catch (Exception e) {
                Log.i("Traitement : ", "C'est pas ok !");
                e.printStackTrace();
            }
        });
        thread.start();
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

    @Override
    protected void onDestroy() {
        treatmentBaseHelper.close();
        super.onDestroy();
    }

}
