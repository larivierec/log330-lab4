package equipe12.log330.developpement.log330_lab4.model;

/**
 * Created by Chris on 10/7/2015.
 */
public abstract class GPSInfo {
    private Integer mGPSID;
    private String mGPSName;

    protected Integer getmGPSID() {
        return mGPSID;
    }

    protected void setmGPSID(Integer mGPSID) {
        this.mGPSID = mGPSID;
    }

    protected String getmGPSName() {
        return mGPSName;
    }

    protected void setmGPSName(String mGPSName) {
        this.mGPSName = mGPSName;
    }
}