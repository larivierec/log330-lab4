package equipe12.log330.developpement.log330_lab4.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import equipe12.log330.developpement.log330_lab4.R;
import equipe12.log330.developpement.log330_lab4.database.DbFacade;
import equipe12.log330.developpement.log330_lab4.model.Zone;
import equipe12.log330.developpement.log330_lab4.utility.CommonVariables;

public class ZoneMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private DbFacade mDatabaseConn;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zone_map_view);
        mDatabaseConn = CommonVariables.dbFacade;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.zoneMap);
        mapFragment.getMapAsync(this);
    }

    public void onMapReady(final GoogleMap map) {
        mMap = map;

        for(Zone z : mDatabaseConn.getZones(CommonVariables.selectedGPS)){
            z.draw(map);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(z.getFirstCoordinate()));
        }
    }
}
