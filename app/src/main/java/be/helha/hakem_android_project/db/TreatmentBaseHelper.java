package be.helha.hakem_android_project.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import be.helha.hakem_android_project.models.Pill;
import be.helha.hakem_android_project.models.Treatment;

public class TreatmentBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1; // version de la base de donnée, va servir à savoir s'il faut mettre à jour le code
    private static final String DATABASE_NAME = "db_onlypills.db";
    public TreatmentBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //We'll try to create the database
        sqLiteDatabase.execSQL(
                "CREATE TABLE \"Treatment\" (\n" +
                "\t\"Id\"\tINTEGER NOT NULL,\n" +
                "\t\"PillId\"\tINTEGER NOT NULL,\n" +
                "\t\"Begining\"\tTEXT NOT NULL,\n" +
                "\t\"End\"\tTEXT NOT NULL,\n" +
                "\t\"Morning\"\tINTEGER NOT NULL,\n" +
                "\t\"Noon\"\tINTEGER NOT NULL,\n" +
                "\t\"Evening\"\tINTEGER NOT NULL,\n" +
                "\tPRIMARY KEY(\"Id\")" +
                ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public List<Treatment> getTreatments() {
        //TODO SQL-Request for getting all treatments
        return null;
    }

}

