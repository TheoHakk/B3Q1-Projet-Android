package be.helha.hakem_android_project.controllers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import be.helha.hakem_android_project.models.Pill;

/**
 * Custom ArrayAdapter for displaying Pills in a Spinner.
 */
public class PillAdapter extends ArrayAdapter<Pill> {
    /**
     * Constructor for the PillAdapter.
     *
     * @param context The context in which the adapter will be used.
     * @param pills   The list of Pill objects to be displayed.
     */
    public PillAdapter(Context context, List<Pill> pills) {
        super(context, 0, pills);
    }

    /**
     * Overrides the getView method to display an element in the Spinner when it is closed.
     *
     * @param position    The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible.
     * @param parent      The parent that this view will eventually be attached to.
     * @return The view for the selected item in the Spinner.
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // The convertView is the element that will be displayed
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
        // Get the position, which is equivalent to that of the list
        Pill pill = getItem(position);
        // Get the TextView that will display the name of the pill
        TextView textView = convertView.findViewById(android.R.id.text1);
        if (pill != null)
            textView.setText(pill.getName());
        return convertView;
    }

    /**
     * Overrides the getDropDownView method to display the elements in the drop-down list
     * when the user scrolls the Spinner to select an item.
     *
     * @param position    The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible.
     * @param parent      The parent that this view will eventually be attached to.
     * @return The view for the selected item in the drop-down list.
     */
    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        // The convertView is the element that will be displayed
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        // Get the position, which is equivalent to that of the list
        Pill pill = getItem(position);
        // Get the TextView that will display the name of the pill
        TextView textView = convertView.findViewById(android.R.id.text1);
        if (pill != null)
            textView.setText(pill.getName());
        return convertView;
    }
}
