package equipe12.log330.developpement.log330_lab4.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by serge on 2015-10-16.
 */
class DbOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PetTracker";

    private static final String USER_TABLE_CREATE =
            "CREATE TABLE " + FeedReaderContract.UserFeedEntry.USER_TABLE_NAME + " ("+
                    FeedReaderContract.UserFeedEntry.COLUMN_NAME_ID +" integer PRIMARY KEY AUTOINCREMENT, " +
                    FeedReaderContract.UserFeedEntry.COLUMN_NAME_NAME + " TEXT UNIQUE, " +
                    FeedReaderContract.UserFeedEntry.COLUMN_NAME_PASSWORD + " TEXT)";
    private static final String SQL_DELETE_USER =
            "DROP TABLE IF EXISTS " + FeedReaderContract.UserFeedEntry.USER_TABLE_NAME;

    private static final String GPS_TABLE_CREATE =
            "CREATE TABLE " + FeedReaderContract.GPSFeedEntry.GPS_TABLE_NAME +
                    " (" + FeedReaderContract.GPSFeedEntry.COLUMN_NAME_ID + " TEXT PRIMARY KEY," +
                    FeedReaderContract.GPSFeedEntry.COLUMN_NAME_NAME + " TEXT," +
                    FeedReaderContract.GPSFeedEntry.COLUMN_NAME_IMAGE + " blob, " +
                    FeedReaderContract.GPSFeedEntry.COLUMN_NAME_ID_USER + " integer, " +
                    "FOREIGN KEY(" + FeedReaderContract.GPSFeedEntry.COLUMN_NAME_ID_USER + ") " +
                    "REFERENCES user(" + FeedReaderContract.UserFeedEntry.COLUMN_NAME_ID + ") ON DELETE CASCADE)";
    private static final String SQL_DELETE_GPS =
            "DROP TABLE IF EXISTS " + FeedReaderContract.GPSFeedEntry.GPS_TABLE_NAME;


    private static final String GPS_POSITION_TABLE_CREATE =
            "CREATE TABLE " + FeedReaderContract.GPSPositionFeedEntry.GPS_POSITION_TABLE_NAME +
                    " (" + FeedReaderContract.GPSPositionFeedEntry.COLUMN_NAME_ID + " integer PRIMARY KEY AUTOINCREMENT, " +
                    FeedReaderContract.GPSPositionFeedEntry.COLUMN_NAME_LAT + " integer, " +
                    FeedReaderContract.GPSPositionFeedEntry.COLUMN_NAME_LON + " integer, " +
                    FeedReaderContract.GPSPositionFeedEntry.COLUMN_NAME_CREATED_TIME + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    FeedReaderContract.GPSPositionFeedEntry.COLUMN_NAME_ID_GPS + " integer, " +
                    "FOREIGN KEY(" + FeedReaderContract.GPSPositionFeedEntry.COLUMN_NAME_ID_GPS + ") " +
                    "REFERENCES GPS(" + FeedReaderContract.GPSFeedEntry.COLUMN_NAME_ID + ") ON DELETE CASCADE)";
    private static final String SQL_DELETE_GPS_POSITION =
            "DROP TABLE IF EXISTS " + FeedReaderContract.GPSPositionFeedEntry.GPS_POSITION_TABLE_NAME;

    private static final String ZONE_TABLE_CREATE =
            "CREATE TABLE " + FeedReaderContract.ZoneFeedEntry.ZONE_TABLE_NAME +
                    " (" + FeedReaderContract.ZoneFeedEntry.COLUMN_NAME_ID + " integer PRIMARY KEY AUTOINCREMENT, " +
                    FeedReaderContract.ZoneFeedEntry.COLUMN_NAME_NAME + " TEXT, " +
                    FeedReaderContract.ZoneFeedEntry.COLUMN_NAME_RADIUS + " integer DEFAULT -1, " +
                    FeedReaderContract.ZoneFeedEntry.COLUMN_NAME_ACTIVE + " NUMERIC, " +
                    FeedReaderContract.ZoneFeedEntry.COLUMN_NAME_ID_GPS + " TEXT, " +
                    "FOREIGN KEY(" + FeedReaderContract.ZoneFeedEntry.COLUMN_NAME_ID_GPS + ") " +
                    "REFERENCES GPS(" + FeedReaderContract.GPSFeedEntry.COLUMN_NAME_ID  + ") ON DELETE CASCADE)";
    private static final String SQL_DELETE_ZONE =
            "DROP TABLE IF EXISTS " + FeedReaderContract.ZoneFeedEntry.ZONE_TABLE_NAME;

    private static final String ZONE_POINT_TABLE_CREATE =
            "CREATE TABLE " + FeedReaderContract.ZonePointFeedEntry.ZONE_POINT_TABLE_NAME +
                    " (" + FeedReaderContract.ZonePointFeedEntry.COLUMN_NAME_ID + " integer PRIMARY KEY AUTOINCREMENT, " +
                    FeedReaderContract.ZonePointFeedEntry.COLUMN_NAME_LAT + " integer, " +
                    FeedReaderContract.ZonePointFeedEntry.COLUMN_NAME_LON + " integer, " +
                    FeedReaderContract.ZonePointFeedEntry.COLUMN_NAME_ID_ZONE + " integer, " +
                    "FOREIGN KEY(" + FeedReaderContract.ZonePointFeedEntry.COLUMN_NAME_ID_ZONE + ") " +
                    "REFERENCES zone(" + FeedReaderContract.ZoneFeedEntry.COLUMN_NAME_ID + ") ON DELETE CASCADE)";
    private static final String SQL_DELETE_ZONE_POINT =
            "DROP TABLE IF EXISTS " + FeedReaderContract.ZonePointFeedEntry.ZONE_POINT_TABLE_NAME;

    public DbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_TABLE_CREATE);
        db.execSQL(GPS_TABLE_CREATE);
        db.execSQL(GPS_POSITION_TABLE_CREATE);
        db.execSQL(ZONE_TABLE_CREATE);
        db.execSQL(ZONE_POINT_TABLE_CREATE);

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.UserFeedEntry.COLUMN_NAME_NAME, "test");
        values.put(FeedReaderContract.UserFeedEntry.COLUMN_NAME_PASSWORD, "test");

        db.insert(
                FeedReaderContract.UserFeedEntry.USER_TABLE_NAME,
                null,
                values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ZONE_POINT);
        db.execSQL(SQL_DELETE_ZONE);
        db.execSQL(SQL_DELETE_GPS_POSITION);
        db.execSQL(SQL_DELETE_GPS);
        db.execSQL(SQL_DELETE_USER);
        onCreate(db);
    }
}
