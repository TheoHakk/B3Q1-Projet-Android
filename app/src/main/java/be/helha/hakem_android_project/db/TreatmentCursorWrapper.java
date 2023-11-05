package be.helha.hakem_android_project.db;


import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import be.helha.hakem_android_project.models.PartOfDay;
import be.helha.hakem_android_project.models.Pill;
import be.helha.hakem_android_project.models.PillsDbSchema;

public class TreatmentCursorWrapper extends CursorWrapper {

    public TreatmentCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Pill getPill() {
        String name = getString(getColumnIndex(PillsDbSchema.Cols.NAME));
        int duration = getInt(getColumnIndex(PillsDbSchema.Cols.DURATION));
        int morning = getInt(getColumnIndex(PillsDbSchema.Cols.MORNING));
        int noon = getInt(getColumnIndex(PillsDbSchema.Cols.NOON));
        int evening = getInt(getColumnIndex(PillsDbSchema.Cols.EVENING));

        List<PartOfDay> partOfDays = new ArrayList<>();

        if (morning == 1)
            partOfDays.add(PartOfDay.MORNING);
        if (noon == 1)
            partOfDays.add(PartOfDay.NOON);
        if (evening == 1)
            partOfDays.add(PartOfDay.EVENING);

        return new Pill(name, duration, partOfDays);

    }
}

