package be.helha.hakem_android_project.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import be.helha.hakem_android_project.R;
import be.helha.hakem_android_project.db.PillsBaseHelper;
import be.helha.hakem_android_project.models.DayOfTreatment;
import be.helha.hakem_android_project.models.Pill;
import be.helha.hakem_android_project.views.Treatment_screen_controller;
import be.helha.hakem_android_project.models.Treatment;

public class Calendar_fragment_controller extends Fragment {
    DayOfTreatment dayOfTreatment;

    TextView date;

    TextView tvMorning;
    TextView tvNoon;
    TextView tvEvening;

    LinearLayout llMorning;
    LinearLayout llNoon;
    LinearLayout llEvening;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_part_fragment, container, false);
        init();
        return view;
    }

    private void init() {
        date = getView().findViewById(R.id.TV_Date);
        tvMorning = getView().findViewById(R.id.TV_Morning);
        tvNoon = getView().findViewById(R.id.TV_Noon);
        tvEvening = getView().findViewById(R.id.TV_Evening);
        llMorning = getView().findViewById(R.id.LL_Morning);
        llNoon = getView().findViewById(R.id.LL_Noon);
        llEvening = getView().findViewById(R.id.LL_Evening);
    }

    public void setDayOfTreatment(DayOfTreatment dayOfTreatment) {
        this.dayOfTreatment = dayOfTreatment;
        date.setText(dayOfTreatment.getDateString());

        setTextViewVisibility(dayOfTreatment);

        for (Treatment t : dayOfTreatment.getTreatsForMorning())
            addViewPartsOfDay(t, llMorning);
        for (Treatment t : dayOfTreatment.getTreatsForNoon())
            addViewPartsOfDay(t, llNoon);
        for (Treatment t : dayOfTreatment.getTreatsForEvening())
            addViewPartsOfDay(t, llEvening);
    }

    private void addViewPartsOfDay(Treatment t, LinearLayout ll) {
        TextView tv = new TextView(getContext());
        tv.setText(t.getPill().getName());
        tv.setOnClickListener(v -> {
            showTreatmentScreen(t);
        });
        ll.addView(tv);
    }

    private void showTreatmentScreen(Treatment treatment) {
        Intent i = new Intent(getContext(), Treatment_screen_controller.class);
        i.putExtra("treatment", treatment);
        startActivity(i);
    }

    private void setTextViewVisibility(DayOfTreatment dayOfTreatment) {
        if (dayOfTreatment.getTreatsForMorning().isEmpty())
            tvEvening.setVisibility(View.GONE);
        if (dayOfTreatment.getTreatsForNoon().isEmpty())
            tvNoon.setVisibility(View.GONE);
        if (dayOfTreatment.getTreatsForEvening().isEmpty())
            tvEvening.setVisibility(View.GONE);
    }

    public boolean isInitialized() {
        return date != null;
    }

    public Calendar_fragment_controller() {
        super(R.layout.calendar_part_fragment);
    }

}
