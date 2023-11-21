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
            This method is called to display an element
            in the Spinner when the latter is closed.
         */
        //The convertView is the element that will be displayed
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);

        //I get a position, the one of the item on which I am, which is equivalent to that of the list
        Pill pill = getItem(position);

        //I get the TextView that will display the name of the pill
        TextView textView = convertView.findViewById(android.R.id.text1);
        if (pill != null)
            textView.setText(pill.getName());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        /*
            This method is called to display the elements in
            the drop-down list when the user scrolls the Spinner
            to select an item.
         */
        //The convertView is the element that will be displayed
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);

        //I get a position, the one of the item on which I am, which is equivalent to that of the list
        Pill pill = getItem(position);

        //I get the TextView that will display the name of the pill
        TextView textView = convertView.findViewById(android.R.id.text1);
        if (pill != null)
            textView.setText(pill.getName());

        return convertView;
    }
}
