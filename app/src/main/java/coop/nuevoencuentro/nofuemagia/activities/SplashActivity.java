package coop.nuevoencuentro.nofuemagia.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import coop.nuevoencuentro.nofuemagia.helper.Common;

/**
 * Created by Tano on 24/07/2016.
 * Nuevo Encuentro
 * No Fue Magia
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Arrancar();
    }

    private void Arrancar() {
        Intent siguiente = new Intent(this, PantallaPrincipal.class);

        SharedPreferences preferences = getSharedPreferences(Common.PREFERENCES, MODE_PRIVATE);
        if (!preferences.getBoolean(Common.FB_REG, false))
            siguiente = new Intent(this, LoginActivity.class);


        startActivity(siguiente);
        finish();
    }
}
