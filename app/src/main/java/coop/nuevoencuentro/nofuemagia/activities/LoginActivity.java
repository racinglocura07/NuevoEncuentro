package coop.nuevoencuentro.nofuemagia.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import coop.nuevoencuentro.nofuemagia.R;
import coop.nuevoencuentro.nofuemagia.helper.Common;

/**
 * Created by jlionti on 10/06/2016. No Fue Magia
 */
public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private ProgressBar progress;
    private LoginButton loginButton;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences(Common.PREFERENCES, MODE_PRIVATE);
        preferences.edit()
                .remove(Common.EMAIL)
                .remove(Common.NOMBRE)
                .remove(Common.FBID)
                .remove(Common.FB_REG)
                .remove(Common.PRIMER_NOMBRE).apply();

        if (preferences.getBoolean(Common.VER_TOUR_LOGIN, true)) {
           // MostrarTour();
        }

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);

            ActionBar actionBar = getActionBar();
            if (actionBar != null)
                actionBar.hide();
        }


        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        progress = (ProgressBar) findViewById(R.id.progress_fb);
        progress.setVisibility(View.GONE);

        assert loginButton != null;

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                progress.setVisibility(View.VISIBLE);
                loginButton.setEnabled(false);

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String email = object.getString("email");
                            String name = object.getString("name");
                            String id = object.getString("id");
                            String primerNombre = object.getString("first_name");

                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean(Common.FB_REG, true);
                            editor.putString(Common.EMAIL, email);
                            editor.putString(Common.NOMBRE, name);
                            editor.putString(Common.PRIMER_NOMBRE, primerNombre);
                            editor.putString(Common.FBID, id);
                            editor.apply();

                            loginButton.setEnabled(true);
                            progress.setVisibility(View.GONE);
                            Intent principal = new Intent(LoginActivity.this, PantallaPrincipal.class);
                            startActivity(principal);
                            finish();

                        } catch (JSONException e) {
                            System.out.println(e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,first_name");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                progress.setVisibility(View.GONE);
                loginButton.setEnabled(true);
            }

            @Override
            public void onError(FacebookException exception) {
                progress.setVisibility(View.GONE);
                loginButton.setEnabled(true);
            }
        });
    }

    private void MostrarTour() {
        ViewTarget target = new ViewTarget(R.id.login_button, this);
        new ShowcaseView.Builder(this)
                .setTarget(target)
                .setContentTitle("ShowcaseView")
                .setContentText("This is highlighting the Home button")
                .hideOnTouchOutside()
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);

            ActionBar actionBar = getActionBar();
            if (actionBar != null)
                actionBar.hide();
        }
    }
}
