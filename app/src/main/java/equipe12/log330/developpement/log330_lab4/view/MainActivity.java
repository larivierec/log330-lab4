package equipe12.log330.developpement.log330_lab4.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

import equipe12.log330.developpement.log330_lab4.R;
import equipe12.log330.developpement.log330_lab4.model.GPS;
import equipe12.log330.developpement.log330_lab4.utility.CommonVariables;

public class MainActivity extends Activity implements Observer {

    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_view);

        final Button listGpsButton = (Button) findViewById(R.id.btnListGps);
        final Button carteButton = (Button) findViewById(R.id.btnMap);

        listGpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this,
                        GPSListActivity.class);
                startActivity(myIntent);
            }
        });

        carteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this,
                        FullMapActivity.class);
                startActivity(myIntent);
            }
        });

        CommonVariables.locationEvent.addObserver(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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
    }

}