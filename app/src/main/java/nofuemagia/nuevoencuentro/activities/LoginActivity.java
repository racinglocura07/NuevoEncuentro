package nofuemagia.nuevoencuentro.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import nofuemagia.nuevoencuentro.R;
import nofuemagia.nuevoencuentro.fcm.Util;

/**
 * Created by jlionti on 10/06/2016. No Fue Magia
 */
public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        SharedPreferences preferences = getSharedPreferences(Util.PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Util.YA_REGISTRADO, false);
        editor.apply();

//        PackageInfo info;
//        try {
//            info = getPackageManager().getPackageInfo("nofuemagia.nuevoencuentro", PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md;
//                md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String something = new String(Base64.encode(md.digest(), 0));
//                //String something = new String(Base64.encodeBytes(md.digest()));
//                Log.e("hash key", something);
//            }
//        } catch (PackageManager.NameNotFoundException e1) {
//            Log.e("name not found", e1.toString());
//        } catch (NoSuchAlgorithmException e) {
//            Log.e("no such an algorithm", e.toString());
//        } catch (Exception e) {
//            Log.e("exception", e.toString());
//        }


        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        assert loginButton != null;
        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String email = object.getString("email");

                            SharedPreferences preferences = getSharedPreferences(Util.PREFERENCES, MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString(Util.EMAIL, email);
                            editor.apply();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,email,gender,birthday,location");
                request.setParameters(parameters);
                request.executeAsync();

                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onCancel() {
                System.out.println("Cancelado");
                setResult(RESULT_CANCELED);
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("Error - " + exception.getMessage());
                setResult(RESULT_CANCELED);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
