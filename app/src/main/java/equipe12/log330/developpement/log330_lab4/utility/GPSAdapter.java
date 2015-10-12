package equipe12.log330.developpement.log330_lab4.utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import equipe12.log330.developpement.log330_lab4.model.GPS;

/**
 * Created by Chris on 10/7/2015.
 */
public class GPSAdapter extends ArrayAdapter<GPS> {

    //TODO custom listviewe: http://www.androidhive.info/2012/02/android-custom-listview-with-image-and-text/

    public GPSAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public GPSAdapter(Context context, int resource, List<GPS> items) {
        super(context, resource, items);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;


        return null;
    }
}
