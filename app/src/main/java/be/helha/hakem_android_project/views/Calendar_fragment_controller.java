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
import be.helha.hakem_android_project.models.DayOfTreatment;
import be.helha.hakem_android_project.models.Treatment;

public class Calendar_fragment_controller extends Fragment {
    private DayOfTreatment mDayOfTreatment;
    private TextView mTVDate;
    private TextView mTVMorning;
    private TextView mTVNoon;
    private TextView mTVEvening;
    private LinearLayout mLLMorning;
    private LinearLayout mLLNoon;
    private LinearLayout mLLEvening;

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
        this.mDayOfTreatment = dayOfTreatment;
    }

    public Calendar_fragment_controller() {
        super(R.layout.calendar_part_fragment);
    }


    public void updateUI() {
        if (mDayOfTreatment != null) {
            mTVDate.setText(mDayOfTreatment.getDateString());

            setTextViewVisibility(mDayOfTreatment);

            for (Treatment t : mDayOfTreatment.getTreatsForMorning())
                addViewPartsOfDay(t, mLLMorning);
            for (Treatment t : mDayOfTreatment.getTreatsForNoon())
                addViewPartsOfDay(t, mLLNoon);
            for (Treatment t : mDayOfTreatment.getTreatsForEvening())
                addViewPartsOfDay(t, mLLEvening);
        }
    }

    private void init(View view) {
        mTVDate = view.findViewById(R.id.TV_Date);
        mTVMorning = view.findViewById(R.id.TV_Morning);
        mTVNoon = view.findViewById(R.id.TV_Noon);
        mTVEvening = view.findViewById(R.id.TV_Evening);
        mLLMorning = view.findViewById(R.id.LL_Morning);
        mLLNoon = view.findViewById(R.id.LL_Noon);
        mLLEvening = view.findViewById(R.id.LL_Evening);
    }

    private void addViewPartsOfDay(Treatment t, LinearLayout ll) {
        TextView tv = new TextView(getContext());
        tv.setText(t.getPill().getName());
        tv.setOnClickListener(v -> showTreatmentScreen(t));
        tv.setPadding(150, 0, 0, 0);
        ll.addView(tv);
    }

    private void showTreatmentScreen(Treatment treatment) {
        Intent i = new Intent(getContext(), Treatment_view_controller.class);
        i.putExtra("treatment", treatment);
        startActivity(i);
    }

    private void setTextViewVisibility(DayOfTreatment dayOfTreatment) {
        if (dayOfTreatment.getTreatsForMorning().isEmpty())
            mTVMorning.setVisibility(View.GONE);
        if (dayOfTreatment.getTreatsForNoon().isEmpty())
            mTVNoon.setVisibility(View.GONE);
        if (dayOfTreatment.getTreatsForEvening().isEmpty())
            mTVEvening.setVisibility(View.GONE);
    }

}
