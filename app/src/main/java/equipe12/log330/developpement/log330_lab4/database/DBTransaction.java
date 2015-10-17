package equipe12.log330.developpement.log330_lab4.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import equipe12.log330.developpement.log330_lab4.model.GPS;
import equipe12.log330.developpement.log330_lab4.model.User;
import equipe12.log330.developpement.log330_lab4.model.Zone;
import equipe12.log330.developpement.log330_lab4.model.ZonePoints;
import equipe12.log330.developpement.log330_lab4.model.ZoneRadius;

/**
 * Created by serge on 2015-10-16.
 */
class DBTransaction {
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

    public User addUser(String user, String password) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.UserFeedEntry.COLUMN_NAME_NAME, user);
        values.put(FeedReaderContract.UserFeedEntry.COLUMN_NAME_PASSWORD, password);
        long newRowId = -1;
        try {
            newRowId = db.insertOrThrow(
                    FeedReaderContract.UserFeedEntry.USER_TABLE_NAME,
                    null,
                    values);
        } catch (Exception e) {
        }
        if(newRowId != -1) {
            return new User(newRowId, user);
        }
        return null;
    }

    public List<Zone> getZones(GPS gps) {
        List<Zone> zones = new ArrayList<Zone>();
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor c = db.query(FeedReaderContract.ZoneFeedEntry.ZONE_TABLE_NAME,
                new String[] {FeedReaderContract.ZoneFeedEntry.COLUMN_NAME_ID,
                        FeedReaderContract.ZoneFeedEntry.COLUMN_NAME_NAME,
                        FeedReaderContract.ZoneFeedEntry.COLUMN_NAME_ACTIVE,
                        FeedReaderContract.ZoneFeedEntry.COLUMN_NAME_RADIUS},
                FeedReaderContract.ZoneFeedEntry.COLUMN_NAME_ID_GPS + " = ?",
                new String[] {String.valueOf(gps.getGPSID())},
                null,
                null,
                null);

        c.moveToFirst();
        while (!c.isAfterLast()) {
            int zoneRadius = c.getInt(c.getColumnIndex(FeedReaderContract.ZoneFeedEntry.COLUMN_NAME_RADIUS));
            int zoneId = c.getInt(c.getColumnIndex(FeedReaderContract.ZoneFeedEntry.COLUMN_NAME_ID));
            String zoneName = c.getString(c.getColumnIndex(FeedReaderContract.ZoneFeedEntry.COLUMN_NAME_NAME));
            boolean zoneActive = c.getInt(c.getColumnIndex(FeedReaderContract.ZoneFeedEntry.COLUMN_NAME_ACTIVE)) == 0 ? false : true;

            Cursor c2 = db.query(FeedReaderContract.ZonePointFeedEntry.ZONE_POINT_TABLE_NAME,
                    new String[]{FeedReaderContract.ZonePointFeedEntry.COLUMN_NAME_ID,
                            FeedReaderContract.ZonePointFeedEntry.COLUMN_NAME_LAT,
                            FeedReaderContract.ZonePointFeedEntry.COLUMN_NAME_LON},
                    FeedReaderContract.ZonePointFeedEntry.COLUMN_NAME_ID_ZONE + " = ?",
                    new String[]{String.valueOf(zoneId)},
                    null,
                    null,
                    null);
            if(c2.moveToFirst()) {
                if (zoneRadius == -1) {
                    while(!c2.isAfterLast()) {
                        LinkedList<LatLng> lst = new LinkedList<LatLng>();
                        lst.add(new LatLng(c2.getDouble(c.getColumnIndex(FeedReaderContract.ZonePointFeedEntry.COLUMN_NAME_LAT)),
                                c2.getDouble(c.getColumnIndex(FeedReaderContract.ZonePointFeedEntry.COLUMN_NAME_LON))));
                        zones.add(new ZonePoints(zoneId, zoneName, zoneActive, lst));
                    }
                } else {
                    zones.add(new ZoneRadius(
                            zoneId,
                            zoneName,
                            zoneActive,
                            new LatLng(c2.getDouble(c.getColumnIndex(FeedReaderContract.ZonePointFeedEntry.COLUMN_NAME_LAT)),
                            c2.getDouble(c.getColumnIndex(FeedReaderContract.ZonePointFeedEntry.COLUMN_NAME_LON))),
                            zoneRadius));
                }
            }
        }
        return zones;

    }

    public List<Zone> addZone(GPS gps, Zone zone) {
        if(zone.getClass() == ZonePoints.class) {
            ZonePoints zp = (ZonePoints) zone;
            SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(FeedReaderContract.ZoneFeedEntry.COLUMN_NAME_NAME, zp.getName());
            values.put(FeedReaderContract.ZoneFeedEntry.COLUMN_NAME_RADIUS, -1);
            values.put(FeedReaderContract.ZoneFeedEntry.COLUMN_NAME_ID_GPS, gps.getGPSID());
            values.put(FeedReaderContract.ZoneFeedEntry.COLUMN_NAME_ACTIVE, zp.isActive());

            long newRowId = -1;
            try {
                newRowId = db.insertOrThrow(
                        FeedReaderContract.ZoneFeedEntry.ZONE_TABLE_NAME,
                        null,
                        values);

                LinkedList<LatLng> points = zp.getPoints();
                for(LatLng ll : points) {
                    ContentValues values1 = new ContentValues();
                    values.put(FeedReaderContract.ZonePointFeedEntry.COLUMN_NAME_LAT, ll.latitude);
                    values.put(FeedReaderContract.ZonePointFeedEntry.COLUMN_NAME_LON, ll.longitude);
                    values.put(FeedReaderContract.ZonePointFeedEntry.COLUMN_NAME_ID_ZONE, newRowId);
                    newRowId = db.insertOrThrow(
                            FeedReaderContract.ZonePointFeedEntry.ZONE_POINT_TABLE_NAME,
                            null,
                            values1);
                }
            } catch (Exception e) {
            }
        } else if(zone.getClass() == ZoneRadius.class) {
            ZoneRadius zr = (ZoneRadius) zone;
            SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(FeedReaderContract.ZoneFeedEntry.COLUMN_NAME_NAME, zr.getName());
            values.put(FeedReaderContract.ZoneFeedEntry.COLUMN_NAME_RADIUS, zr.getRadius());
            values.put(FeedReaderContract.ZoneFeedEntry.COLUMN_NAME_ID_GPS, gps.getGPSID());
            values.put(FeedReaderContract.ZoneFeedEntry.COLUMN_NAME_ACTIVE, zr.isActive());

            long newRowId = -1;
            try {
                newRowId = db.insertOrThrow(
                        FeedReaderContract.ZoneFeedEntry.ZONE_TABLE_NAME,
                        null,
                        values);
                ContentValues values1 = new ContentValues();
                LatLng mid = zr.getMiddle();
                values.put(FeedReaderContract.ZonePointFeedEntry.COLUMN_NAME_LAT, mid.latitude);
                values.put(FeedReaderContract.ZonePointFeedEntry.COLUMN_NAME_LON, mid.longitude);
                values.put(FeedReaderContract.ZonePointFeedEntry.COLUMN_NAME_ID_ZONE, newRowId);
                newRowId = db.insertOrThrow(
                        FeedReaderContract.ZonePointFeedEntry.ZONE_POINT_TABLE_NAME,
                        null,
                        values1);
            } catch (Exception e) {
            }
        }
        return getZones(gps);
    }

    public List<Zone> modifyZone(GPS gps, Zone zone) {
        deleteZone(gps, zone);
        return addZone(gps, zone);
    }

    public List<Zone> deleteZone(GPS gps, Zone zone) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        String selection = FeedReaderContract.ZoneFeedEntry.COLUMN_NAME_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(zone.getId()) };
        db.delete(FeedReaderContract.ZoneFeedEntry.ZONE_TABLE_NAME, selection, selectionArgs);
        return getZones(gps);
    }

    public List<GPS> getGps(User user) {
        List<GPS> gps = new ArrayList<GPS>();
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor c = db.query(FeedReaderContract.GPSFeedEntry.GPS_TABLE_NAME,
                new String[] {FeedReaderContract.GPSFeedEntry.COLUMN_NAME_ID,
                        FeedReaderContract.GPSFeedEntry.COLUMN_NAME_NAME,
                        FeedReaderContract.GPSFeedEntry.COLUMN_NAME_IMAGE},
                FeedReaderContract.GPSFeedEntry.COLUMN_NAME_ID_USER + " = ?",
                new String[] {String.valueOf(user.getId())},
                null,
                null,
                null);

        c.moveToFirst();
        while (!c.isAfterLast()) {
            byte[] byteArray = c.getBlob(c.getColumnIndex(FeedReaderContract.GPSFeedEntry.COLUMN_NAME_IMAGE));

            gps.add(new GPS(c.getString(c.getColumnIndex(FeedReaderContract.GPSFeedEntry.COLUMN_NAME_ID)),
                    c.getString(c.getColumnIndex(FeedReaderContract.GPSFeedEntry.COLUMN_NAME_NAME)),
                    BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length)));
        }
        return gps;
    }

    public List<GPS> addGps(User user, GPS gps) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        gps.getAssignedPicture().compress(Bitmap.CompressFormat.PNG, 0, outputStream);

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.GPSFeedEntry.COLUMN_NAME_ID, gps.getGPSID());
        values.put(FeedReaderContract.GPSFeedEntry.COLUMN_NAME_NAME, gps.getGPSName());
        values.put(FeedReaderContract.GPSFeedEntry.COLUMN_NAME_IMAGE, outputStream.toByteArray());
        values.put(FeedReaderContract.GPSFeedEntry.COLUMN_NAME_ID_USER, user.getId());
        long newRowId = db.insert(
                FeedReaderContract.GPSFeedEntry.GPS_TABLE_NAME,
                null,
                values);
        return getGps(user);
    }

    public List<GPS> modifyGps(User user, GPS gps) {
        deleteGps(user, gps);
        return addGps(user, gps);
    }

    public List<GPS> deleteGps(User user, GPS gps) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        String selection = FeedReaderContract.GPSFeedEntry.COLUMN_NAME_ID + " LIKE ?";
        String[] selectionArgs = { gps.getGPSID() };
        db.delete(FeedReaderContract.GPSFeedEntry.GPS_TABLE_NAME, selection, selectionArgs);
        return getGps(user);
    }

    public LatLng getCurrentPosition(GPS gps) {
        return new LatLng(45.501689, -73.567256);
    }
}
