package be.helha.hakem_android_project.db.schema;

/**
 * The DBSchema class defines the database schema including table names and column names
 * for the Pills and Treatments tables.
 */
public abstract class DBSchema {

    /**
     * Defines the schema for the Pills table in the database.
     */
    public static final class PillsTable {
        public static final String NAME = "Pills";

        /**
         * Defines the columns for the Pills table.
         */
        public static final class Cols {
            public static final String ID = "Id";
            public static final String NAME = "Name";
            public static final String DURATION = "Duration";
            public static final String MORNING = "Morning";
            public static final String NOON = "Noon";
            public static final String EVENING = "Evening";
        }
    }

    /**
     * Defines the schema for the Treatments table in the database.
     */
    public static final class TreatmentsTable {
        public static final String NAME = "Treatment";

        /**
         * Defines the columns for the Treatments table.
         */
        public static final class Cols {
            public static final String ID = "Id";
            public static final String PILLID = "PillId";
            public static final String BEGINNING = "Beginning";
            public static final String ENDING = "Ending";
            public static final String MORNING = "Morning";
            public static final String NOON = "Noon";
            public static final String EVENING = "Evening";
        }
    }
}
