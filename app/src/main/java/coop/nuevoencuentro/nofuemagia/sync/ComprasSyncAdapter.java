package coop.nuevoencuentro.nofuemagia.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

//import com.loopj.android.http.SyncHttpClient;


import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

import coop.nuevoencuentro.nofuemagia.helper.Common;

/**
 * Created by jlionti on 06/06/2016. No Fue Magia
 */
public class ComprasSyncAdapter extends AbstractThreadedSyncAdapter {

    public ComprasSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Cache cache = new DiskBasedCache(getContext().getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        RequestQueue mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();

        String que = extras.getString("QUE");
        if (que != null && que.equals(Common.ACTIVIDADES)) {
            Common.SincronizarActividades(getContext(), mRequestQueue, syncResult);
        } else if (que != null && que.equals(Common.BOLSONES)) {
            Common.SincronizarBolsones(getContext(), mRequestQueue, syncResult);
        } else if (que != null && que.equals(Common.NOTICIAS)) {
            Common.SincronizarNoticias(getContext(), mRequestQueue, syncResult);
        }
    }


}