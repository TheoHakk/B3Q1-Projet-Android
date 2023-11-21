package be.helha.hakem_android_project.views;

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

public class PartOfDay_fragment_controller extends Fragment {

    private CheckBox mMorning;
    private CheckBox mNoon;
    private  CheckBox mEvening;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.part_rb_fragment, container, false);

        mMorning = view.findViewById(R.id.CB_morning);
        mNoon = view.findViewById(R.id.CB_noon);
        mEvening = view.findViewById(R.id.CB_afternoon);

        return view;
    }


    public List<PartOfDay> getPartsOfDay(){
        List<PartOfDay> partsOfDay = new ArrayList<>();
        if(mMorning.isChecked())
            partsOfDay.add(PartOfDay.MORNING);
        if(mNoon.isChecked())
            partsOfDay.add(PartOfDay.NOON);
        if(mEvening.isChecked())
            partsOfDay.add(PartOfDay.EVENING);
        return partsOfDay;
    }

    public PartOfDay_fragment_controller() {
        super(R.layout.calendar_part_fragment);
    }

    public boolean checkBoxState() {
        return mMorning == null || mNoon == null || mEvening == null;
    }


    public void setCheckBoxState(Pill actualPill) {

        mMorning.setChecked(false);
        mNoon.setChecked(false);
        mEvening.setChecked(false);

        for(PartOfDay partOfDay : actualPill.getPartsOfDay()){
            switch (partOfDay){
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

    public void setCheckBoxState(List<PartOfDay> partsOfDay) {

        mMorning.setChecked(false);
        mNoon.setChecked(false);
        mEvening.setChecked(false);

        for(PartOfDay partOfDay : partsOfDay){
            switch (partOfDay){
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
