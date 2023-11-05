package be.helha.hakem_android_project.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import be.helha.hakem_android_project.models.PartOfDay;
import be.helha.hakem_android_project.models.Pill;
import be.helha.hakem_android_project.models.PillsDbSchema;
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

    public List<Treatment> getTreatments(Context context) {
        List<Treatment> treatments = new ArrayList<>();

        //Obtention d'une référence vers la db
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM Treatment";
        //Exécution de la requête
        Cursor cursor = db.rawQuery(query, null);

        //Parcours du curseur sur toutes les colonnes
        if (cursor.moveToFirst()) {
            do {
                int pillId = cursor.getInt(cursor.getColumnIndex(TreatmentDbSchema.Cols.PILLID));
                String beginning = cursor.getString(cursor.getColumnIndex(TreatmentDbSchema.Cols.BEGINNING));
                String end = cursor.getString(cursor.getColumnIndex(TreatmentDbSchema.Cols.END));
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

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                Date beginninOfTreatment = new Date();
                Date endOfTreament = new Date();

                try {
                    beginninOfTreatment = dateFormat.parse(beginning);
                    endOfTreament = dateFormat.parse(end);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Treatment treatment = new Treatment(pill, partsOfDay, beginninOfTreatment, endOfTreament);

                treatments.add(treatment);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return treatments;
    }

    public void addTreatment(Treatment treatment) {
        //Obtention d'une référence vers la db
        SQLiteDatabase db = getWritableDatabase();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String beginning = dateFormat.format(treatment.getBeginning());
        String end = dateFormat.format(treatment.getEnd());

        int morning = 0;
        int noon = 0;
        int evening = 0;

        for (PartOfDay partOfDay : treatment.getPartOfDays()) {
            switch (partOfDay) {
                case MORNING:
                    morning = 1;
                    break;
                case NOON:
                    noon = 1;
                    break;
                case EVENING:
                    evening = 1;
                    break;
            }
        }

        String query = "INSERT INTO Treatment (PillId, Begining, End, Morning, Noon, Evening) VALUES (" +
                treatment.getPill().getId() + ", '" +
                beginning + "', '" +
                end + "', " +
                morning + ", " +
                noon + ", " +
                evening + ");";


        try {
            db.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

