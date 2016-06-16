package nofuemagia.nuevoencuentro.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by jlionti on 06/06/2016. No Fue Magia
 */
public class ComprasAuthService extends Service {

    private ComprasAuth mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new ComprasAuth(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}