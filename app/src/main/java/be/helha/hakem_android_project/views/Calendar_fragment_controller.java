package be.helha.hakem_android_project.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
        init(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //We will now sure that the view is created, and we can use it
        //Otherwise, we would have a NullPointerException
        super.onViewCreated(view, savedInstanceState);
        updateUI();
    }

    public Calendar_fragment_controller(DayOfTreatment dayOfTreatment) {
        super(R.layout.calendar_part_fragment);
        this.dayOfTreatment = dayOfTreatment;
    }

    public void updateUI() {
        date.setText(dayOfTreatment.getDateString());

        setTextViewVisibility(dayOfTreatment);

        for (Treatment t : dayOfTreatment.getTreatsForMorning())
            addViewPartsOfDay(t, llMorning);
        for (Treatment t : dayOfTreatment.getTreatsForNoon())
            addViewPartsOfDay(t, llNoon);
        for (Treatment t : dayOfTreatment.getTreatsForEvening())
            addViewPartsOfDay(t, llEvening);
    }

    private void init(View view) {
        date = view.findViewById(R.id.TV_Date);
        tvMorning = view.findViewById(R.id.TV_Morning);
        tvNoon = view.findViewById(R.id.TV_Noon);
        tvEvening = view.findViewById(R.id.TV_Evening);
        llMorning = view.findViewById(R.id.LL_Morning);
        llNoon = view.findViewById(R.id.LL_Noon);
        llEvening = view.findViewById(R.id.LL_Evening);
    }

    private void addViewPartsOfDay(Treatment t, LinearLayout ll) {
        TextView tv = new TextView(getContext());
        tv.setText(t.getPill().getName());
        tv.setOnClickListener(v -> showTreatmentScreen(t));
        tv.setPadding(150, 0, 0, 0);
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
}
