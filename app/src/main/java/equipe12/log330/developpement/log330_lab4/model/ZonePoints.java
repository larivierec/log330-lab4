package equipe12.log330.developpement.log330_lab4.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedList;

/**
 * Created by serge on 2015-10-16.
 */
public class ZonePoints extends Zone {

    private LinkedList<LatLng> radius;

    public ZonePoints(int id, String name, boolean active, LinkedList<LatLng> radius) {
        super(id, name, active);
        this.radius = radius;
    }

    public LinkedList<LatLng> getRadius() {
        return radius;
    }

    public void setRadius(LinkedList<LatLng> radius) {
        this.radius = radius;
    }
}
