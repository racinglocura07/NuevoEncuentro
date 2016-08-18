package coop.nuevoencuentro.nofuemagia;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.internal.network.GuestAuthenticator;
import com.twitter.sdk.android.tweetui.TweetUi;

import io.fabric.sdk.android.Fabric;

/**
 * Created by jlionti on 07/06/2016. No Fue Magia
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Iconify.with(new FontAwesomeModule());
        FacebookSdk.sdkInitialize(this);

        //Configuration dbConfiguration = new Configuration.Builder(this).setDatabaseName("Compras.db").create();
        ActiveAndroid.initialize(this);
        AppEventsLogger.activateApp(this);
        if (!Fabric.isInitialized()) {
            TwitterAuthConfig authConfig =  new TwitterAuthConfig("sPA7QHo4q8to6CZ9ucnlNM3Ql", "FkHjcDIxgIkmu41e2GNeGio0ibPWROuq9MfOORIK6Z8sCgRH10");
            Fabric.with(this, new TwitterCore(authConfig), new TweetUi());
        }
    }
}