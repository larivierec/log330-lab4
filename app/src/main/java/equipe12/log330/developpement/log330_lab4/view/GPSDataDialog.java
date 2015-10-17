package equipe12.log330.developpement.log330_lab4.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.LinkedList;

import equipe12.log330.developpement.log330_lab4.R;
import equipe12.log330.developpement.log330_lab4.interfaces.DialogGPSAccepted;


/**
 * Created by Chris on 10/16/2015.
 */
public class GPSDataDialog extends Activity {

    private LinkedList<DialogGPSAccepted> listeners;

    public GPSDataDialog(){
        listeners = new LinkedList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_gps_layout);
        setTitle("Add a GPS");

        final Button dialogAdd = (Button) findViewById(R.id.btn_add_gps_data);
        final Button dialogCancel = (Button) findViewById(R.id.btn_cancel_gps_data);
        final Button imgPicker = (Button) findViewById(R.id.btn_image_pick);

        final EditText view_gpsname = (EditText) findViewById(R.id.txt_gps_name);
        final EditText view_gpsid = (EditText) findViewById(R.id.txt_gps_id);
        final ImageView view_image = (ImageView) findViewById(R.id.img_view_dialog);

        imgPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                startActivityForResult(photoPicker, 1000);
            }
        });

        dialogAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gpsname = view_gpsname.getText().toString();
                String gpsid = view_gpsid.getText().toString();

                notifyListeners(gpsname, gpsid, null);
                finish();
            }
        });

        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void addListener(DialogGPSAccepted futureListener){
        this.listeners.push(futureListener);
    }

    private void notifyListeners(String gpsname, String gpsid, String view){
        for(DialogGPSAccepted listener : listeners){
            listener.onDialogButtonAdded(gpsname, gpsid, view);
        }
    }
}
