package equipe12.log330.developpement.log330_lab4.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by serge on 2015-10-16.
 */
public class ZoneRadius extends Zone {

    private LatLng middle;
    private int radius;

    public ZoneRadius(int id, String name, boolean active, LatLng middle, int radius) {
        super(id, name, active);
        this.middle = middle;
        this.radius = radius;
    }

    public LatLng getMiddle() {
        return middle;
    }

    public void setMiddle(LatLng middle) {
        this.middle = middle;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
