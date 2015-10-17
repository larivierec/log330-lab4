package equipe12.log330.developpement.log330_lab4.database;

import android.content.Context;

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

    private DBTransaction dbTransaction;

    public DbFacade(Context context) {
        dbTransaction = new DBTransaction(context);
    }

    public User isValidUser(String user, String password) {
        return dbTransaction.isValidUser(user, password);
    }

    public User addUser(String user, String password) {
        return dbTransaction.addUser(user, password);
    }

    public List<Zone> getZones(GPS gps) {
        return dbTransaction.getZones(gps);
    }

    public List<Zone> addZone(GPS gps, Zone zone) {
        return dbTransaction.addZone(gps, zone);
    }

    public List<Zone> modifyZone(GPS gps, Zone zone) {
        return dbTransaction.modifyZone(gps, zone);
    }

    public List<Zone> deleteZone(GPS gps, Zone zone) {
        return dbTransaction.deleteZone(gps, zone);
    }

    public List<GPS> getGps(User user) {
        return dbTransaction.getGps(user);
    }

    public List<GPS> addGps(User user, GPS gps) {
        return dbTransaction.addGps(user, gps);
    }

    public List<GPS> modifyGps(User user, GPS gps) { return dbTransaction.modifyGps(user, gps); }

    public List<GPS> deleteGps(User user,GPS gps) {
        return dbTransaction.deleteGps(user, gps);
    }

    public LatLng getCurrentPosition(GPS gps) {
        return dbTransaction.getCurrentPosition(gps);
    }
}
