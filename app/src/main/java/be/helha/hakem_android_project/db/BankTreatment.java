package be.helha.hakem_android_project.db;

import android.database.sqlite.SQLiteDatabase;

import be.helha.hakem_android_project.models.PartOfDay;
import be.helha.hakem_android_project.models.Treatment;

/**
 * The BankTreatment class is responsible for managing interactions with the SQLite database
 * to perform operations related to the Treatment entity.
 * Only for the update and insert of the treatment.
 */
public class BankTreatment {

    private SQLiteDatabase mDatabase;

    /**
     * Constructs a new instance of the BankTreatment class.
     *
     * @param database The SQLiteDatabase instance to be used for database operations.
     */
    public BankTreatment(SQLiteDatabase database) {
        this.mDatabase = database;
    }

    /**
     * Inserts a new treatment record into the database.
     *
     * @param currentTreatment The Treatment object representing the data to be inserted.
     */
    public void insertTreatment(Treatment currentTreatment) {
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
        mDatabase.execSQL(query, args);
    }

    /**
     * Updates an existing treatment record in the database.
     *
     * @param treatmentToWorkOn The Treatment object representing the updated data.
     */
    public void updateTreatment(Treatment treatmentToWorkOn) {
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
        mDatabase.execSQL(query, args);
    }
}
