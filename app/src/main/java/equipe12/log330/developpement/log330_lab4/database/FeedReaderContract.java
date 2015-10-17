package equipe12.log330.developpement.log330_lab4.database;

import android.provider.BaseColumns;

/**
 * Created by serge on 2015-10-16.
 */
final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static abstract class UserFeedEntry implements BaseColumns {
        public static final String USER_TABLE_NAME = "User";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_PASSWORD = "password";
    }

    public static abstract class GPSFeedEntry implements BaseColumns {
        public static final String GPS_TABLE_NAME = "GPS";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_IMAGE = "image";
        public static final String COLUMN_NAME_ID_USER = "idUser";
    }

    public static abstract class GPSPositionFeedEntry implements BaseColumns {
        public static final String GPS_POSITION_TABLE_NAME = "GPSPosition";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_LAT = "lat";
        public static final String COLUMN_NAME_LON = "lon";
        public static final String COLUMN_NAME_CREATED_TIME = "createdTime";
        public static final String COLUMN_NAME_ID_GPS = "idGps";
    }

    public static abstract class ZoneFeedEntry implements BaseColumns {
        public static final String ZONE_TABLE_NAME = "Zone";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_RADIUS = "radius";
        public static final String COLUMN_NAME_ACTIVE = "active";
        public static final String COLUMN_NAME_ID_GPS = "idGps";
    }

    public static abstract class ZonePointFeedEntry implements BaseColumns {
        public static final String ZONE_POINT_TABLE_NAME = "ZonePoint";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_LAT = "lat";
        public static final String COLUMN_NAME_LON = "lon";
        public static final String COLUMN_NAME_ID_ZONE = "idZone";
    }
}
