package be.helha.hakem_android_project.db;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import be.helha.hakem_android_project.models.PartOfDay;
import be.helha.hakem_android_project.models.Pill;
import be.helha.hakem_android_project.models.Treatment;

/**
 * The TreatmentsCursorWrapper class is a cursor wrapper that facilitates the extraction
 * of Treatment objects from a database cursor.
 * Only for getting the treatments.
 */
public class TreatmentsCursorWrapper extends CursorWrapper {

    private SQLiteDatabase mDatabase;

    /**
     * Constructs a new instance of the TreatmentsCursorWrapper class.
     *
     * @param cursor   The Cursor object to be wrapped.
     * @param database The SQLiteDatabase instance.
     */
    public TreatmentsCursorWrapper(Cursor cursor, SQLiteDatabase database) {
        super(cursor);
        this.mDatabase = database;
    }

    /**
     * Retrieves a list of Treatment objects from the cursor.
     *
     * @return The list of Treatment objects extracted from the cursor.
     * @throws ParseException If there is an error parsing date strings.
     */
    public List<Treatment> getTreatments() throws ParseException {
        List<Treatment> treatments = new ArrayList<>();
        if (moveToFirst())
            do {
                int id = getInt(getColumnIndex(DBSchema.TreatmentsTable.Cols.ID));
                int pillId = getInt(getColumnIndex(DBSchema.TreatmentsTable.Cols.PILLID));
                String beginningString = getString(getColumnIndex(DBSchema.TreatmentsTable.Cols.BEGINNING));
                String endString = getString(getColumnIndex(DBSchema.TreatmentsTable.Cols.ENDING));
                int morning = getInt(getColumnIndex(DBSchema.TreatmentsTable.Cols.MORNING));
                int noon = getInt(getColumnIndex(DBSchema.TreatmentsTable.Cols.NOON));
                int evening = getInt(getColumnIndex(DBSchema.TreatmentsTable.Cols.EVENING));

                treatments.add(createTreatWithObtainedInformation(id, pillId, beginningString, endString, morning, noon, evening));
            } while (moveToNext());
        return treatments;
    }

    /**
     * Creates a Treatment object using obtained information from the cursor.
     *
     * @param id              The ID of the treatment.
     * @param pillId          The ID of the associated pill.
     * @param beginningString The beginning date string.
     * @param endString       The ending date string.
     * @param morning         The value representing the presence of morning in the database.
     * @param noon            The value representing the presence of noon in the database.
     * @param evening         The value representing the presence of evening in the database.
     * @return The created Treatment object.
     * @throws ParseException If there is an error parsing date strings.
     */
    private Treatment createTreatWithObtainedInformation(int id, int pillId, String beginningString, String endString, int morning, int noon, int evening) throws ParseException {
        //We have to obtain the pill object from the database
        Pill pill = getSpecificPill(pillId);
        //We have to obtain the parts of day from the database
        List<PartOfDay> partsOfDay = getPartsOfDay(morning, noon, evening);

        Calendar beginning = convertStringToCalendar(beginningString);
        Calendar end = convertStringToCalendar(endString);

        Treatment treatment = new Treatment(pill, partsOfDay, beginning, end);
        treatment.setId(id);
        return treatment;
    }

    /**
     * Extracts a Pill object from the cursor based on the given ID.
     *
     * @param id The ID of the pill to extract.
     * @return The extracted Pill object.
     */
    public Pill getSpecificPill(int id) {
        String query = "SELECT * FROM " +
                DBSchema.PillsTable.NAME +
                " WHERE " + DBSchema.PillsTable.Cols.ID + " = ?";
        String[] args = {String.valueOf(id)};

        Cursor cursor = mDatabase.rawQuery(query, args);

        Pill pill = extractSpecificPill(cursor);
        cursor.close();
        return pill;
    }

    /**
     * Extracts a Pill object from the cursor.
     *
     * @param cursor The cursor positioned at the desired data.
     * @return The extracted Pill object.
     */
    private static Pill extractSpecificPill(Cursor cursor) {
        Pill pill = null;
        if (cursor.moveToFirst())
            do {
                pill = getPillFromCursor(cursor);
            } while (cursor.moveToNext());
        return pill;
    }

    /**
     * Extracts a Pill object from the cursor.
     *
     * @param cursor The cursor positioned at the desired data.
     * @return The extracted Pill object.
     */
    private static Pill getPillFromCursor(Cursor cursor) {
        //@SuppressLint("Range") is used to suppress the warning about the use of the getColumnIndex method.
        @SuppressLint("Range") int pillId = cursor.getInt(cursor.getColumnIndex(DBSchema.PillsTable.Cols.ID));
        @SuppressLint("Range") String pillName = cursor.getString(cursor.getColumnIndex(DBSchema.PillsTable.Cols.NAME));
        @SuppressLint("Range") int duration = cursor.getInt(cursor.getColumnIndex(DBSchema.PillsTable.Cols.DURATION));
        @SuppressLint("Range") int morning = cursor.getInt(cursor.getColumnIndex(DBSchema.PillsTable.Cols.MORNING));
        @SuppressLint("Range") int noon = cursor.getInt(cursor.getColumnIndex(DBSchema.PillsTable.Cols.NOON));
        @SuppressLint("Range") int evening = cursor.getInt(cursor.getColumnIndex(DBSchema.PillsTable.Cols.EVENING));

        List<PartOfDay> partsOfDay = getPartsOfDay(morning, noon, evening);
        return new Pill(pillName, duration, partsOfDay, pillId);
    }

    /**
     * Converts integer values representing parts of the day into a list of PartOfDay enum.
     *
     * @param morning The value representing the presence of morning in the data.
     * @param noon    The value representing the presence of noon in the data.
     * @param evening The value representing the presence of evening in the data.
     * @return The list of PartOfDay enum based on the input values.
     */
    private static ArrayList<PartOfDay> getPartsOfDay(int morning, int noon, int evening) {
        ArrayList<PartOfDay> partsOfDay = new ArrayList<>();
        if (morning == 1)
            partsOfDay.add(PartOfDay.MORNING);
        if (noon == 1)
            partsOfDay.add(PartOfDay.NOON);
        if (evening == 1)
            partsOfDay.add(PartOfDay.EVENING);
        return partsOfDay;
    }

    /**
     * Converts a string of a date in format dd/MM/yyyy to a Calendar object.
     *
     * @param dateString The date string to be converted.
     * @return The Calendar object representing the date.
     * @throws ParseException If there is an error parsing the date string.
     */
    private Calendar convertStringToCalendar(String dateString) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = sdf.parse(dateString);
        assert date != null;
        calendar.setTime(date);
        return calendar;
    }
}
