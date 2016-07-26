package coop.nuevoencuentro.nofuemagia;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

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
    }
}