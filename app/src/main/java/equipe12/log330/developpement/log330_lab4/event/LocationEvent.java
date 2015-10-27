package equipe12.log330.developpement.log330_lab4.event;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
    private static int n = 0;

    public LocationEvent() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        Log.d("LOCATION_EVENT", "Thread" + n++);
        double x = 0.0001;
        double y = 0.0001;
        double multiplierX = Math.random() < 0.5 ? -1 : 1;
        double multiplierY = Math.random() < 0.5 ? -1 : 1;
        List<GPS> wasOut = new ArrayList<GPS>();

        while (true) {
            if (CommonVariables.context != null && CommonVariables.user != null && CommonVariables.dbFacade != null) {
                LinkedList<GPS> gpses = CommonVariables.dbFacade.getGps(CommonVariables.user);
                for (GPS g : gpses) {
                    if(g != null) {
                        Log.d("LOCATION_EVENT", "inside Gps : " + g.toString());
                        LinkedList<Zone> zones = CommonVariables.dbFacade.getZones(g);
                        if (zones.size() > 0) {
                            Zone z = zones.get(0);
                            Log.d("LOCATION_EVENT", "inside zone : " + z.toString());
                            setChanged();
                            if (z instanceof ZoneRadius) {
                                LatLng mid = ((ZoneRadius) z).getMiddle();
                                Log.d("LOCATION_EVENT", "X : " + x + " Y : " + y);
                                LatLng newLL = new LatLng(mid.latitude + x, mid.longitude + y);
                                CommonVariables.dbFacade.addCurrentPosition(g, newLL);
                                if (distance(mid.latitude, mid.longitude, newLL.latitude, newLL.longitude) < (((ZoneRadius) z).getRadius() * 1000) && !wasOut.contains(g)) {
                                    multiplierX = Math.random() < 0.5 ? -1 : 1;
                                    multiplierY = Math.random() < 0.5 ? -1 : 1;
                                    wasOut.add(g);
                                    notifyObservers(g);
                                } else {
                                    x += (0.0001 * multiplierX);
                                    y += (0.0001 * multiplierY);
                                    wasOut.remove(g);
                                    notifyObservers(null);
                                }
                            } else if (z instanceof ZonePoints) {
                                LinkedList<LatLng> points = ((ZonePoints) z).getPoints();
                                if (points.size() > 0) {
                                    LatLng mid = points.get(0);
                                    LatLng newLL = new LatLng(mid.latitude + x, mid.longitude + y);
                                    CommonVariables.dbFacade.addCurrentPosition(g, newLL);
                                    Log.d("LOCATION_EVENT", "Added : " + newLL.toString());
                                    if (!isPointInPolygon(newLL, points) && !wasOut.contains(g)) {
                                        multiplierX = Math.random() < 0.5 ? -1 : 1;
                                        multiplierY = Math.random() < 0.5 ? -1 : 1;
                                        wasOut.add(g);
                                        notifyObservers(g);
                                    } else {
                                        x += (0.0001 * multiplierX);
                                        y += (0.0001 * multiplierY);
                                        wasOut.remove(g);
                                        notifyObservers(null);
                                    }
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
