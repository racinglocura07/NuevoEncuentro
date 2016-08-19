package coop.nuevoencuentro.nofuemagia.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.loopj.android.http.SyncHttpClient;


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
        SyncHttpClient client = new SyncHttpClient();

        String que = extras.getString("QUE");
        if (que != null && que.equals(Common.ACTIVIDADES)) {
            Common.SincronizarActividades(getContext(), client, syncResult);
        } else if (que != null && que.equals(Common.BOLSONES)) {
            Common.SincronizarBolsones(getContext(), client, syncResult);
        } else if (que != null && que.equals(Common.NOTICIAS)) {
            Common.SincronizarNoticias(getContext(), client, syncResult);
        }
    }


}