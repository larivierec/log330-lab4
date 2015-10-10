package equipe12.log330.developpement.log330_lab4.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.TextView;

import equipe12.log330.developpement.log330_lab4.R;
import equipe12.log330.developpement.log330_lab4.interfaces.DialogGPSAccepted;

/**
 * Created by Chris on 10/9/2015.
 */
public class DialogGPS extends Activity {

    private AlertDialog.Builder mBuilder;
    private LayoutInflater mInflater;
    private AlertDialog mDialog;

    public DialogGPS(){}

    public DialogGPS(final DialogGPSAccepted listener){
        mBuilder = new AlertDialog.Builder(this);
        mInflater = getLayoutInflater();

        mBuilder.setMessage(R.string.add_gps_message_dialog)
                .setTitle("Add a GPS")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TextView view_gpsname = (TextView) findViewById(R.id.txt_gps_name);
                        TextView view_gpsid = (TextView) findViewById(R.id.txt_gps_id);
                        TextView view_gpstype = (TextView) findViewById(R.id.txt_gps_type);

                        /*listener.onDialogButtonAdded(view_gpsname.getText().toString(),
                                view_gpsid.getText().toString(),
                                view_gpstype.getText().toString());*/
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setView(mInflater.inflate(R.layout.dialog_gps_layout,null));

        mDialog = mBuilder.create();
        mDialog.show();
    }

}
