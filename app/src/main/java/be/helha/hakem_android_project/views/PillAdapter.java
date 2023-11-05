package be.helha.hakem_android_project.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import be.helha.hakem_android_project.models.Pill;

public class PillAdapter extends ArrayAdapter<Pill> {
    public PillAdapter(Context context, List<Pill> pills) {
        super(context, 0, pills);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*
            Cette méthode est appelée pour afficher un élément
            dans le Spinner lorsque ce dernier est fermé.
         */

        //La convertView est l'élément qui va être affiché
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);

        //J'obtiens une position, celle de l'item sur lequel je suis, qui équivaut à celle de la liste
        Pill pill = getItem(position);


        //Je récupère le TextView qui va afficher le nom de la pill
        TextView textView = convertView.findViewById(android.R.id.text1);
        if (pill != null)
            textView.setText(pill.getName());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        /*
            Cette méthode est appelée pour afficher les éléments dans
            la liste déroulante lorsque l'utilisateur déroule le Spinner
            pour sélectionner un élément.
         */

        //La convertView est l'élément qui va être affiché
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);

        //Idem, j'obtiens une position, celle de l'item sur lequel je suis, qui équivaut à celle de la liste
        Pill pill = getItem(position);

        //Je récupère le TextView qui va afficher le nom de la pill
        TextView textView = convertView.findViewById(android.R.id.text1);
        if (pill != null) {
            textView.setText(pill.getName());
        }

        return convertView;
    }
}
