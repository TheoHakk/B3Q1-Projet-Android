package be.helha.hakem_android_project.db.baseHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import be.helha.hakem_android_project.db.schema.DBSchema;

/**
 * The ProjectBaseHelper class is a helper class for managing the creation and upgrading
 * of the SQLite database used in the project.
 */
public class ProjectBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1; // version of the database, used for potential updates
    private static final String DATABASE_NAME = "db_onlypills.db";

    /**
     * Constructs a new instance of the ProjectBaseHelper class.
     *
     * @param context The context in which the helper is created.
     */
    public ProjectBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     * Called when the database needs to be created.
     *
     * @param sqLiteDatabase The SQLiteDatabase instance.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createTablePillsString());
        sqLiteDatabase.execSQL(createTableTreatmentString());
    }

    /**
     * Called when the database needs to be upgraded.
     *
     * @param sqLiteDatabase The SQLiteDatabase instance.
     * @param oldVersion     The old version of the database.
     * @param newVersion     The new version of the database.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Perform database upgrade if necessary
        // This method is called when the version number is increased
    }

    /**
     * Creates the SQL string for creating the Treatments table.
     *
     * @return The SQL string for creating the Treatments table.
     */
    private String createTableTreatmentString() {
        return "CREATE TABLE IF NOT EXISTS " +
                DBSchema.TreatmentsTable.NAME + "(" +
                DBSchema.TreatmentsTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBSchema.TreatmentsTable.Cols.PILLID + " INTEGER NOT NULL, " +
                DBSchema.TreatmentsTable.Cols.BEGINNING + " TEXT NOT NULL, " +
                DBSchema.TreatmentsTable.Cols.ENDING + " TEXT NOT NULL, " +
                DBSchema.TreatmentsTable.Cols.MORNING + " INTEGER NOT NULL, " +
                DBSchema.TreatmentsTable.Cols.NOON + " INTEGER NOT NULL, " +
                DBSchema.TreatmentsTable.Cols.EVENING + " INTEGER NOT NULL )";
    }

    /**
     * Creates the SQL string for creating the Pills table.
     *
     * @return The SQL string for creating the Pills table.
     */
    private String createTablePillsString() {
        return "CREATE TABLE IF NOT EXISTS " +
                DBSchema.PillsTable.NAME + "(" +
                DBSchema.PillsTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBSchema.PillsTable.Cols.NAME + " TEXT NOT NULL, " +
                DBSchema.PillsTable.Cols.DURATION + " INTEGER NOT NULL, " +
                DBSchema.PillsTable.Cols.MORNING + " INTEGER NOT NULL, " +
                DBSchema.PillsTable.Cols.NOON + " INTEGER NOT NULL, " +
                DBSchema.PillsTable.Cols.EVENING + " INTEGER NOT NULL )";
    }
}
