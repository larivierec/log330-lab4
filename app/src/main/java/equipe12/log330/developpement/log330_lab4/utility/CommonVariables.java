package equipe12.log330.developpement.log330_lab4.utility;

import android.content.Context;

import equipe12.log330.developpement.log330_lab4.database.DbFacade;
import equipe12.log330.developpement.log330_lab4.event.LocationEvent;
import equipe12.log330.developpement.log330_lab4.model.GPS;
import equipe12.log330.developpement.log330_lab4.model.User;

/**
 * Created by serge on 2015-10-16.
 */
public class CommonVariables {

    public static User user;
    public static Context context;
    public static GPS selectedGPS;
    public static DbFacade dbFacade;

    public static LocationEvent locationEvent;

}
