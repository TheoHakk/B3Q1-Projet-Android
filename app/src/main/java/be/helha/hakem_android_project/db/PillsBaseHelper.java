package be.helha.hakem_android_project.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PillsBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1; // version de la base de donnée, va servir à savoir s'il faut mettre à jour le code
    private static final String DATABASE_NAME = "db_onlypills.db";
    public PillsBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //We will try to create the database
        sqLiteDatabase.execSQL("CREATE TABLE \"Pills\" (\n" +
                "\t\"Id\"\tINTEGER,\n" +
                "\t\"Name\"\tTEXT NOT NULL,\n" +
                "\t\"Duration\"\tTEXT NOT NULL,\n" +
                "\t\"Morning\"\tINTEGER NOT NULL,\n" +
                "\t\"Noon\"\tINTEGER NOT NULL,\n" +
                "\t\"Evening\"\tINTEGER NOT NULL,\n" +
                "\tPRIMARY KEY(\"Id\"));"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Define what i've to do when i upgrade the database
    }

}
