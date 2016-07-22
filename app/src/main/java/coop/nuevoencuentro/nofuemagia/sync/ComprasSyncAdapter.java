package coop.nuevoencuentro.nofuemagia.sync;

import android.accounts.Account;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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


import coop.nuevoencuentro.nofuemagia.activities.PantallaPrincipal;
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




        SharedPreferences preferences = getContext().getSharedPreferences(Common.PREFERENCES, Context.MODE_PRIVATE);

        SyncHttpClient client = new SyncHttpClient();

        String que = extras.getString("QUE");

        Intent intent = new Intent(getContext(), PantallaPrincipal.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Common.ABRIR_DONDE, que);

        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (que != null && que.equals("Actividades")) {
            Common.SincronizarActividades(client, syncResult);
            if (preferences.getBoolean(Common.RECIBIR_ACTIVIDAD, false))
                Common.sendNotification(getContext(), "Hay actividades nuevas!", "No te pierdas las ultimas novedades", pendingIntent);
        } else if (que != null && que.equals("Bolson")) {
            Common.SincronizarBolsones(client, syncResult);
            if (preferences.getBoolean(Common.RECIBIR_BOLSON, true))
                Common.sendNotification(getContext(), "Nuevo formulario de compra!", "Desde hoy podes hacer tu pedido!", pendingIntent);
        } else if (que != null && que.equals("Noticias")) {
            Common.SincronizarNoticias(client, syncResult);
            if (preferences.getBoolean(Common.RECIBIR_NOTICIA, false))
                Common.sendNotification(getContext(), "Siempre al dia!", "Mantenete al tanto de las ultimas noticias", pendingIntent);
        }

        System.out.println("Stast:");
        System.out.println(syncResult.stats);
    }


}