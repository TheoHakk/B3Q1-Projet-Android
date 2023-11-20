package be.helha.hakem_android_project.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import be.helha.hakem_android_project.models.PartOfDay;
import be.helha.hakem_android_project.models.Pill;
import be.helha.hakem_android_project.models.Treatment;
import be.helha.hakem_android_project.models.TreatmentDbSchema;

public class TreatmentBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1; // version de la base de donnée, va servir à savoir s'il faut mettre à jour le code
    private static final String DATABASE_NAME = "db_onlypills.db";

    public TreatmentBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createTable());
    }

    private String dropTable() {
        return "DROP TABLE IF EXISTS Treatment";
    }

    private String createTable() {
        return "CREATE TABLE IF NOT EXISTS \"Treatment\" (\n" +
                "\t\"Id\"\tINTEGER NOT NULL,\n" +
                "\t\"PillId\"\tINTEGER NOT NULL,\n" +
                "\t\"Begining\"\tTEXT NOT NULL,\n" +
                "\t\"End\"\tTEXT NOT NULL,\n" +
                "\t\"Morning\"\tINTEGER NOT NULL,\n" +
                "\t\"Noon\"\tINTEGER NOT NULL,\n" +
                "\t\"Evening\"\tINTEGER NOT NULL,\n" +
                "\tPRIMARY KEY(\"Id\")" +
                ");";
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //We will drop the table and create it again
        sqLiteDatabase.execSQL(dropTable());
        onCreate(sqLiteDatabase);
    }

    public List<Treatment> getTreatments(Context context) throws ParseException {
        Log.i("Traitement : ", "getTreatments");

        List<Treatment> treatments = new ArrayList<>();

        String query = "SELECT * FROM Treatment";
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
                int id = cursor.getInt(cursor.getColumnIndex(TreatmentDbSchema.Cols.ID));
                int pillId = cursor.getInt(cursor.getColumnIndex(TreatmentDbSchema.Cols.PILLID));
                String beginningString = cursor.getString(cursor.getColumnIndex(TreatmentDbSchema.Cols.BEGINNING));
                String endString = cursor.getString(cursor.getColumnIndex(TreatmentDbSchema.Cols.END));
                int morning = cursor.getInt(cursor.getColumnIndex(TreatmentDbSchema.Cols.MORNING));
                int noon = cursor.getInt(cursor.getColumnIndex(TreatmentDbSchema.Cols.NOON));
                int evening = cursor.getInt(cursor.getColumnIndex(TreatmentDbSchema.Cols.EVENING));

                Pill pill = new PillsBaseHelper(context).getSpecificPill(pillId);
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

                treatments.add(treatment);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return treatments;
    }

    private Calendar convertStringToCalendar(String dateString) throws ParseException {
        //We need to convert the string to a usable calendar
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Log.i("yaourt", dateString);
        Date date = sdf.parse(dateString);
        calendar.setTime(date);
        return calendar;
    }


    public void insertTreatment(Treatment currentTreatment) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "INSERT INTO Treatment (PillId, Beginning, End, Morning, Noon, Evening) VALUES (?, ?, ?, ?, ?, ?)";
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
        String query = "UPDATE Treatment SET PillId = ?, Beginning = ?, End = ?, Morning = ?, Noon = ?, Evening = ? WHERE Id = ?";
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
}

