package equipe12.log330.developpement.log330_lab4.utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.LinkedList;

import equipe12.log330.developpement.log330_lab4.R;
import equipe12.log330.developpement.log330_lab4.model.Zone;

/**
 * Created by Chris on 10/7/2015.
 */
public class ZoneAdapter extends ArrayAdapter<Zone> {

    private LinkedList<Zone> mZoneItems;

    public ZoneAdapter(Context ctx, LinkedList<Zone> items){
        super(ctx, R.layout.zone_list_view_item, items);
        this.mZoneItems = items;
    }

    public ZoneAdapter(Context context, int resource, LinkedList<Zone> items) {
        super(context, resource, items);
        this.mZoneItems = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ZoneViewHolder vh = null;

        if(row == null){
            LayoutInflater zoneInflater = LayoutInflater.from(getContext());
            row = zoneInflater.inflate(R.layout.zone_list_view_item, parent, false);
            vh = new ZoneViewHolder(row);
            row.setTag(vh);
        }else{
            vh = (ZoneViewHolder) row.getTag();
        }

        Zone zone = mZoneItems.get(position);
        vh.name.setText(zone.getName());
        vh.active.setText(zone.isActive() ? "Activé" : "Désactivé");

        return row;
    }

    class ZoneViewHolder{
        TextView name;
        TextView active;

        public ZoneViewHolder(View v) {
            name = (TextView) v.findViewById(R.id.txt_zone_name_row);
            active = (TextView) v.findViewById(R.id.txt_zone_active_row);
        }
    }
}
