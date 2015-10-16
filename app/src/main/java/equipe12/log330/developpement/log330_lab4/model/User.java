package equipe12.log330.developpement.log330_lab4.model;

import android.graphics.Bitmap;

/**
 * Created by serge on 2015-10-16.
 */
public class User {

    private int id;
    private String username;
    private String password;
    private Bitmap image;

    public User(int id, String username, String password, Bitmap image) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
