package be.helha.hakem_android_project.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import be.helha.hakem_android_project.R;
import be.helha.hakem_android_project.models.Treatment;

public class Calendar_fragment_controller extends Fragment {

    TextView partOfDay;
    RecyclerView recyclerView;
    List<Treatment> treatments;
    LinearLayout mContainer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_part_fragment, container, false);

        /*
        TODO : -Landscape
               -Enregistrer les états
               -Actualiser les états une fois qu'ils sont modifiés
               -S'assurer de l'actualisation des données
         */


        init();

        return view;
    }

    private void init() {
        partOfDay = getView().findViewById(R.id.TV_moment);
        recyclerView = getView().findViewById(R.id.LL_container);

    }
    public void setTreatments(List<Treatment> treatments) {
        this.treatments = treatments;
    }

    public Calendar_fragment_controller() {
        super(R.layout.calendar_part_fragment);
    }



}
