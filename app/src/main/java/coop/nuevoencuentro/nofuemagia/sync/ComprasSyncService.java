package coop.nuevoencuentro.nofuemagia.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by jlionti on 06/06/2016. No Fue Magia
 */
public class ComprasSyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static ComprasSyncAdapter sSyncAdapter = null;

    @Override
    public void onCreate() {
        super.onCreate();

        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new ComprasSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
