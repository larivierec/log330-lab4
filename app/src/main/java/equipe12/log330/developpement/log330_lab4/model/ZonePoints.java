package equipe12.log330.developpement.log330_lab4.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedList;

/**
 * Created by serge on 2015-10-16.
 */
public class ZonePoints extends Zone {

    private LinkedList<LatLng> points;

    public ZonePoints(int id, String name, boolean active, LinkedList<LatLng> points) {
        super(id, name, active);
        this.points = points;
    }

    public LinkedList<LatLng> getPoints() {
        return points;
    }

    public void setPoints(LinkedList<LatLng> radius) {
        this.points = radius;
    }
}
