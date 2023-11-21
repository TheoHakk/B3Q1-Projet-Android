package be.helha.hakem_android_project.db;

import android.database.CursorWrapper;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

import be.helha.hakem_android_project.models.PartOfDay;
import be.helha.hakem_android_project.models.Pill;

public class PillsCursorWrapper extends CursorWrapper {

    public PillsCursorWrapper(Cursor cursor ) {
        super(cursor);
    }

    public List<Pill> getPills() {
        List<Pill> pills = new ArrayList<>();
        if (moveToFirst()) {
            do {
                Pill pill = getPillFromCursor(this);
                pills.add(pill);
            } while (moveToNext());
        }
        close();
        return pills;
    }

    @NonNull
    private Pill getPillFromCursor(Cursor cursor) {
        int pillId = cursor.getInt(cursor.getColumnIndexOrThrow(DBSchema.PillsTable.Cols.ID));
        String pillName = cursor.getString(cursor.getColumnIndexOrThrow(DBSchema.PillsTable.Cols.NAME));
        int duration = cursor.getInt(cursor.getColumnIndexOrThrow(DBSchema.PillsTable.Cols.DURATION));
        int morning = cursor.getInt(cursor.getColumnIndexOrThrow(DBSchema.PillsTable.Cols.MORNING));
        int noon = cursor.getInt(cursor.getColumnIndexOrThrow(DBSchema.PillsTable.Cols.NOON));
        int evening = cursor.getInt(cursor.getColumnIndexOrThrow(DBSchema.PillsTable.Cols.EVENING));

        List<PartOfDay> partsOfDay = getPartsOfDay(morning, noon, evening);

        Pill pill = new Pill(pillName, duration, partsOfDay);
        pill.setId(pillId);

        return pill;
    }

    private List<PartOfDay> getPartsOfDay(int morning, int noon, int evening) {
        List<PartOfDay> partsOfDay = new ArrayList<>();

        if (morning == 1) partsOfDay.add(PartOfDay.MORNING);
        if (noon == 1) partsOfDay.add(PartOfDay.NOON);
        if (evening == 1) partsOfDay.add(PartOfDay.EVENING);

        return partsOfDay;
    }
}

