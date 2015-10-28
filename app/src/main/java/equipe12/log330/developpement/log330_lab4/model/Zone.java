package equipe12.log330.developpement.log330_lab4.model;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by serge on 2015-10-16.
 * Modified by chris : 2015-10-25
 */
public abstract class Zone {

    private int id;
    private String name;
    private boolean active;

    public Zone(int id, String name, boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     *
     * @return Latitude / Longitude coordinate
     */
    public abstract LatLng getFirstCoordinate();

    /**
     * Provides an extract method draw for its derivators
     * @param map of type GoogleMap
     */
    public abstract void draw(GoogleMap map);
}
