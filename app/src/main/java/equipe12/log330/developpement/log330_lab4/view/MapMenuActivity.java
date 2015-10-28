package equipe12.log330.developpement.log330_lab4.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Observable;
import java.util.Observer;

import equipe12.log330.developpement.log330_lab4.R;
import equipe12.log330.developpement.log330_lab4.database.DbFacade;
import equipe12.log330.developpement.log330_lab4.model.GPS;
import equipe12.log330.developpement.log330_lab4.model.Zone;
import equipe12.log330.developpement.log330_lab4.utility.CommonVariables;

public class MapMenuActivity extends FragmentActivity implements OnMapReadyCallback, Observer {

    private GoogleMap mMap;
    private DbFacade mDatabaseConn;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_menu);
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
    }

    @Override
    public void update(Observable observable, Object data) {
        if(data != null && data instanceof GPS){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "The GPS is out of its zone", Toast.LENGTH_SHORT).show();
                }
            });
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                paintZonesAndGPS();
            }
        });
    }

    private void paintZonesAndGPS(){
        mMap.clear();
        for(Zone z : mDatabaseConn.getZones(CommonVariables.selectedGPS)){
            z.draw(mMap);
        }
        paintGPS();
    }

    private void paintGPS() {
        LatLng ll = mDatabaseConn.getCurrentPosition(CommonVariables.selectedGPS);
        MarkerOptions mOptions = new MarkerOptions()
                .position(ll)
                .title("Current GPS Position");
        //Log.d("MAP_MENU_ACTIVITY", "Current Loc : " + ll.toString());
        mMap.addMarker(mOptions);
    }
}
