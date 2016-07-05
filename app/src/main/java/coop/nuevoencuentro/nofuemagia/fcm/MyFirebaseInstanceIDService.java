package coop.nuevoencuentro.nofuemagia.fcm;

import android.content.SharedPreferences;

import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by jlionti on 14/06/2016. No Fue Magia
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        SharedPreferences preferences = getSharedPreferences(Util.PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Util.YA_REGISTRADO, false);
        editor.apply();
    }
}
