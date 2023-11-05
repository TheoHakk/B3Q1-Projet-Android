package be.helha.hakem_android_project.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import be.helha.hakem_android_project.models.PartOfDay;
import be.helha.hakem_android_project.models.Pill;
import be.helha.hakem_android_project.models.PillsDbSchema;

public class PillsBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1; // version de la base de donnée, va servir à savoir s'il faut mettre à jour le code
    private static final String DATABASE_NAME = "db_onlypills.db";
    public PillsBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //We will try to create the database
        sqLiteDatabase.execSQL(createTable());
    }

    public String createTable() {
        return "CREATE TABLE IF NOT EXISTS \"Pills\" (\n" +
                "\t\"Id\"\tINTEGER,\n" +
                "\t\"Name\"\tTEXT NOT NULL,\n" +
                "\t\"Duration\"\tINTEGER NOT NULL,\n" +
                "\t\"Morning\"\tINTEGER NOT NULL,\n" +
                "\t\"Noon\"\tINTEGER NOT NULL,\n" +
                "\t\"Evening\"\tINTEGER NOT NULL,\n" +
                "\tPRIMARY KEY(\"Id\"));";
    }

    public String dropTable() {
        return "DROP TABLE IF EXISTS Pills";
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //We will drop the table and create it again
        sqLiteDatabase.execSQL(dropTable());
        onCreate(sqLiteDatabase);
    }


    public Pill getSpecificPill(int id){
        Pill pill = null;

        // Obtention d'une référence vers la db
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM Pills WHERE Id = ?";
        String[] args = {String.valueOf(id)};

        // Exécution de la requête avec des paramètres
        Cursor cursor = db.rawQuery(query, args);


        //Parcours du curseur sur toutes les colonnes
        if (cursor.moveToFirst()) {
            do {
                int idPill = cursor.getInt(cursor.getColumnIndex(PillsDbSchema.Cols.ID));
                String name = cursor.getString(cursor.getColumnIndex(PillsDbSchema.Cols.NAME));
                int duration = cursor.getInt(cursor.getColumnIndex(PillsDbSchema.Cols.DURATION));
                int morning = cursor.getInt(cursor.getColumnIndex(PillsDbSchema.Cols.MORNING));
                int noon = cursor.getInt(cursor.getColumnIndex(PillsDbSchema.Cols.NOON));
                int evening = cursor.getInt(cursor.getColumnIndex(PillsDbSchema.Cols.EVENING));

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
