package be.helha.hakem_android_project.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import be.helha.hakem_android_project.R;
import be.helha.hakem_android_project.db.TreatmentBaseHelper;
import be.helha.hakem_android_project.models.DayOfTreatment;
import be.helha.hakem_android_project.models.PartOfDay;
import be.helha.hakem_android_project.models.Treatment;

public class Calendar_screen_controller extends AppCompatActivity {

    FloatingActionButton addTreatment;
    TreatmentBaseHelper treatmentBaseHelper;
    List<Treatment> treatmentList;
    List<DayOfTreatment> calendar;
    LinearLayout mContainer;

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
        mContainer = findViewById(R.id.dayOfTreatmentList);

        initializeCalendar();
        updateUI();
        setActions();
    }


    private void updateUI() {
        mContainer.removeAllViews();
        try {
            for (final DayOfTreatment day : calendar) {
                if (day.hasTreatment()) {
                    TextView tv = new TextView(getApplicationContext());
                    tv.setPadding(20, 0, 0, 10);
                    tv.setText(day.getDateString());
                    mContainer.addView(tv);

                    TextView tv2 = new TextView(getApplicationContext());
                    tv2.setPadding(50, 0, 0, 0);
                    if (!day.getTreatsForMorning().isEmpty())
                        tv2.setText("Morning : ");
                    mContainer.addView(tv2);

                    for (Treatment t : day.getTreatsForMorning()) {
                        TextView textView = new TextView(getApplicationContext());
                        textView.setOnClickListener(v -> showTreatmentScreen(t));
                        textView.setPadding(75, 0, 0, 0);
                        textView.setText(t.getPill().getName());
                        mContainer.addView(textView);
                    }

                    TextView tv3 = new TextView(getApplicationContext());
                    tv3.setPadding(50, 0, 0, 0);
                    if (!day.getTreatsForNoon().isEmpty())
                        tv3.setText("Noon : ");
                    mContainer.addView(tv3);

                    for (Treatment t : day.getTreatsForNoon()) {
                        TextView textView = new TextView(getApplicationContext());
                        textView.setOnClickListener(v -> showTreatmentScreen(t));
                        textView.setPadding(75, 0, 0, 0);
                        textView.setText(t.getPill().getName());
                        mContainer.addView(textView);
                    }

                    TextView tv4 = new TextView(getApplicationContext());
                    tv4.setPadding(50, 0, 0, 0);
                    if (!day.getTreatsForEvening().isEmpty())
                        tv4.setText("Evening : ");
                    mContainer.addView(tv4);

                    for (Treatment t : day.getTreatsForEvening()) {
                        TextView textView = new TextView(getApplicationContext());
                        textView.setOnClickListener(v -> showTreatmentScreen(t));
                        textView.setPadding(75, 0, 0, 0);
                        textView.setText(t.getPill().getName());
                        mContainer.addView(textView);
                    }
                }
            }
        } catch (Exception e) {
            Log.i("ALERT", "updateUI: " + e.getMessage());
        }
    }




    private void initializeCalendar() {
        calendar = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        Log.i("Calendar : ", c.getTime().toString());
        //We will create a calendar of 30 days from today
        for(int i = 0; i <= 30 ; i++) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + i);
            calendar.add(new DayOfTreatment(cal));
        }

        //We will search for every treatment that is in the calendar
        //and add it to the corresponding day, and the corresponding part of the day
        for(DayOfTreatment d : calendar)
            for(Treatment t : treatmentList)
                if(t.containsTheDate(d.getDate())){
                    if(t.getPartsOfDay().contains(PartOfDay.MORNING))
                        d.addTreatForMorning(t);
                    if(t.getPartsOfDay().contains(PartOfDay.NOON))
                        d.addTreatForNoon(t);
                    if(t.getPartsOfDay().contains(PartOfDay.EVENING))
                        d.addTreatForEvening(t);
                }
    }

    private void getTreatments() {
        treatmentBaseHelper = new TreatmentBaseHelper(this);
        try {
            treatmentList = treatmentBaseHelper.getTreatments(this);
            Log.i("Traitement : ", "C'est ok !");
            for (Treatment t : treatmentList)
                Log.i("Traitement : ", t.getPill().getName());
        } catch (Exception e) {
            Log.i("Traitement : ", "C'est pas ok !");
            e.printStackTrace();
        }
    }

    private void setActions() {
        addTreatment.setOnClickListener(v -> showTreatmentScreen(null));
    }

    private void showTreatmentScreen(Treatment treatment) {
        Intent intent = new Intent(this, Treatment_screen_controller.class);
        if (treatment != null)
            intent.putExtra("treatment", treatment);

        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        treatmentBaseHelper.close();
        super.onDestroy();
    }

}
