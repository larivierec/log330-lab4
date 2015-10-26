package equipe12.log330.developpement.log330_lab4.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import equipe12.log330.developpement.log330_lab4.R;

public class ZoneMapActivity extends FragmentActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone_map);

    }

    public void onMapReady(GoogleMap map) {

    }
}
