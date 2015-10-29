package equipe12.log330.developpement.log330_lab4.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.LinkedList;

import equipe12.log330.developpement.log330_lab4.R;
import equipe12.log330.developpement.log330_lab4.database.DbFacade;
import equipe12.log330.developpement.log330_lab4.model.GPS;
import equipe12.log330.developpement.log330_lab4.model.Zone;
import equipe12.log330.developpement.log330_lab4.utility.CommonVariables;

public class ZoneListActivity extends Activity {
    ListView listView ;
    //private ZoneListAdapter adapter;
    private DbFacade dbFacade;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone_list);

        listView = (ListView) findViewById(R.id.list);
        dbFacade = CommonVariables.dbFacade;
        LinkedList<GPS> gpsList = dbFacade.getGps(CommonVariables.user);
        LinkedList<Zone> zoneListGlobal = new LinkedList<>();
        LinkedList<Zone> listZoneTempo = new LinkedList<>();
        ArrayList<Zone> zoneList = new ArrayList<>();
        String[] tableau;
        for( int i=0; i < gpsList.size(); i++){

            listZoneTempo = dbFacade.getZones(gpsList.get(i));

            for( int j=0; j < listZoneTempo.size(); j++){
                zoneListGlobal.add(listZoneTempo.get(j));
            }
        }
        tableau = new String[zoneListGlobal.size()];
        for( int i=0; i < zoneListGlobal.size(); i++){
            zoneList.add(zoneListGlobal.get(i));
            tableau[i]= String.format("%d %s", (zoneListGlobal.get(i)).getId(), (zoneListGlobal.get(i)).getName().toString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, tableau);

        listView.setAdapter(adapter);
    }

}
