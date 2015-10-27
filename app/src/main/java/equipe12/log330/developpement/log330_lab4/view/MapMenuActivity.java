package equipe12.log330.developpement.log330_lab4.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Observable;
import java.util.Observer;

import equipe12.log330.developpement.log330_lab4.R;
import equipe12.log330.developpement.log330_lab4.database.DbFacade;
import equipe12.log330.developpement.log330_lab4.event.LocationEvent;
import equipe12.log330.developpement.log330_lab4.model.Zone;
import equipe12.log330.developpement.log330_lab4.utility.CommonVariables;

public class MapMenuActivity extends FragmentActivity implements OnMapReadyCallback, Observer {

    private GoogleMap mMap;
    private DbFacade mDatabaseConn;
    private LocationEvent mLocationEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_menu);
        mDatabaseConn = new DbFacade(CommonVariables.context);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapMenu);
        mapFragment.getMapAsync(this);
    }

    public void onMapReady(final GoogleMap map) {
        mMap = map;

        //TODO mLocationEvent
        mLocationEvent = new LocationEvent();
        mLocationEvent.addObserver(this);

        paintZonesAndGPS();
    }

    @Override
    public void update(Observable observable, Object data) {
        if(observable == mLocationEvent){
            paintZonesAndGPS();
        }
    }

    private void paintZonesAndGPS(){
        for(Zone z : mDatabaseConn.getZones(CommonVariables.selectedGPS)){
            z.drawZone(mMap);
        }
        MarkerOptions mOptions = new MarkerOptions()
                .position(mDatabaseConn.getCurrentPosition(CommonVariables.selectedGPS))
                .title("Current GPS Position");
        mMap.addMarker(mOptions);
    }
}
