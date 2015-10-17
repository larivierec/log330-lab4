package equipe12.log330.developpement.log330_lab4.model;

import android.graphics.Bitmap;

/**
 * Created by Chris on 10/7/2015.
 */
public abstract class GPSInfo {

    private String mGPSID;
    private String mGPSName;
    private Bitmap mAssignedPicture;

    public Bitmap getAssignedPicture() {
        return mAssignedPicture;
    }

    protected void setAssignedPicture(Bitmap mAssignedPicture) {
        this.mAssignedPicture = mAssignedPicture;
    }

    public String getGPSID() {
        return mGPSID;
    }

    protected void setGPSID(String mGPSID) {
        this.mGPSID = mGPSID;
    }

    public String getGPSName() {
        return mGPSName;
    }

    protected void setGPSName(String mGPSName) {
        this.mGPSName = mGPSName;
    }
}
