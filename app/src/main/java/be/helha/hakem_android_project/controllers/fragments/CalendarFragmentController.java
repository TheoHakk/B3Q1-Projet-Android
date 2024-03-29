package be.helha.hakem_android_project.controllers.fragments;

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
import be.helha.hakem_android_project.controllers.views.TreatmentViewController;
import be.helha.hakem_android_project.models.DayOfTreatment;
import be.helha.hakem_android_project.models.Treatment;

/**
 * The CalendarFragmentController class represents a Fragment for displaying information about a day of treatment.
 */
public class CalendarFragmentController extends Fragment {
    private DayOfTreatment mDayOfTreatment;
    private TextView mTVDate;
    private TextView mTVMorning;
    private TextView mTVNoon;
    private TextView mTVEvening;
    private LinearLayout mLLMorning;
    private LinearLayout mLLNoon;
    private LinearLayout mLLEvening;

    /**
     * Constructs a new instance of the CalendarFragmentController class with a specified day of treatment.
     *
     * @param dayOfTreatment The DayOfTreatment object representing the day for which information is displayed.
     */
    public CalendarFragmentController(DayOfTreatment dayOfTreatment) {
        super(R.layout.calendar_part_fragment);
        this.mDayOfTreatment = dayOfTreatment;
    }

    /**
     * Constructs a new instance of the CalendarFragmentController class.
     */
    public CalendarFragmentController() {
        super(R.layout.calendar_part_fragment);
    }

    /**
     * Initializes the view for the fragment.
     * We know that we have initialize all the components, so now we can
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The root view for the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_part_fragment, container, false);
        init(view);
        return view;
    }

    /**
     * Called immediately after onCreateView(LayoutInflater, ViewGroup, Bundle) has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once they know their view hierarchy has been completely created.
     *
     * @param view               The View returned by onCreateView(LayoutInflater, ViewGroup, Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateUI();
    }

    /**
     * Updates the user interface with information about the day of treatment.
     */
    public void updateUI() {
        if (mDayOfTreatment != null) {
            //Showing the date of the day of treatment
            mTVDate.setText(mDayOfTreatment.getDateString());
            setTextViewVisibility(mDayOfTreatment);
            //We have to add the name of the pills contained into the treatment to the layout
            for (Treatment t : mDayOfTreatment.getTreatsForMorning())
                addViewPartsOfDay(t, mLLMorning);
            for (Treatment t : mDayOfTreatment.getTreatsForNoon())
                addViewPartsOfDay(t, mLLNoon);
            for (Treatment t : mDayOfTreatment.getTreatsForEvening())
                addViewPartsOfDay(t, mLLEvening);
        }
    }

    /**
     * Initializes the fragment.
     *
     * @param view The view of the fragment.
     */
    private void init(View view) {
        mTVDate = view.findViewById(R.id.TV_Date);
        mTVMorning = view.findViewById(R.id.TV_Morning);
        mTVNoon = view.findViewById(R.id.TV_Noon);
        mTVEvening = view.findViewById(R.id.TV_Evening);
        mLLMorning = view.findViewById(R.id.LL_Morning);
        mLLNoon = view.findViewById(R.id.LL_Noon);
        mLLEvening = view.findViewById(R.id.LL_Evening);
    }

    /**
     * Adds a TextView to the layout for the specified pill of the treatment.
     *
     * @param t  The Treatment object to be displayed.
     * @param ll The LinearLayout object representing the layout for the specified part of the day.
     */
    private void addViewPartsOfDay(Treatment t, LinearLayout ll) {
        TextView tv = new TextView(getContext());
        tv.setText(t.getPill().getName());
        //If we click on the TextView, we have to show the screen for
        // the treatment, and we have to pass the treatment to the screen
        tv.setOnClickListener(v -> showTreatmentScreen(t));
        tv.setPadding(150, 0, 0, 0);
        ll.addView(tv);
    }

    /**
     * Shows the screen for the specified treatment.
     *
     * @param treatment The Treatment object for which the screen will be displayed.
     */
    private void showTreatmentScreen(Treatment treatment) {
        Intent i = new Intent(getContext(), TreatmentViewController.class);
        i.putExtra("treatment", treatment);
        startActivity(i);
    }

    /**
     * Show or hide the TextViews for the specified day of treatment.
     * If we have any treatment into the list, we have to show the TextViews otherwise we have to hide them.
     *
     * @param dayOfTreatment The DayOfTreatment object for which the visibility of the TextViews will be set.
     */
    private void setTextViewVisibility(DayOfTreatment dayOfTreatment) {
        //Control for all parts of day, if we have any treatment into the list,
        // we have to show the TextViews otherwise we have to hide them
        if (dayOfTreatment.getTreatsForMorning().isEmpty())
            mTVMorning.setVisibility(View.GONE);
        if (dayOfTreatment.getTreatsForNoon().isEmpty())
            mTVNoon.setVisibility(View.GONE);
        if (dayOfTreatment.getTreatsForEvening().isEmpty())
            mTVEvening.setVisibility(View.GONE);
    }
}
