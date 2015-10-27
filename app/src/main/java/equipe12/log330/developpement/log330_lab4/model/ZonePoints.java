package equipe12.log330.developpement.log330_lab4.model;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

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

    @Override
    public LatLng getFirstCoordinate() {
        return points.getFirst();
    }

    @Override
    public void drawZone(GoogleMap map) {
        PolygonOptions pOptions = new PolygonOptions()
                .strokeColor(Color.BLUE)
                .fillColor(Color.GRAY);
        pOptions.addAll(points);
        Polygon poly = map.addPolygon(pOptions);
    }
}
