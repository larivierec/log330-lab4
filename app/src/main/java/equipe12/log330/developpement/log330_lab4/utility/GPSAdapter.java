package equipe12.log330.developpement.log330_lab4.utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.LinkedList;

import equipe12.log330.developpement.log330_lab4.R;
import equipe12.log330.developpement.log330_lab4.model.GPS;

/**
 * Created by Chris on 10/7/2015.
 */
public class GPSAdapter extends ArrayAdapter<GPS> {

    private LinkedList<GPS> mGPSItems;

    public GPSAdapter(Context ctx, LinkedList<GPS> items){
        super(ctx, R.layout.gps_listview_layout,items);
        this.mGPSItems = items;
    }

    public GPSAdapter(Context context, int resource, LinkedList<GPS> items) {
        super(context, resource, items);
        this.mGPSItems = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        GPSViewHolder vh = null;

        if(row == null){
            LayoutInflater gpsInflater = LayoutInflater.from(getContext());
            row = gpsInflater.inflate(R.layout.gps_listview_layout, parent, false);
            vh = new GPSViewHolder(row);
            row.setTag(vh);
        }else{
            vh = (GPSViewHolder) row.getTag();
        }

        vh.theText.setText(mGPSItems.get(position).getGPSName());

        return row;
    }

    //Google ViewHolder pattern
    class GPSViewHolder{
        TextView theText;
        ImageView theImage;

        public GPSViewHolder(View v) {
            theText = (TextView) v.findViewById(R.id.txt_gps_name_row);
            theImage = (ImageView) v.findViewById(R.id.lv_gps_image_row);
        }
    }
}
