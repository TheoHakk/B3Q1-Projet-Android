package be.helha.hakem_android_project.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import be.helha.hakem_android_project.models.PartOfDay;
import be.helha.hakem_android_project.models.Pill;
import be.helha.hakem_android_project.models.Treatment;

public class ProjectBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1; // version de la base de donnée, va servir à savoir s'il faut mettre à jour le code
    private static final String DATABASE_NAME = "db_onlypills.db";

    public ProjectBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Method called when we need to CREATE the db file
        //So, note for a dumb guy like me : method is called when the file doesn't exist
        sqLiteDatabase.execSQL(createTablePills());
        sqLiteDatabase.execSQL(createTableTreatment());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //We will do here the update of the database
    }

    private String createTableTreatment() {
        return "Create Table if not exists " +
                DBSchema.TreatmentsTable.NAME + "(" +
                DBSchema.TreatmentsTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBSchema.TreatmentsTable.Cols.PILLID + " INTEGER NOT NULL, " +
                DBSchema.TreatmentsTable.Cols.BEGINNING + " TEXT NOT NULL, " +
                DBSchema.TreatmentsTable.Cols.ENDING + " TEXT NOT NULL, " +
                DBSchema.TreatmentsTable.Cols.MORNING + " INTEGER NOT NULL, " +
                DBSchema.TreatmentsTable.Cols.NOON + " INTEGER NOT NULL, " +
                DBSchema.TreatmentsTable.Cols.EVENING + " INTEGER NOT NULL )";
    }

    private String createTablePills() {
        return "Create Table if not exists " +
                DBSchema.PillsTable.NAME + "(" +
                DBSchema.PillsTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBSchema.PillsTable.Cols.NAME + " TEXT NOT NULL, " +
                DBSchema.PillsTable.Cols.DURATION + " INTEGER NOT NULL, " +
                DBSchema.PillsTable.Cols.MORNING + " INTEGER NOT NULL, " +
                DBSchema.PillsTable.Cols.NOON + " INTEGER NOT NULL, " +
                DBSchema.PillsTable.Cols.EVENING + " INTEGER NOT NULL )";
    }


    public List<Treatment> getTreatments() throws ParseException {

        List<Treatment> treatments = new ArrayList<>();

        String query = "SELECT * FROM " + DBSchema.TreatmentsTable.NAME;
        SQLiteDatabase db;
        Cursor cursor = null;

        try {
            //Obtention d'une référence vers la db
            db = getReadableDatabase();
            //Exécution de la requête
            cursor = db.rawQuery(query, null);
        } catch (Exception e) {
            Log.i("Traitement : ", "getTreatments : " + e.getMessage());
        }

        //Parcours du curseur sur toutes les colonnes
        assert cursor != null;
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DBSchema.TreatmentsTable.Cols.ID));
                int pillId = cursor.getInt(cursor.getColumnIndex(DBSchema.TreatmentsTable.Cols.PILLID));
                String beginningString = cursor.getString(cursor.getColumnIndex(DBSchema.TreatmentsTable.Cols.BEGINNING));
                String endString = cursor.getString(cursor.getColumnIndex(DBSchema.TreatmentsTable.Cols.ENDING));
                int morning = cursor.getInt(cursor.getColumnIndex(DBSchema.TreatmentsTable.Cols.MORNING));
                int noon = cursor.getInt(cursor.getColumnIndex(DBSchema.TreatmentsTable.Cols.NOON));
                int evening = cursor.getInt(cursor.getColumnIndex(DBSchema.TreatmentsTable.Cols.EVENING));

                treatments.add(createTreatWithObtainedInformation(id, pillId, beginningString, endString, morning, noon, evening));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return treatments;
    }

    @NonNull
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

    private Calendar convertStringToCalendar(String dateString) throws ParseException {
        //We need to convert the string to a usable calendar
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = sdf.parse(dateString);
        calendar.setTime(date);
        return calendar;
    }


    public void insertTreatment(Treatment currentTreatment) {
        SQLiteDatabase db = getWritableDatabase();

        String query = "INSERT INTO " + DBSchema.TreatmentsTable.NAME + "(" +
                DBSchema.TreatmentsTable.Cols.PILLID + ", " +
                DBSchema.TreatmentsTable.Cols.BEGINNING + ", " +
                DBSchema.TreatmentsTable.Cols.ENDING + ", " +
                DBSchema.TreatmentsTable.Cols.MORNING + ", " +
                DBSchema.TreatmentsTable.Cols.NOON + ", " +
                DBSchema.TreatmentsTable.Cols.EVENING + " ) VALUES (?, ?, ?, ?, ?, ?);";

        String[] args = {
                String.valueOf(currentTreatment.getPill().getId()),
                currentTreatment.getBeginningString(),
                currentTreatment.getEndString(),
                String.valueOf(currentTreatment.getPartsOfDay().contains(PartOfDay.MORNING) ? 1 : 0),
                String.valueOf(currentTreatment.getPartsOfDay().contains(PartOfDay.NOON) ? 1 : 0),
                String.valueOf(currentTreatment.getPartsOfDay().contains(PartOfDay.EVENING) ? 1 : 0)};

        try {
            db.execSQL(query, args);
        } catch (Exception e) {
            Log.i("Traitement : ", "insertTreatment : " + e.getMessage());
        }
    }

    public void updateTreatment(Treatment treatmentToWorkOn) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE " + DBSchema.TreatmentsTable.NAME +
                " SET " + DBSchema.TreatmentsTable.Cols.PILLID + " = ?," +
                DBSchema.TreatmentsTable.Cols.BEGINNING + " = ?," +
                DBSchema.TreatmentsTable.Cols.ENDING + " = ?," +
                DBSchema.TreatmentsTable.Cols.MORNING + " = ?," +
                DBSchema.TreatmentsTable.Cols.NOON + " = ?," +
                DBSchema.TreatmentsTable.Cols.EVENING + " = ?" +
                " WHERE " + DBSchema.TreatmentsTable.Cols.ID + " = ?";

        String[] args = {
                String.valueOf(treatmentToWorkOn.getPill().getId()),
                treatmentToWorkOn.getBeginningString(),
                treatmentToWorkOn.getEndString(),
                String.valueOf(treatmentToWorkOn.getPartsOfDay().contains(PartOfDay.MORNING) ? 1 : 0),
                String.valueOf(treatmentToWorkOn.getPartsOfDay().contains(PartOfDay.NOON) ? 1 : 0),
                String.valueOf(treatmentToWorkOn.getPartsOfDay().contains(PartOfDay.EVENING) ? 1 : 0),
                String.valueOf(treatmentToWorkOn.getId())};
        try {
            db.execSQL(query, args);
        } catch (Exception e) {
            Log.i("Traitement : ", "updateTreatment : " + e.getMessage());
        }
    }


    public void insertPill(Pill pill) {
        SQLiteDatabase db = getWritableDatabase();

        String query = "INSERT INTO " +
                DBSchema.PillsTable.NAME + " ( " +
                DBSchema.PillsTable.Cols.NAME + ", " +
                DBSchema.PillsTable.Cols.DURATION + ", " +
                DBSchema.PillsTable.Cols.MORNING + ", " +
                DBSchema.PillsTable.Cols.NOON + ", " +
                DBSchema.PillsTable.Cols.EVENING + ") VALUES (?, ?, ?, ?, ?)";

        String[] args = {
                pill.getName(),
                String.valueOf(pill.getDuration()),
                String.valueOf(pill.getPartsOfDay().contains(PartOfDay.MORNING) ? 1 : 0),
                String.valueOf(pill.getPartsOfDay().contains(PartOfDay.NOON) ? 1 : 0),
                String.valueOf(pill.getPartsOfDay().contains(PartOfDay.EVENING) ? 1 : 0)};

        db.execSQL(query, args);
    }

    public void updatePill(Pill pill) {
        SQLiteDatabase db = getWritableDatabase();

        String query = "UPDATE " +
                DBSchema.PillsTable.NAME + " SET " +
                DBSchema.PillsTable.Cols.NAME + " = ?, " +
                DBSchema.PillsTable.Cols.DURATION + " = ?, " +
                DBSchema.PillsTable.Cols.MORNING + " = ?, " +
                DBSchema.PillsTable.Cols.NOON + " = ?, " +
                DBSchema.PillsTable.Cols.EVENING + " = ? " +
                "WHERE " + DBSchema.PillsTable.Cols.ID + " = ?";

        String[] args = {
                pill.getName(),
                String.valueOf(pill.getDuration()),
                String.valueOf(pill.getPartsOfDay().contains(PartOfDay.MORNING) ? 1 : 0),
                String.valueOf(pill.getPartsOfDay().contains(PartOfDay.NOON) ? 1 : 0),
                String.valueOf(pill.getPartsOfDay().contains(PartOfDay.EVENING) ? 1 : 0),
                String.valueOf(pill.getId())};
        try {
            db.execSQL(query, args);
        } catch (Exception e) {
            Log.i("ERROR", "updatePill: " + e.getMessage());
        }
    }


    public List<Pill> getPills() {
        String query = "SELECT * FROM " + DBSchema.PillsTable.NAME;
        SQLiteDatabase db;
        Cursor cursor = null;

        try {
            //Obtention d'une référence vers la db
            db = getReadableDatabase();
            //Exécution de la requête
            cursor = db.rawQuery(query, null);
        } catch (Exception e) {
            Log.i("Traitement : ", "getPills : " + e.getMessage());
        }

        return extractPills(cursor);
    }

    private static ArrayList extractPills(Cursor cursor) {
        List<Pill> pills = new ArrayList<>();
        //Parcours du curseur sur toutes les colonnes
        assert cursor != null;
        if (cursor.moveToFirst()) {
            do {
                int pillId = cursor.getInt(cursor.getColumnIndex(DBSchema.PillsTable.Cols.ID));
                String pillName = cursor.getString(cursor.getColumnIndex(DBSchema.PillsTable.Cols.NAME));
                int duration = cursor.getInt(cursor.getColumnIndex(DBSchema.PillsTable.Cols.DURATION));
                int morning = cursor.getInt(cursor.getColumnIndex(DBSchema.PillsTable.Cols.MORNING));
                int noon = cursor.getInt(cursor.getColumnIndex(DBSchema.PillsTable.Cols.NOON));
                int evening = cursor.getInt(cursor.getColumnIndex(DBSchema.PillsTable.Cols.EVENING));

                List<PartOfDay> partsOfDay = new ArrayList<>();

                if (morning == 1)
                    partsOfDay.add(PartOfDay.MORNING);
                if (noon == 1)
                    partsOfDay.add(PartOfDay.NOON);
                if (evening == 1)
                    partsOfDay.add(PartOfDay.EVENING);

                Pill pill = new Pill(pillName, duration, partsOfDay);

                pill.setId(pillId);

                pills.add(pill);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return (ArrayList) pills;
    }

    public Pill getSpecificPill(int id) {
        // Obtention d'une référence vers la db
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM Pills WHERE Id = ?";
        String[] args = {String.valueOf(id)};

        // Exécution de la requête avec des paramètres
        Cursor cursor = db.rawQuery(query, args);

        return extractSpecificPill(cursor);
    }

    private static Pill extractSpecificPill(Cursor cursor) {
        Pill pill = null;
        //Parcours du curseur sur toutes les colonnes
        if (cursor.moveToFirst()) {
            do {
                int idPill = cursor.getInt(cursor.getColumnIndex(DBSchema.PillsTable.Cols.ID));
                String name = cursor.getString(cursor.getColumnIndex(DBSchema.PillsTable.Cols.NAME));
                int duration = cursor.getInt(cursor.getColumnIndex(DBSchema.PillsTable.Cols.DURATION));
                int morning = cursor.getInt(cursor.getColumnIndex(DBSchema.PillsTable.Cols.MORNING));
                int noon = cursor.getInt(cursor.getColumnIndex(DBSchema.PillsTable.Cols.NOON));
                int evening = cursor.getInt(cursor.getColumnIndex(DBSchema.PillsTable.Cols.EVENING));

                List<PartOfDay> partsOfDay = new ArrayList<>();

                if (morning == 1)
                    partsOfDay.add(PartOfDay.MORNING);
                if (noon == 1)
                    partsOfDay.add(PartOfDay.NOON);
                if (evening == 1)
                    partsOfDay.add(PartOfDay.EVENING);

                pill = new Pill(name, duration, partsOfDay);
                pill.setId(idPill);
            } while (cursor.moveToNext());
        }
        return pill;
    }


}

