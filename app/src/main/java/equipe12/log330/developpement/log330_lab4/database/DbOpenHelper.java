package equipe12.log330.developpement.log330_lab4.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by serge on 2015-10-16.
 */
public class DbOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PetTracker";
    private static final String GPS_TABLE_NAME = "GPS";
    private static final String GPS_TABLE_CREATE =
            "CREATE TABLE " + GPS_TABLE_NAME + " (id TEXT PRIMARY KEY ASC, name TEXT, image blob)";
    private static final String ZONE_TABLE_NAME = "GPS";
    private static final String ZONE_TABLE_CREATE =
            "CREATE TABLE " + ZONE_TABLE_NAME + " (id integer PRIMARY KEY ASC, name TEXT, image blob)";
    DbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(GPS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
