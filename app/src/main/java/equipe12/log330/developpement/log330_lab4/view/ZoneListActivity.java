package equipe12.log330.developpement.log330_lab4.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ToggleButton;

import java.util.LinkedList;

import equipe12.log330.developpement.log330_lab4.R;
import equipe12.log330.developpement.log330_lab4.model.Zone;
import equipe12.log330.developpement.log330_lab4.utility.CommonVariables;
import equipe12.log330.developpement.log330_lab4.utility.ZoneAdapter;

public class ZoneListActivity extends Activity {
    ListView listView ;

    final private Context mContext = this;
    private ZoneAdapter mZoneAdapter;
    private ListView zoneDataLV;
    private LinkedList<Zone> mZoneList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zone_list_view);

        final Button zoneAddButton = (Button) findViewById(R.id.btn_add_zone);
        zoneAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapsIntent = new Intent(mContext, MapsActivity.class);
                startActivity(mapsIntent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        zoneDataLV = (ListView) findViewById(R.id.lst_avail_zone);
        mZoneList = CommonVariables.dbFacade.getZones(CommonVariables.selectedGPS);
        mZoneAdapter = new ZoneAdapter(this, mZoneList);
        zoneDataLV.setAdapter(mZoneAdapter);

        zoneDataLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final Dialog zone_info_dialog = new Dialog(mContext);
                zone_info_dialog.setContentView(R.layout.zone_command_dialog);
                zone_info_dialog.setTitle("Commandes");

                final Button zone_remove = (Button) zone_info_dialog.findViewById(R.id.btn_remove_zone);
                final ToggleButton zone_toggle = (ToggleButton) zone_info_dialog.findViewById(R.id.btn_activate_zone);

                zone_remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CommonVariables.dbFacade.deleteZone(CommonVariables.selectedGPS, mZoneList.get(position));
                        mZoneList.remove(position);
                        mZoneAdapter.notifyDataSetChanged();
                        zone_info_dialog.dismiss();
                    }
                });

                zone_toggle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mZoneList.get(position).setActive(!mZoneList.get(position).isActive());
                        zone_toggle.setChecked(mZoneList.get(position).isActive());
                        CommonVariables.dbFacade.modifyZone(CommonVariables.selectedGPS, mZoneList.get(position));
                        mZoneAdapter.notifyDataSetChanged();
                        zone_info_dialog.dismiss();
                    }
                });
                zone_info_dialog.show();
            }
        });
    }

}
