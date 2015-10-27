package equipe12.log330.developpement.log330_lab4.view;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import equipe12.log330.developpement.log330_lab4.R;
import equipe12.log330.developpement.log330_lab4.database.DbFacade;
import equipe12.log330.developpement.log330_lab4.event.LocationEvent;
import equipe12.log330.developpement.log330_lab4.model.User;
import equipe12.log330.developpement.log330_lab4.utility.CommonVariables;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    // UI references.
    private EditText mUserView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        CommonVariables.context = getApplicationContext();
        CommonVariables.dbFacade = new DbFacade(getApplicationContext());
        CommonVariables.locationEvent = new LocationEvent();

        // Set up the login form.
        mUserView = (EditText) findViewById(R.id.userTxt);

        mPasswordView = (EditText) findViewById(R.id.passwordTxt);

        Button mSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DbFacade f = CommonVariables.dbFacade;
                User u = f.isValidUser(mUserView.getText().toString(), mPasswordView.getText().toString());
                if(u == null) {
                    u = f.addUser(mUserView.getText().toString(), mPasswordView.getText().toString());
                }
                if(u != null) {
                    CommonVariables.user = u;
                    ((TextView) findViewById(R.id.txtLoginError)).setVisibility(View.GONE);
                    Intent myIntent = new Intent(LoginActivity.this,
                            GPSActivity.class);
                    startActivity(myIntent);
                } else {
                    ((TextView) findViewById(R.id.txtLoginError)).setVisibility(View.VISIBLE);
                }
            }
        });
    }

}