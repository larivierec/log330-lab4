package equipe12.log330.developpement.log330_lab4.model;

import android.graphics.Bitmap;

/**
 * Created by serge on 2015-10-16.
 */
public class User {

    private long id;
    private String username;
    private String password;

    public User(long id, String username) {
        this.id = id;
        this.username = username;
        this.password = "";
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
