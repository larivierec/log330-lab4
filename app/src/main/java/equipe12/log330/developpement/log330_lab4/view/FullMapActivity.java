package equipe12.log330.developpement.log330_lab4.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import equipe12.log330.developpement.log330_lab4.R;
import equipe12.log330.developpement.log330_lab4.database.DbFacade;
import equipe12.log330.developpement.log330_lab4.model.GPS;
import equipe12.log330.developpement.log330_lab4.model.Zone;
import equipe12.log330.developpement.log330_lab4.utility.CommonVariables;

public class FullMapActivity extends FragmentActivity implements OnMapReadyCallback, Observer {

    private GoogleMap mMap;
    List<Marker> markers = new ArrayList<Marker>();

    private DbFacade mDatabaseConn;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_map_view);
        mDatabaseConn = CommonVariables.dbFacade;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapMenu);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        CommonVariables.locationEvent.deleteObserver(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
    }

    public void onMapReady(final GoogleMap map) {
        mMap = map;
        CommonVariables.locationEvent.addObserver(this);
        createZonesAndMarkers();
    }

    @Override
    public void update(Observable observable, Object data) {
        if(data != null && data instanceof GPS){
            final GPS g = (GPS)data;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, g.getGPSName() + " est sorti de sa zone.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                repositionGPS();
            }
        });
    }

    private void createZonesAndMarkers(){
        mMap.clear();
        LinkedList<GPS> gpses = mDatabaseConn.getGps(CommonVariables.user);
        for(GPS gps : gpses) {
            for (Zone z : mDatabaseConn.getZones(gps)) {
                if(z.isActive()) {
                    z.draw(mMap);
                }
            }
        }

        LinkedList<LatLng> lls = mDatabaseConn.getAllCurrentPositions(CommonVariables.user);
        for(LatLng ll : lls) {
            MarkerOptions mOptions = new MarkerOptions()
                    .position(ll)
                    .title("");
            markers.add(mMap.addMarker(mOptions));
        }
    }

    private void repositionGPS() {
        for(Marker m : markers) {
            m.remove();
        }
        LinkedList<LatLng> lls = mDatabaseConn.getAllCurrentPositions(CommonVariables.user);
        for(LatLng ll : lls) {
            MarkerOptions mOptions = new MarkerOptions()
                    .position(ll)
                    .title("");
            markers.add(mMap.addMarker(mOptions));
        }
    }
}
