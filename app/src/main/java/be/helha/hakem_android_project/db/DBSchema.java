package be.helha.hakem_android_project.db;

public abstract class DBSchema {

    public static final class PillsTable {
        public static final String NAME = "Pills";
        public static final class Cols {
            public static final String ID = "Id";
            public static final String NAME = "Name";
            public static final String DURATION = "Duration";
            public static final String MORNING = "Morning";
            public static final String NOON = "Noon";
            public static final String EVENING = "Evening";
        }
    }

    public static final class TreatmentsTable {
        public static final String NAME = "Treatment";
        public static final class Cols {
            public static final String ID = "Id";
            public static final String PILLID = "PillId";
            public static final String BEGINNING = "Beginning";
            public static final String END = "Ending";
            public static final String MORNING = "Morning";
            public static final String NOON = "Noon";
            public static final String EVENING = "Evening";

        }
    }

}

