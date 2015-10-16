package equipe12.log330.developpement.log330_lab4.model;

/**
 * Created by Chris on 10/7/2015.
 */
public class GPS extends GPSInfo {
    public GPS(String id, String name, String picture){
        super.setGPSID(id);
        super.setGPSName(name);
        if(picture != null)
            super.setAssignedPicture(picture);
    }
}
