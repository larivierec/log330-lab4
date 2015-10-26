package equipe12.log330.developpement.log330_lab4.event;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedList;
import java.util.Observable;

import equipe12.log330.developpement.log330_lab4.database.DbFacade;
import equipe12.log330.developpement.log330_lab4.model.GPS;
import equipe12.log330.developpement.log330_lab4.model.Zone;
import equipe12.log330.developpement.log330_lab4.model.ZonePoints;
import equipe12.log330.developpement.log330_lab4.model.ZoneRadius;
import equipe12.log330.developpement.log330_lab4.utility.CommonVariables;

/**
 * Created by serge on 2015-10-26.
 */
public class LocationEvent extends Observable implements Runnable {
    private int n = 0;

    public LocationEvent() {
        new Thread(this).start();
    }

    public void setValue(int n) {
        this.n = n;
        setChanged();
        notifyObservers();
    }

    public int getValue() {
        return n;
    }

    @Override
    public void run() {
        while (true) {
            if (CommonVariables.context != null && CommonVariables.user != null) {
                DbFacade f = new DbFacade(CommonVariables.context);
                double x = 0.0001;
                double y = 0.0001;
                double multiplier = 1;

                LinkedList<GPS> gpses = f.getGps(CommonVariables.user);
                for (GPS g : gpses) {
                    LinkedList<Zone> zones = f.getZones(g);
                    if (zones.size() > 0) {
                        Zone z = zones.get(0);
                        if (z instanceof ZoneRadius) {
                            LatLng mid = ((ZoneRadius) z).getMiddle();
                            LatLng newLL = new LatLng(mid.latitude + x, mid.longitude + y);
                            f.addCurrentPosition(g, newLL);
                            if (distance(mid.latitude, mid.longitude, newLL.latitude, newLL.longitude) < (((ZoneRadius) z).getRadius() * 1000)) {
                                x += (0.0001 * multiplier);
                                y += (0.0001 * multiplier);
                            } else {
                                notifyObservers(g);
                                multiplier *= -1;
                            }
                        } else if (z instanceof ZonePoints) {
                            LinkedList<LatLng> points = ((ZonePoints) z).getPoints();
                            if (points.size() > 0) {
                                LatLng mid = points.get(0);
                                LatLng newLL = new LatLng(mid.latitude + x, mid.longitude + y);
                                f.addCurrentPosition(g, newLL);
                                if (isPointInPolygon(newLL, points)) {
                                    x += (0.0001 * multiplier);
                                    y += (0.0001 * multiplier);
                                } else {
                                    notifyObservers(g);
                                    multiplier *= -1;
                                }
                            }
                        }
                    }
                }
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static Double distance(double nLat, double nLon,
                                  double nLat2, double nLon2) {
        double distance = 0;
        Location location1 = new Location("1");
        location1.setLatitude(nLat);
        location1.setLongitude(nLon);

        Location location2 = new Location("2");
        location2.setLatitude(nLat2);
        location2.setLongitude(nLon2);

        distance = location2.distanceTo(location2);

        return distance;
    }

    private boolean isPointInPolygon(LatLng location, LinkedList<LatLng> area) {
        int intersectCount = 0;
        for (int j = 0; j < area.size() - 1; j++) {
            if (rayCastIntersect(location, area.get(j), area.get(j + 1))) {
                intersectCount++;
            }
        }

        return ((intersectCount % 2) == 1);
    }

    private boolean rayCastIntersect(LatLng location, LatLng verticeA, LatLng verticeB) {

        double verticaALat = verticeA.latitude;
        double verticeBLat = verticeB.latitude;
        double verticeALon = verticeA.longitude;
        double verticeBLon = verticeB.longitude;
        double locationLat = location.latitude;
        double locationLon = location.longitude;

        if ((verticaALat > locationLat && verticeBLat > locationLat) ||
                (verticaALat < locationLat && verticeBLat < locationLat)
                || (verticeALon < locationLon && verticeBLon < locationLon)) {
            return false;
        }

        double slope = (verticaALat - verticeBLat) / (verticeALon - verticeBLon);
        double linearEquation = (-verticeALon) * slope + verticaALat;
        double x = (locationLat - linearEquation) / slope;

        return x > locationLon;
    }
}
