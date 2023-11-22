package be.helha.hakem_android_project.db.cursorWrapper;

import android.database.CursorWrapper;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

import be.helha.hakem_android_project.db.schema.DBSchema;
import be.helha.hakem_android_project.models.PartOfDay;
import be.helha.hakem_android_project.models.Pill;

/**
 * The PillsCursorWrapper class is a cursor wrapper that facilitates the extraction
 * of Pill objects from a database cursor.
 */
public class PillsCursorWrapper extends CursorWrapper {

    /**
     * Constructs a new instance of the PillsCursorWrapper class.
     *
     * @param cursor The Cursor object to be wrapped.
     */
    public PillsCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    /**
     * Construct a list of Pill objects from the cursor.
     *
     * @return The list of Pill objects extracted from the cursor.
     */
    public List<Pill> getPills() {
        List<Pill> pills = new ArrayList<>();
        if (moveToFirst())
            do {
                Pill pill = getPillFromCursor(this);
                pills.add(pill);
            } while (moveToNext());
        close();
        return pills;
    }

    /**
     * Extracts a Pill object from the current cursor position.
     *
     * @param cursor The cursor positioned at the desired data.
     * @return The extracted Pill object.
     */
    @NonNull
    private Pill getPillFromCursor(Cursor cursor) {
        int pillId = cursor.getInt(cursor.getColumnIndexOrThrow(DBSchema.PillsTable.Cols.ID));
        String pillName = cursor.getString(cursor.getColumnIndexOrThrow(DBSchema.PillsTable.Cols.NAME));
        int duration = cursor.getInt(cursor.getColumnIndexOrThrow(DBSchema.PillsTable.Cols.DURATION));
        int morning = cursor.getInt(cursor.getColumnIndexOrThrow(DBSchema.PillsTable.Cols.MORNING));
        int noon = cursor.getInt(cursor.getColumnIndexOrThrow(DBSchema.PillsTable.Cols.NOON));
        int evening = cursor.getInt(cursor.getColumnIndexOrThrow(DBSchema.PillsTable.Cols.EVENING));

        List<PartOfDay> partsOfDay = getPartsOfDay(morning, noon, evening);

        return new Pill(pillName, duration, partsOfDay, pillId);
    }

    /**
     * Converts integer values representing the presence of a part of day in the database
     *
     * @param morning The value representing the presence of morning in the data, who's extract from the database.
     * @param noon    The value representing the presence of noon in the data.
     * @param evening The value representing the presence of evening in the data.
     * @return The list of PartOfDay enum based on the input values.
     */
    private List<PartOfDay> getPartsOfDay(int morning, int noon, int evening) {
        List<PartOfDay> partsOfDay = new ArrayList<>();
        if (morning == 1) partsOfDay.add(PartOfDay.MORNING);
        if (noon == 1) partsOfDay.add(PartOfDay.NOON);
        if (evening == 1) partsOfDay.add(PartOfDay.EVENING);
        return partsOfDay;
    }
}
