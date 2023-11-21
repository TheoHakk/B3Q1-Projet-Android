package be.helha.hakem_android_project.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import be.helha.hakem_android_project.models.PartOfDay;
import be.helha.hakem_android_project.models.Treatment;

public class BankTreatment {

    SQLiteDatabase database;
    public BankTreatment(SQLiteDatabase database) {
        this.database = database;
    }


    public void insertTreatment(Treatment currentTreatment) {
        SQLiteDatabase db = database;

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
        SQLiteDatabase db = database;
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
}
