package be.helha.hakem_android_project.db;

import android.annotation.SuppressLint;
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
        sqLiteDatabase.execSQL(createTablePillsString());
        sqLiteDatabase.execSQL(createTableTreatmentString());
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //We will do here the update of the database
    }


    private String createTableTreatmentString() {
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
    private String createTablePillsString() {
        return "Create Table if not exists " +
                DBSchema.PillsTable.NAME + "(" +
                DBSchema.PillsTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBSchema.PillsTable.Cols.NAME + " TEXT NOT NULL, " +
                DBSchema.PillsTable.Cols.DURATION + " INTEGER NOT NULL, " +
                DBSchema.PillsTable.Cols.MORNING + " INTEGER NOT NULL, " +
                DBSchema.PillsTable.Cols.NOON + " INTEGER NOT NULL, " +
                DBSchema.PillsTable.Cols.EVENING + " INTEGER NOT NULL )";
    }




}

