package equipe12.log330.developpement.log330_lab4.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GoogleMapOptions mMapOptions = new GoogleMapOptions();
    private LinkedList<LatLng> mHardCodedList = new LinkedList<>();
    private LinkedList<LatLng> mUserSelections = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mHardCodedList.push(new LatLng(45.501689, -73.567256)); //ets
        mHardCodedList.push(new LatLng(55.00, -115)); //alberta
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMapOptions.mapType(GoogleMap.MAP_TYPE_NORMAL)
                .compassEnabled(true)
                .rotateGesturesEnabled(true)
                .tiltGesturesEnabled(true)
                .zoomControlsEnabled(true)
                .zoomGesturesEnabled(true)
                .mapToolbarEnabled(true);


        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mUserSelections.push(latLng);
                if(mUserSelections.size() > 1 && mUserSelections.size() <= 2){
                    Polyline pLine = mMap.addPolyline(new PolylineOptions());
                    pLine.setPoints(mUserSelections);
                    pLine.setColor(Color.CYAN);
                }
                else if(mUserSelections.size() > 2){
                    mMap.clear();
                    PolygonOptions pOptions = new PolygonOptions()
                            .strokeColor(Color.BLUE)
                            .fillColor(Color.GRAY);
                    pOptions.addAll(mUserSelections);

                    Polygon poly = mMap.addPolygon(pOptions);
                }
            }
        });

        // Add a marker in Sydney and move the camera
        LatLng montreal = new LatLng(45.5017, -73.5673);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(montreal));

        for(LatLng coord : mHardCodedList){
            Circle circle = mMap.addCircle(new CircleOptions()
                    .center(coord)
                    .radius(50000)
                    .strokeColor(Color.BLUE));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gps_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.gps_save_zone) {
            //TODO save the zone to the database
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}