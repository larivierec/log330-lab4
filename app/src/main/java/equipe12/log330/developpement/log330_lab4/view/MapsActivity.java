package equipe12.log330.developpement.log330_lab4.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.LinkedList;

import equipe12.log330.developpement.log330_lab4.R;
import equipe12.log330.developpement.log330_lab4.database.DbFacade;
import equipe12.log330.developpement.log330_lab4.model.GPS;
import equipe12.log330.developpement.log330_lab4.model.Zone;
import equipe12.log330.developpement.log330_lab4.model.ZonePoints;
import equipe12.log330.developpement.log330_lab4.model.ZoneRadius;
import equipe12.log330.developpement.log330_lab4.utility.CommonVariables;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GPS mGPS;
    private GoogleMap mMap;
    private GoogleMapOptions mMapOptions = new GoogleMapOptions();
    private LinkedList<LatLng> mUserSelections = new LinkedList<>();
    private DbFacade database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        database = new DbFacade(CommonVariables.context);

        this.mGPS = (GPS) getIntent().getSerializableExtra("GPS");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        mMapOptions.mapType(GoogleMap.MAP_TYPE_NORMAL)
                .compassEnabled(true)
                .rotateGesturesEnabled(true)
                .tiltGesturesEnabled(true)
                .zoomControlsEnabled(true)
                .zoomGesturesEnabled(true)
                .mapToolbarEnabled(true);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                mUserSelections.push(latLng);
                if (mUserSelections.size() == 1) {
                    CircleOptions cOptions = new CircleOptions()
                            .center(latLng)
                            .fillColor(Color.GREEN)
                            .radius(5000);
                    Circle drawingRadius = mMap.addCircle(cOptions);
                } else if (mUserSelections.size() == 2) {
                    mMap.clear();

                    Polyline pLine = mMap.addPolyline(new PolylineOptions());
                    pLine.setPoints(mUserSelections);
                    pLine.setColor(Color.CYAN);
                } else if (mUserSelections.size() > 2) {
                    mMap.clear();
                    PolygonOptions pOptions = new PolygonOptions()
                            .strokeColor(Color.BLUE)
                            .fillColor(Color.GRAY);
                    pOptions.addAll(mUserSelections);

                    Polygon poly = mMap.addPolygon(pOptions);
                }
            }
        });

        LatLng montreal = new LatLng(45.5017, -73.5673);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(montreal));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gps_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.gps_save_zone) {
            if(!mUserSelections.isEmpty()){
                int numberOfPoints = mUserSelections.size();
                Zone zoneToSave = null;

                //create a circle
                if(numberOfPoints == 1){
                    zoneToSave = new ZoneRadius(-1, mGPS.getGPSName() + "_circlezone",
                            true, mUserSelections.getFirst(), 5000);
                }
                else if(numberOfPoints >= 3){
                    zoneToSave = new ZonePoints(-1, mGPS.getGPSName() + "_zone",
                            true, mUserSelections);
                }
                if(zoneToSave != null)
                    database.addZone(mGPS, zoneToSave);
            }else{
                Toast.makeText(getApplicationContext(),R.string.gps_selection_incorrect,Toast.LENGTH_LONG).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}