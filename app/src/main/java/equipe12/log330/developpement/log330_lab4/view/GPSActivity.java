package equipe12.log330.developpement.log330_lab4.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.LinkedList;

import equipe12.log330.developpement.log330_lab4.R;
import equipe12.log330.developpement.log330_lab4.database.DbFacade;
import equipe12.log330.developpement.log330_lab4.interfaces.DialogGPSAccepted;
import equipe12.log330.developpement.log330_lab4.model.GPS;
import equipe12.log330.developpement.log330_lab4.utility.CommonVariables;
import equipe12.log330.developpement.log330_lab4.utility.GPSAdapter;

/**
 * Uses the DialogGPSAccepted interface to add a new entity to the list
 */

public class GPSActivity extends Activity implements DialogGPSAccepted {

    final private Context mContext = this;
    private GPSAdapter mGPSAdapter;
    private ListView gpsDataLV;
    private DbFacade dbFacade;
    private LinkedList<GPS> mGPSList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbFacade = new DbFacade(getBaseContext());
        //create dummy coordinates
        // for adding GPS
        final Button gpsButton = (Button) findViewById(R.id.btn_add_gps);
        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog gps_data_dialog = new Dialog(mContext);
                gps_data_dialog.setContentView(R.layout.dialog_gps_layout);
                gps_data_dialog.setTitle("Add a GPS");

                final Button dialogAdd = (Button) gps_data_dialog.findViewById(R.id.btn_add_gps_data);
                final Button dialogCancel = (Button) gps_data_dialog.findViewById(R.id.btn_cancel_gps_data);

                final EditText view_gpsname = (EditText) gps_data_dialog.findViewById(R.id.txt_gps_name);
                final EditText view_gpsid = (EditText) gps_data_dialog.findViewById(R.id.txt_gps_id);


                dialogAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String gpsname = view_gpsname.getText().toString();
                        String gpsid = view_gpsid.getText().toString();

                        GPSActivity.this.onDialogButtonAdded(gpsname, gpsid, null);
                        gps_data_dialog.dismiss();
                    }
                });

                dialogCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gps_data_dialog.cancel();
                    }
                });
                gps_data_dialog.show();
            }
        });

        gpsDataLV = (ListView) findViewById(R.id.lst_avail_gps);
        mGPSList = dbFacade.getGps(CommonVariables.user);
        mGPSAdapter = new GPSAdapter(this, mGPSList);
        gpsDataLV.setAdapter(mGPSAdapter);

        gpsDataLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final Dialog gps_info_dialog = new Dialog(mContext);
                gps_info_dialog.setContentView(R.layout.lv_command_dialog);
                gps_info_dialog.setTitle("GPS Commands");
                final Button gps_remove = (Button) gps_info_dialog.findViewById(R.id.btn_delete_gps);
                final Button map_mode = (Button) gps_info_dialog.findViewById(R.id.btn_open_maps_view);
                final Button btn_cancel = (Button) gps_info_dialog.findViewById(R.id.btn_gps_info_cancel);

                gps_remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dbFacade.deleteGps(CommonVariables.user, mGPSList.remove(position));
                        mGPSAdapter.notifyDataSetChanged();
                        gps_info_dialog.dismiss();
                    }
                });

                map_mode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gps_info_dialog.dismiss();
                        Intent mapsIntent = new Intent(mContext, MapsActivity.class);
                        startActivity(mapsIntent);
                    }
                });

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gps_info_dialog.cancel();
                    }
                });

                gps_info_dialog.show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onDialogButtonAdded(String gpsName, String gpsID, String assignedPicture) {
        if(!gpsName.trim().isEmpty() && !gpsID.trim().isEmpty()){
            DbFacade f = new DbFacade(getBaseContext());
            mGPSList = f.addGps(CommonVariables.user, new GPS(gpsID, gpsName, null));
            mGPSAdapter = new GPSAdapter(this, mGPSList);
            gpsDataLV.setAdapter(mGPSAdapter);
            mGPSAdapter.notifyDataSetChanged();
        }
    }
}