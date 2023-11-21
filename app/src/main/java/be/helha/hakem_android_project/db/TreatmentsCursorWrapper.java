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

public class TreatmentsCursorWrapper extends CursorWrapper {

    private SQLiteDatabase database;

    public TreatmentsCursorWrapper(Cursor cursor, SQLiteDatabase database) {
        super(cursor);
        this.database = database;
    }

    public List<Treatment> getTreatments() throws ParseException {
        List<Treatment> treatments = new ArrayList<>();

        if (moveToFirst()) {
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
        }
        return treatments;
    }

    private Treatment createTreatWithObtainedInformation(int id, int pillId, String beginningString, String endString, int morning, int noon, int evening) throws ParseException {
        Pill pill = getSpecificPill(pillId);
        List<PartOfDay> partsOfDay = new ArrayList<>();

        if (morning == 1)
            partsOfDay.add(PartOfDay.MORNING);
        if (noon == 1)
            partsOfDay.add(PartOfDay.NOON);
        if (evening == 1)
            partsOfDay.add(PartOfDay.EVENING);

        Calendar beginning = convertStringToCalendar(beginningString);
        Calendar end = convertStringToCalendar(endString);

        Treatment treatment = new Treatment(pill, partsOfDay, beginning, end);
        treatment.setId(id);
        return treatment;
    }

    private static Pill getPillFromCursor(Cursor cursor) {
        @SuppressLint("Range") int pillId = cursor.getInt(cursor.getColumnIndex(DBSchema.PillsTable.Cols.ID));
        @SuppressLint("Range") String pillName = cursor.getString(cursor.getColumnIndex(DBSchema.PillsTable.Cols.NAME));
        @SuppressLint("Range") int duration = cursor.getInt(cursor.getColumnIndex(DBSchema.PillsTable.Cols.DURATION));
        @SuppressLint("Range") int morning = cursor.getInt(cursor.getColumnIndex(DBSchema.PillsTable.Cols.MORNING));
        @SuppressLint("Range") int noon = cursor.getInt(cursor.getColumnIndex(DBSchema.PillsTable.Cols.NOON));
        @SuppressLint("Range") int evening = cursor.getInt(cursor.getColumnIndex(DBSchema.PillsTable.Cols.EVENING));

        List<PartOfDay> partsOfDay = getPartsOfDay(morning, noon, evening);

        Pill pill = new Pill(pillName, duration, partsOfDay);

        pill.setId(pillId);
        return pill;
    }

    public Pill getSpecificPill(int id) {

        String query = "SELECT * FROM " +
                DBSchema.PillsTable.NAME +
                " WHERE " + DBSchema.PillsTable.Cols.ID + " = ?";

        String[] args = {String.valueOf(id)};

        Cursor cursor = database.rawQuery(query, args);

        Pill pill = extractSpecificPill(cursor);
        cursor.close();
        return pill;
    }

    private static Pill extractSpecificPill(Cursor cursor) {
        Pill pill = null;
        if (cursor.moveToFirst()) {
            do {
                pill = getPillFromCursor(cursor);
            } while (cursor.moveToNext());
        }
        return pill;
    }

    private static ArrayList<PartOfDay> getPartsOfDay(int morning, int noon, int evening) {
        List<PartOfDay> partsOfDay = new ArrayList<>();

        if (morning == 1)
            partsOfDay.add(PartOfDay.MORNING);
        if (noon == 1)
            partsOfDay.add(PartOfDay.NOON);
        if (evening == 1)
            partsOfDay.add(PartOfDay.EVENING);

        return (ArrayList<PartOfDay>) partsOfDay;
    }

    private Calendar convertStringToCalendar(String dateString) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = sdf.parse(dateString);
        calendar.setTime(date);
        return calendar;
    }
}
