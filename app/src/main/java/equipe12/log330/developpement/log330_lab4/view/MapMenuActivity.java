package equipe12.log330.developpement.log330_lab4.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import equipe12.log330.developpement.log330_lab4.R;
import equipe12.log330.developpement.log330_lab4.database.DbFacade;
import equipe12.log330.developpement.log330_lab4.model.Zone;
import equipe12.log330.developpement.log330_lab4.utility.CommonVariables;

public class MapMenuActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DbFacade mDatabaseConn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_menu);
        mDatabaseConn = new DbFacade(CommonVariables.context);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.zoneMap);
        mapFragment.getMapAsync(this);
    }

    public void onMapReady(final GoogleMap map) {
        mMap = map;

        for(Zone z : mDatabaseConn.getZones(CommonVariables.selectedGPS)){
            z.drawZone(map);
        }
    }
}
