package be.helha.hakem_android_project.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import be.helha.hakem_android_project.models.PartOfDay;
import be.helha.hakem_android_project.models.Pill;

public class BankPill {

    private SQLiteDatabase database;
    public BankPill(SQLiteDatabase database) {
        this.database = database;
    }

    public void insertPill(Pill pill) {
        SQLiteDatabase db = database;

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
        SQLiteDatabase db = database;

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
}
