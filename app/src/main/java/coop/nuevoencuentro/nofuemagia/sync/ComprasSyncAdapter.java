package coop.nuevoencuentro.nofuemagia.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;


import coop.nuevoencuentro.nofuemagia.helper.Common;
import coop.nuevoencuentro.nofuemagia.model.Bolsones;
import cz.msebera.android.httpclient.Header;
import coop.nuevoencuentro.nofuemagia.helper.DatabaseHelper;
import coop.nuevoencuentro.nofuemagia.model.Actividades;
import coop.nuevoencuentro.nofuemagia.model.Caracteristica;

/**
 * Created by jlionti on 06/06/2016. No Fue Magia
 */
public class ComprasSyncAdapter extends AbstractThreadedSyncAdapter {

    private DatabaseHelper dbHelper;

    public ComprasSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        System.out.println("Sincronizando, Con errores? " + syncResult.hasError());


        SyncHttpClient client = new SyncHttpClient();

        String que = extras.getString("QUE");
        if (que != null && que.equals("Actividades")) {
            Common.SincronizarActividades(client, syncResult);
        } else if (que != null && que.equals("Bolson")) {
            Common.SincronizarBolsones(client, syncResult);
        } else if (que != null && que.equals("Noticias")) {
            Common.SincronizarNoticias(client, syncResult);
        }

        System.out.println("Stast:");
        System.out.println(syncResult.stats);
    }
}