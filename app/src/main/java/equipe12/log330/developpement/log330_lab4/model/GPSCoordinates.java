package equipe12.log330.developpement.log330_lab4.model;

import com.orm.SugarRecord;

/**
 * Created by Chris on 10/14/2015.
 */

public class GPSCoordinates extends SugarRecord<GPSCoordinates> {
    private String mLongitude;
    private String mLatitude;

    public GPSCoordinates(){}
    public GPSCoordinates(Object longitude, Object latitude){
        if(longitude instanceof Long){
            mLongitude = longitude.toString();
        }else if(longitude instanceof Double){
            mLongitude = ((Double) longitude).toString();
        }

        if(latitude instanceof Long){
            mLongitude = latitude.toString();
        }else if(latitude instanceof Double){
            mLongitude = ((Double) latitude).toString();
        }
    }

    public String getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(String mLongitude) {
        this.mLongitude = mLongitude;
    }
}
