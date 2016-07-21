package coop.nuevoencuentro.nofuemagia.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.widget.IconButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import coop.nuevoencuentro.nofuemagia.R;
import coop.nuevoencuentro.nofuemagia.helper.Common;

/**
 * Created by jlionti on 10/06/2016. No Fue Magia
 */
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 0x01;
    private CallbackManager callbackManager;
    private ProgressBar progress;
    private LoginButton loginButton;

    private SharedPreferences preferences;
    private SignInButton signInButton;
    private GoogleApiClient mGoogleApiClient;
    private IconButton ibInvitado;

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
                .remove(Common.YA_REGISTRADO)
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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        callbackManager = CallbackManager.Factory.create();

        ibInvitado = (IconButton) findViewById(R.id.ib_invidato);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        signInButton = (SignInButton) findViewById(R.id.sb_google);

        progress = (ProgressBar) findViewById(R.id.progress_fb);
        progress.setVisibility(View.GONE);

        assert loginButton != null;

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        ibInvitado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comoInvitado();
            }
        });

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                updateUI(true);

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String email = object.getString("email");
                            String name = object.getString("name");
                            String id = object.getString("id");
                            String primerNombre = object.getString("first_name");

                            SavePreferences(email, name, id, primerNombre);
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
                updateUI(false);
            }

            @Override
            public void onError(FacebookException exception) {
                updateUI(false);
            }
        });
    }

    private void comoInvitado() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_nombre, null);
        final EditText edt = (EditText) dialogView.findViewById(R.id.et_nombre);

        new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
                .setTitle(R.string.invitado_ingreso)
                .setCancelable(true)
                .setIcon(new IconDrawable(this, FontAwesomeIcons.fa_info).actionBarSize().color(R.color.colorAccent))
                .setView(dialogView)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String nombre = edt.getText().toString();
                        String deviceId = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
                        if (!TextUtils.isEmpty(nombre)) {
                            SavePreferences(getEmiailID(getApplicationContext()), nombre, deviceId, nombre);
                        }
                    }
                })
                .show();
    }

    private void SavePreferences(String email, String name, String id, String primerNombre) {
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
    }

//    private void MostrarTour() {
//        ViewTarget target = new ViewTarget(R.id.login_button, this);
//        new ShowcaseView.Builder(this)
//                .setTarget(target)
//                .setContentTitle("ShowcaseView")
//                .setContentText("This is highlighting the Home button")
//                .hideOnTouchOutside()
//                .build();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {

                GoogleSignInAccount acct = result.getSignInAccount();
                if (acct != null) {
                    String email = acct.getEmail();
                    String name = acct.getDisplayName();
                    String id = acct.getIdToken();
                    String primerNombre = acct.getGivenName();

                    SavePreferences(email, name, id, primerNombre);
                }


                updateUI(true);
            } else {
                System.out.println("Error = " + result.getStatus().toString());
                updateUI(false);
            }

        } else
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void updateUI(boolean logueado) {
        if (logueado) {
            progress.setVisibility(View.VISIBLE);
            loginButton.setEnabled(false);
            ibInvitado.setEnabled(false);
        } else {
            progress.setVisibility(View.GONE);
            loginButton.setEnabled(true);
            ibInvitado.setEnabled(true);
        }
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println("Error = " + connectionResult.toString());
    }

    private String getEmiailID(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getAccount(accountManager);
        if (account == null) {
            return null;
        } else {
            return account.name;
        }
    }

    private static Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        return account;
    }
}
