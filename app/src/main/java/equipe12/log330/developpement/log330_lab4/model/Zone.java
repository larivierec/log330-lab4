package equipe12.log330.developpement.log330_lab4.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedList;

/**
 * Created by serge on 2015-10-16.
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
}
