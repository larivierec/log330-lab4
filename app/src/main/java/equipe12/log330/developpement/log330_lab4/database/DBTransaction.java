package equipe12.log330.developpement.log330_lab4.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import equipe12.log330.developpement.log330_lab4.model.GPS;
import equipe12.log330.developpement.log330_lab4.model.User;
import equipe12.log330.developpement.log330_lab4.model.Zone;

/**
 * Created by serge on 2015-10-16.
 */
public class DBTransaction {
    private DbOpenHelper dbOpenHelper;

    public DBTransaction(Context context) {
        dbOpenHelper = new DbOpenHelper(context);
    }

    public User isValidUser(String user, String password) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor c = db.query(FeedReaderContract.UserFeedEntry.USER_TABLE_NAME,
                new String[] {FeedReaderContract.UserFeedEntry.COLUMN_NAME_ID,
                FeedReaderContract.UserFeedEntry.COLUMN_NAME_NAME,
                FeedReaderContract.UserFeedEntry.COLUMN_NAME_PASSWORD},
                FeedReaderContract.UserFeedEntry.COLUMN_NAME_NAME + " = ? and " + FeedReaderContract.UserFeedEntry.COLUMN_NAME_PASSWORD + " = ?",
                new String[] {user, password},
                null,
                null,
                null);
        if(c.moveToFirst()) {
         return new User(c.getInt(c.getColumnIndex(FeedReaderContract.UserFeedEntry.COLUMN_NAME_ID)),
                 c.getString(c.getColumnIndex(FeedReaderContract.UserFeedEntry.COLUMN_NAME_NAME)));
        }
        return null;
    }

    public static List<Zone> getZones(GPS gps) {
        return new ArrayList<Zone>();
    }

    public static List<Zone> addZone(GPS gps, Zone zone) {
        return getZones(gps);
    }

    public static List<Zone> modifyZone(GPS gps, Zone zone) {
        return getZones(gps);
    }

    public static List<Zone> deleteZone(GPS gps, Zone zone) {
        return getZones(gps);
    }

    public static List<GPS> getGps(User user) {
        return new ArrayList<GPS>();
    }

    public static List<GPS> addGps(User user, GPS gps) {
        return getGps(user);
    }

    public static List<GPS> modifyGps(User user, GPS gps) {
        return getGps(user);
    }

    public static List<GPS> deleteGps(User user,GPS gps) {
        return getGps(user);
    }
}
