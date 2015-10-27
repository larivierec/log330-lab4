package equipe12.log330.developpement.log330_lab4.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Chris on 10/7/2015.
 */
public class GPS extends GPSInfo implements Serializable{
    public GPS(String id, String name, Bitmap picture){
        super.setGPSID(id);
        super.setGPSName(name);
        if(picture != null)
            super.setAssignedPicture(picture);
    }
    /*
    protected GPS(Parcel in) {
    }

    public static final Creator<GPS> CREATOR = new Creator<GPS>() {
        @Override
        public GPS createFromParcel(Parcel in) {
            return new GPS(in);
        }

        @Override
        public GPS[] newArray(int size) {
            return new GPS[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(super.getGPSID());
        dest.writeString(super.getGPSName());
    }*/
}
