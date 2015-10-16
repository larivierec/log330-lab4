package equipe12.log330.developpement.log330_lab4.database;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import equipe12.log330.developpement.log330_lab4.model.GPS;
import equipe12.log330.developpement.log330_lab4.model.User;
import equipe12.log330.developpement.log330_lab4.model.Zone;

/**
 * Created by serge on 2015-10-16.
 */
public class DbFacade {

    public static User isValidUser(String user, String password) {
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
