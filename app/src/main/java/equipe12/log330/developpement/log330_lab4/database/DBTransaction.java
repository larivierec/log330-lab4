package equipe12.log330.developpement.log330_lab4.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

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
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
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

    public LinkedList<Zone> getZones(GPS gps) {
        LinkedList<Zone> zones = new LinkedList<Zone>();
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
                    LinkedList<LatLng> lst = new LinkedList<LatLng>();
                    while(!c2.isAfterLast()) {
                        lst.add(new LatLng(c2.getDouble(c2.getColumnIndex(FeedReaderContract.ZonePointFeedEntry.COLUMN_NAME_LAT)),
                                c2.getDouble(c2.getColumnIndex(FeedReaderContract.ZonePointFeedEntry.COLUMN_NAME_LON))));
                        c2.moveToNext();
                    }
                    if(!lst.isEmpty()) {
                        zones.add(new ZonePoints(zoneId, zoneName, zoneActive, lst));
                    }
                } else {
                    zones.add(new ZoneRadius(
                            zoneId,
                            zoneName,
                            zoneActive,
                            new LatLng(c2.getDouble(c2.getColumnIndex(FeedReaderContract.ZonePointFeedEntry.COLUMN_NAME_LAT)),
                            c2.getDouble(c2.getColumnIndex(FeedReaderContract.ZonePointFeedEntry.COLUMN_NAME_LON))),
                            zoneRadius));
                }
            }
            c.moveToNext();
        }
        return zones;

    }

    public LinkedList<Zone> addZone(GPS gps, Zone zone) {
        if(zone.getClass() == ZonePoints.class) {
            ZonePoints zp = (ZonePoints) zone;
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
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
                    values1.put(FeedReaderContract.ZonePointFeedEntry.COLUMN_NAME_LAT, ll.latitude);
                    values1.put(FeedReaderContract.ZonePointFeedEntry.COLUMN_NAME_LON, ll.longitude);
                    values1.put(FeedReaderContract.ZonePointFeedEntry.COLUMN_NAME_ID_ZONE, newRowId);
                    db.insertOrThrow(
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
                values1.put(FeedReaderContract.ZonePointFeedEntry.COLUMN_NAME_LAT, mid.latitude);
                values1.put(FeedReaderContract.ZonePointFeedEntry.COLUMN_NAME_LON, mid.longitude);
                values1.put(FeedReaderContract.ZonePointFeedEntry.COLUMN_NAME_ID_ZONE, newRowId);
                newRowId = db.insertOrThrow(
                        FeedReaderContract.ZonePointFeedEntry.ZONE_POINT_TABLE_NAME,
                        null,
                        values1);
            } catch (Exception e) {
            }
        }
        return getZones(gps);
    }

    public LinkedList<Zone> modifyZone(GPS gps, Zone zone) {
        //deleteZone(gps, zone);
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        String whereCondtion = FeedReaderContract.ZoneFeedEntry.COLUMN_NAME_ID + " = ?";
        ContentValues cv = new ContentValues();
        cv.put(FeedReaderContract.ZoneFeedEntry.COLUMN_NAME_ACTIVE, zone.isActive());
        db.update(FeedReaderContract.ZoneFeedEntry.ZONE_TABLE_NAME, cv, whereCondtion, new String[]{String.valueOf(zone.getId())});
        return getZones(gps);
    }

    public LinkedList<Zone> deleteZone(GPS gps, Zone zone) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        String selection = FeedReaderContract.ZoneFeedEntry.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = { String.valueOf(zone.getId()) };
        db.delete(FeedReaderContract.ZoneFeedEntry.ZONE_TABLE_NAME, selection, selectionArgs);
        return getZones(gps);
    }

    public LinkedList<GPS> getGps(User user) {
        LinkedList<GPS> gps = new LinkedList<GPS>();
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        Cursor c = db.rawQuery("select g." + FeedReaderContract.GPSFeedEntry.COLUMN_NAME_ID + "," +
                "g." + FeedReaderContract.GPSFeedEntry.COLUMN_NAME_NAME + "," +
                "g." + FeedReaderContract.GPSFeedEntry.COLUMN_NAME_IMAGE + " from " + FeedReaderContract.GPSFeedEntry.GPS_TABLE_NAME +
                " as g join " + FeedReaderContract.UserGPSFeedEntry.USER_GPS_TABLE_NAME +
                " as ug on ug." + FeedReaderContract.UserGPSFeedEntry.COLUMN_NAME_ID_GPS +
                " = g." + FeedReaderContract.GPSFeedEntry.COLUMN_NAME_ID +
                " where ug." + FeedReaderContract.UserGPSFeedEntry.COLUMN_NAME_ID_USER + " = ?", new String[] {String.valueOf(user.getId())});

        c.moveToFirst();
        while (!c.isAfterLast()) {
            byte[] byteArray = c.getBlob(2);
            Bitmap img = null;
            if(byteArray != null) {
                BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            }
            gps.add(new GPS(c.getString(0),
                    c.getString(1),
                    img));
            c.moveToNext();
        }
        return gps;
    }

    public LinkedList<GPS> addGps(User user, GPS gps) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.GPSFeedEntry.COLUMN_NAME_ID, gps.getGPSID());
        values.put(FeedReaderContract.GPSFeedEntry.COLUMN_NAME_NAME, gps.getGPSName());

        Bitmap img = gps.getAssignedPicture();
        if(img != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
            values.put(FeedReaderContract.GPSFeedEntry.COLUMN_NAME_IMAGE, outputStream.toByteArray());
        }
        try {
            db.insertOrThrow(
                    FeedReaderContract.GPSFeedEntry.GPS_TABLE_NAME,
                    null,
                    values);
        } catch(Exception e) {

        }
        ContentValues values1 = new ContentValues();
        values1.put(FeedReaderContract.UserGPSFeedEntry.COLUMN_NAME_ID_GPS, gps.getGPSID());
        values1.put(FeedReaderContract.UserGPSFeedEntry.COLUMN_NAME_ID_USER, user.getId());
        db.insert(
                FeedReaderContract.UserGPSFeedEntry.USER_GPS_TABLE_NAME,
                null,
                values1);
        return getGps(user);
    }

    public LinkedList<GPS> modifyGps(User user, GPS gps) {
        deleteGps(user, gps);
        return addGps(user, gps);
    }

    public LinkedList<GPS> deleteGps(User user, GPS gps) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        String selection = FeedReaderContract.UserGPSFeedEntry.COLUMN_NAME_ID_GPS + " LIKE ? and " +
                FeedReaderContract.UserGPSFeedEntry.COLUMN_NAME_ID_USER + " = ?";
        String[] selectionArgs = { gps.getGPSID(), String.valueOf(user.getId()) };
        db.delete(FeedReaderContract.UserGPSFeedEntry.USER_GPS_TABLE_NAME, selection, selectionArgs);
        return getGps(user);
    }

    public void addCurrentPosition(GPS gps, LatLng latLng) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.GPSPositionFeedEntry.COLUMN_NAME_LAT, latLng.latitude);
        values.put(FeedReaderContract.GPSPositionFeedEntry.COLUMN_NAME_LON, latLng.longitude);
        values.put(FeedReaderContract.GPSPositionFeedEntry.COLUMN_NAME_CREATED_TIME, getDateTime());
        values.put(FeedReaderContract.GPSPositionFeedEntry.COLUMN_NAME_ID_GPS, gps.getGPSID());
        db.insert(
                FeedReaderContract.GPSPositionFeedEntry.GPS_POSITION_TABLE_NAME,
                null,
                values);
    }

    public LatLng getCurrentPosition(GPS gps) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor c = db.query(FeedReaderContract.GPSPositionFeedEntry.GPS_POSITION_TABLE_NAME,
                new String[] {FeedReaderContract.GPSPositionFeedEntry.COLUMN_NAME_ID,
                        FeedReaderContract.GPSPositionFeedEntry.COLUMN_NAME_LAT,
                        FeedReaderContract.GPSPositionFeedEntry.COLUMN_NAME_LON},
                FeedReaderContract.GPSPositionFeedEntry.COLUMN_NAME_ID_GPS + " = ?",
                new String[] {gps.getGPSID()},
                null,
                null,
                FeedReaderContract.GPSPositionFeedEntry.COLUMN_NAME_CREATED_TIME + " DESC LIMIT 1");

        if (c.moveToFirst()) {
            return new LatLng(c.getDouble(c.getColumnIndex(FeedReaderContract.GPSPositionFeedEntry.COLUMN_NAME_LAT)),
                    c.getDouble(c.getColumnIndex(FeedReaderContract.GPSPositionFeedEntry.COLUMN_NAME_LON)));
        }
        return null;
    }

    public LinkedList<LatLng> getAllCurrentPositions(User user) {
        LinkedList<GPS> gpses = getGps(user);
        LinkedList<LatLng> positions = new LinkedList<LatLng>();
        for(GPS g : gpses) {
            LatLng ll = getCurrentPosition(g);
            if(ll != null) {
                positions.add(ll);
            }
        }
        return positions;
    }

    private String getDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

}
