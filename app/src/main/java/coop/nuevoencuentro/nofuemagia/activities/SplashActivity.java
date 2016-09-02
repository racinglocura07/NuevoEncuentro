package coop.nuevoencuentro.nofuemagia.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import coop.nuevoencuentro.nofuemagia.R;
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

    //    @Override
//    public void initSplash(ConfigSplash configSplash) {
//
//        ActionBar abar = getSupportActionBar();
//        if (abar != null)
//            abar.hide();
//
//        configSplash.setBackgroundColor(R.color.partido_medio); //any color you want form colors.xml
//        configSplash.setAnimCircularRevealDuration(500); //int ms
//        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
//        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP
//
//        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default
//
//        //Customize Logo
//        configSplash.setLogoSplash(R.mipmap.splash); //or any other drawable
////        configSplash.set
//        configSplash.setAnimLogoSplashDuration(1000); //int ms
//        configSplash.setAnimLogoSplashTechnique(Techniques.Bounce); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)
//
////
////        //Customize Path
////        configSplash.setPathSplash(Constants.DROID_LOGO); //set path String
////        configSplash.setOriginalHeight(400); //in relation to your svg (path) resource
////        configSplash.setOriginalWidth(400); //in relation to your svg (path) resource
////        configSplash.setAnimPathStrokeDrawingDuration(3000);
////        configSplash.setPathSplashStrokeSize(3); //I advise value be <5
////        configSplash.setPathSplashStrokeColor(R.color.colorAccent); //any color you want form colors.xml
////        configSplash.setAnimPathFillingDuration(3000);
////        configSplash.setPathSplashFillColor(R.color.Wheat); //path object filling color
//
//
//        //Customize Title
//        configSplash.setTitleSplash("Nuevo Encuentro");
//        configSplash.setTitleTextColor(R.color.Wheat);
//        configSplash.setTitleTextSize(30f); //float value
//        configSplash.setAnimTitleDuration(1500);
//        configSplash.setAnimTitleTechnique(Techniques.FlipInX);
//    }
//
//    @Override
//    public void animationsFinished() {
//        Arrancar();
//    }

    private void Arrancar() {
        Intent siguiente = new Intent(this, PantallaPrincipal2.class);

        SharedPreferences preferences = getSharedPreferences(Common.PREFERENCES, MODE_PRIVATE);
        if (!preferences.getBoolean(Common.FB_REG, false))
            siguiente = new Intent(this, LoginActivity.class);


        startActivity(siguiente);
        finish();
    }
}
