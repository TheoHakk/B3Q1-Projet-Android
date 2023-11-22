package be.helha.hakem_android_project.controllers.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import be.helha.hakem_android_project.R;
import be.helha.hakem_android_project.models.PartOfDay;
import be.helha.hakem_android_project.models.Pill;

/**
 * The PartOfDayFragmentController class represents a fragment for selecting parts of the day using checkboxes.
 */
public class PartOfDayFragmentController extends Fragment {

    private CheckBox mMorning;
    private CheckBox mNoon;
    private CheckBox mEvening;

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     *                           The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.part_rb_fragment, container, false);

        mMorning = view.findViewById(R.id.CB_morning);
        mNoon = view.findViewById(R.id.CB_noon);
        mEvening = view.findViewById(R.id.CB_afternoon);

        return view;
    }

    /**
     * Gets the selected parts of the day.
     *
     * @return A list of selected parts of the day.
     */
    public List<PartOfDay> getPartsOfDay() {
        List<PartOfDay> partsOfDay = new ArrayList<>();
        if (mMorning.isChecked())
            partsOfDay.add(PartOfDay.MORNING);
        if (mNoon.isChecked())
            partsOfDay.add(PartOfDay.NOON);
        if (mEvening.isChecked())
            partsOfDay.add(PartOfDay.EVENING);
        return partsOfDay;
    }

    /**
     * Default constructor for PartOfDayFragmentController.
     */
    public PartOfDayFragmentController() {
        super(R.layout.calendar_part_fragment);
    }

    /**
     * Checks if any of the checkboxes is null.
     *
     * @return True if any of the checkboxes is null, false otherwise.
     */
    public boolean checkBoxState() {
        return mMorning == null || mNoon == null || mEvening == null;
    }

    /**
     * Sets the state of checkboxes based on the parts of the day of the given pill.
     *
     * @param actualPill The pill whose parts of the day are used to set the checkbox state.
     */
    public void setCheckBoxState(Pill actualPill) {

        mMorning.setChecked(false);
        mNoon.setChecked(false);
        mEvening.setChecked(false);

        for (PartOfDay partOfDay : actualPill.getPartsOfDay()) {
            switch (partOfDay) {
                case MORNING:
                    mMorning.setChecked(true);
                    break;
                case NOON:
                    mNoon.setChecked(true);
                    break;
                case EVENING:
                    mEvening.setChecked(true);
                    break;
            }
        }
    }

    /**
     * Sets the state of checkboxes based on the provided list of parts of the day.
     *
     * @param partsOfDay The list of parts of the day used to set the checkbox state.
     */
    public void setCheckBoxState(List<PartOfDay> partsOfDay) {

        mMorning.setChecked(false);
        mNoon.setChecked(false);
        mEvening.setChecked(false);

        for (PartOfDay partOfDay : partsOfDay) {
            switch (partOfDay) {
                case MORNING:
                    mMorning.setChecked(true);
                    break;
                case NOON:
                    mNoon.setChecked(true);
                    break;
                case EVENING:
                    mEvening.setChecked(true);
                    break;
            }
        }
    }
}
