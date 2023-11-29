package be.helha.hakem_android_project.db.bank;

import android.database.sqlite.SQLiteDatabase;

import be.helha.hakem_android_project.db.schema.DBSchema;
import be.helha.hakem_android_project.models.PartOfDay;
import be.helha.hakem_android_project.models.Pill;

/**
 * The BankPill class is responsible for interacting with the SQLite database
 * to perform operations related to the Pill entity.
 * Only for the update and insert of the pill.
 */
public class BankPill {
    private SQLiteDatabase mDatabase;

    /**
     * Constructs a new instance of the BankPill class.
     *
     * @param database The SQLiteDatabase instance to be used for database operations.
     */
    public BankPill(SQLiteDatabase database) {
        this.mDatabase = database;
    }

    /**
     * Inserts a new pill record into the database.
     *
     * @param pill The Pill object representing the data to be inserted.
     */
    public void insertPill(Pill pill) {
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
        mDatabase.execSQL(query, args);
    }

    /**
     * Updates an existing pill record in the database.
     *
     * @param pill The Pill object representing the updated data.
     */
    public void updatePill(Pill pill) {
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
        mDatabase.execSQL(query, args);
    }
}
