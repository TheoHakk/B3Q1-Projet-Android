package be.helha.hakem_android_project.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import be.helha.hakem_android_project.R;
import be.helha.hakem_android_project.models.PartOfDay;

public class PartOfDay_fragment_controller extends Fragment {

    CheckBox morning;
    CheckBox noon;
    CheckBox afternoon;

    TextView title;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.part_rb_fragment, container, false);

        morning = view.findViewById(R.id.CB_morning);
        noon = view.findViewById(R.id.CB_noon);
        afternoon = view.findViewById(R.id.CB_afternoon);
        title = view.findViewById(R.id.TV_title_taken);

        return view;
    }


    public List<PartOfDay> getPartsOfDay(){
        List<PartOfDay> partsOfDay = new ArrayList<>();
        if(morning.isChecked())
            partsOfDay.add(PartOfDay.MORNING);
        if(noon.isChecked())
            partsOfDay.add(PartOfDay.NOON);
        if(afternoon.isChecked())
            partsOfDay.add(PartOfDay.EVENING);
        return partsOfDay;

    }

    public PartOfDay_fragment_controller() {
        super(R.layout.part_of_day_fragment);
    }


}
