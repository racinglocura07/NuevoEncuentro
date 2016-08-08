package coop.nuevoencuentro.nofuemagia.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.View;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import coop.nuevoencuentro.nofuemagia.R;
import coop.nuevoencuentro.nofuemagia.activities.PantallaPrincipal;
import coop.nuevoencuentro.nofuemagia.fragments.ActividadesFragment;
import coop.nuevoencuentro.nofuemagia.fragments.ComprasComunitariasFragment;
import coop.nuevoencuentro.nofuemagia.fragments.NoticiasImagenFragment;
import coop.nuevoencuentro.nofuemagia.fragments.TalleresFragment;
import coop.nuevoencuentro.nofuemagia.model.Actividades;
import coop.nuevoencuentro.nofuemagia.model.Bolsones;
import coop.nuevoencuentro.nofuemagia.model.Noticias;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Tano on 16/06/2016.
 * Nuevo Encuentro
 * No Fue Magia
 */
public class Common {

    public static final String PREFERENCES = "NOFUEMAGIA_NUEVOENCUENTRO";
    public static final String YA_REGISTRADO = "YA_REGISTRADO";

    public static final String PLAY_URL = "https://play.google.com/store/apps/details?id=coop.nuevoencuentro&hl=es";
    public static final String EMAIL = "EMAIL";
    public static final String NOMBRE = "NOMBRE";
    public static final String FBID = "FBID";
    public static final String FB_REG = "FB_REG";
    public static final String PRIMER_NOMBRE = "PRIMER_NOMBRE";
    public static final String VER_TOUR_LOGIN = "VER_TOUR_LOGIN";
    public static final String ES_FB = "ES_FB";
    public static final String RECIBIR_NOTICIA = "RECIBIR_NOTICIA";
    public static final String RECIBIR_ACTIVIDAD = "RECIBIR_ACTIVIDAD";
    public static final String RECIBIR_BOLSON = "RECIBIR_BOLSON";
    public static final String RECIBIR_MENSAJES = "RECIBIR_MENSAJES";

    public static final String ABRIR_DONDE = "ABRIR_DONDE";
    public static final String NOTICIAS = "Noticias";
    public static final String ACTIVIDADES = "Actividades";
    public static final String TALLERES = "Talleres";
    public static final String BOLSONES = "Bolson";
    public static final String CONTACTO = "Contacto";
    public static final String ULTIMA = "ULTIMA";
    public static final String MICOMUNA = "MiComuna";

    private static final int ID_NOTIF = 0x2207;

    public static final String MAIN_URL = "http://nofuemagia.ueuo.com/Nuevo/";

    public static final String ESADMIN_URL = MAIN_URL + "backend/usuarios/esAdmin.php";


    public static final String REGISTRAR_URL = MAIN_URL + "backend/usuarios/crearUsuario.php";

    public static final String imagenURL = MAIN_URL + "imagenes/";

    public static final String urlActividades = MAIN_URL + "backend/actividades/listActividades.php?fid=-2";
    public static final String urlBolsones = MAIN_URL + "backend/bolsones/listBolsones.php?fid=-2";
    private static final String urlNoticias = MAIN_URL + "backend/noticias/listNoticias.php?fid=-1";

    public static final String NUESTAS_VOCES = "http://www.nuestrasvoces.com.ar/feed/";
    public static final String PAGINA_12 = "http://www.pagina12.com.ar/diario/rss/principal.xml";
    public static final String PAGINA_12_ULTIMAS = "http://www.pagina12.com.ar/diario/rss/ultimas_noticias.xml";


    public static final String AGREGARACTIVIDAD = MAIN_URL + "backend/actividades/crearActividad.php";
    public static final String AGREGARNOTICIA = MAIN_URL + "backend/noticias/crearNoticia.php";
    public static final String AGREGARBOLSON = MAIN_URL + "backend/bolsones/crearBolson.php";


    public static void ShowOkMessage(View v, int mensaje) {
        final Snackbar snackBar = Snackbar.make(v, mensaje, Snackbar.LENGTH_INDEFINITE);
        snackBar.setAction(android.R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackBar.dismiss();
            }
        });
        snackBar.show();
    }

    public static void ShowMessage(View v, int mensaje) {
        final Snackbar snackBar = Snackbar.make(v, mensaje, Snackbar.LENGTH_SHORT);
        snackBar.show();
    }

    public static void SincronizarBolsones(final Context mContext, SyncHttpClient client, final SyncResult result) {
        client.get(urlBolsones, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                guadarBolsones(response, result);

                Intent intent = new Intent(mContext, PantallaPrincipal.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Common.ABRIR_DONDE, BOLSONES);

                PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                SharedPreferences preferences = mContext.getSharedPreferences(Common.PREFERENCES, Context.MODE_PRIVATE);
                if (preferences.getBoolean(Common.RECIBIR_BOLSON, true))
                    Common.sendNotification(mContext, mContext.getString(R.string.titulo_notif_bolson), mContext.getString(R.string.desc_notif_bolson), pendingIntent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public static void SincronizarBolsones(final ComprasComunitariasFragment frag) {
        AsyncHttpClient client = ((PantallaPrincipal)frag.getActivity()).GetAsynk();
        client.get(urlBolsones, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                guadarBolsones(response, null);
                frag.recargar();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private static void guadarBolsones(JSONObject response, SyncResult result) {
        ActiveAndroid.beginTransaction();
        try {
            Bolsones bolson = new Bolsones();
            bolson.setIdBolson(response.getInt("idBolson"));
            bolson.setLink(response.getString("link"));
            bolson.setCreadoEl(response.getString("creadoEl"));
            bolson.save();

            if (result != null)
                result.stats.numInserts++;

            ActiveAndroid.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            if (result != null)
                result.stats.numIoExceptions++;
            System.out.println(e.getMessage());
        } finally {
            System.out.println("Bien bolson");
            ActiveAndroid.endTransaction();
        }
    }

    public static void SincronizarActividades(final Context mContext, SyncHttpClient client, final SyncResult result) {
        client.get(urlActividades, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                guardarActividades(response, result);

                Intent intent = new Intent(mContext, PantallaPrincipal.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Common.ABRIR_DONDE, ACTIVIDADES);

                PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                SharedPreferences preferences = mContext.getSharedPreferences(Common.PREFERENCES, Context.MODE_PRIVATE);
                if (preferences.getBoolean(Common.RECIBIR_ACTIVIDAD, false))
                    Common.sendNotification(mContext, mContext.getString(R.string.titulo_notif_actividades), mContext.getString(R.string.desc_notif_actividades), pendingIntent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            }
        });
    }

    public static void SincronizarActividades(final Fragment frag) {
        AsyncHttpClient client = ((PantallaPrincipal)frag.getActivity()).GetAsynk();
        client.get(urlActividades, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                guardarActividades(response, null);
                if (frag instanceof ActividadesFragment)
                    ((ActividadesFragment) frag).recargar();
                else if (frag instanceof TalleresFragment)
                    ((TalleresFragment) frag).recargar();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            }
        });
    }

    private static void guardarActividades(JSONArray response, SyncResult result) {
        ActiveAndroid.beginTransaction();
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
            builder.excludeFieldsWithoutExposeAnnotation();

            Type listType = new TypeToken<List<Actividades>>() {
            }.getType();

            for (Actividades local : Actividades.GetAll()) {
                if (result != null)
                    result.stats.numDeletes++;
                new Delete().from(Actividades.class).where("idActividad = ?", local.getId()).execute();
            }

            List<Actividades> remoto = builder.create().fromJson(response.toString(), listType);
            for (Actividades carRemoto : remoto) {
                if (result != null)
                    result.stats.numInserts++;
                Actividades nueva = new Actividades(carRemoto.idActividad, carRemoto.nombre, carRemoto.descripcion, carRemoto.cuando, carRemoto.repeticion, carRemoto.esTaller);
                nueva.save();
            }

            ActiveAndroid.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            if (result != null)
                result.stats.numIoExceptions++;
        } finally {
            sendNotification(null, null, null, null);
            System.out.println("Bien actividad");
            ActiveAndroid.endTransaction();
        }
    }

    public static void SincronizarNoticias(final Context mContext, SyncHttpClient client, final SyncResult result) {
        client.get(urlNoticias, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                guardarNoticias(response, result);

                Intent intent = new Intent(mContext, PantallaPrincipal.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Common.ABRIR_DONDE, NOTICIAS);

                PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                SharedPreferences preferences = mContext.getSharedPreferences(Common.PREFERENCES, Context.MODE_PRIVATE);
                if (preferences.getBoolean(Common.RECIBIR_NOTICIA, false))
                    Common.sendNotification(mContext, mContext.getString(R.string.titulo_notif_noticias), mContext.getString(R.string.desc_notif_noticias), pendingIntent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

            }
        });
    }

    public static void SincronizarNoticias(final NoticiasImagenFragment frag) {
        AsyncHttpClient client = ((PantallaPrincipal)frag.getActivity()).GetAsynk();
        client.get(urlNoticias, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                guardarNoticias(response, null);
                frag.recargar();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

            }

        });
    }

    private static void guardarNoticias(JSONArray response, SyncResult result) {
        ActiveAndroid.beginTransaction();
        try {

            GsonBuilder builder = new GsonBuilder();
            builder.excludeFieldsWithoutExposeAnnotation();

            Type listType = new TypeToken<List<Noticias>>() {
            }.getType();


            for (Noticias local : Noticias.GetAll()) {
                if (result != null)
                    result.stats.numDeletes++;
                new Delete().from(Noticias.class).where("idNoticia = ?", local.getId()).execute();
            }

            List<Noticias> remoto = builder.create().fromJson(response.toString(), listType);
            for (Noticias carRemoto : remoto) {
                if (result != null)
                    result.stats.numInserts++;
                Noticias nueva = new Noticias(carRemoto.idNoticia, carRemoto.titulo, carRemoto.descripcion, carRemoto.link);
                nueva.save();
            }

            ActiveAndroid.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            if (result != null)
                result.stats.numIoExceptions++;
            System.out.println(e.getMessage());
        } finally {
            System.out.println("Bien Noticias");
            ActiveAndroid.endTransaction();
        }
    }

    public static ArrayList<String> mensajes = new ArrayList<>();

    public static void sendNotification(Context context, String titulo, String msg, PendingIntent pendingIntent) {

        if (context == null && titulo == null && msg == null && pendingIntent == null) {
            mensajes.clear();
            return;
        }

        assert titulo != null;
        assert context != null;

        if (titulo.equals("Pruebas")) {
            mensajes.add(msg);
            msg = TextUtils.join("\n", mensajes);
        } else {
            mensajes.clear();
        }

        SharedPreferences preferences = context.getSharedPreferences(Common.PREFERENCES, Context.MODE_PRIVATE);
        if (!preferences.getBoolean(YA_REGISTRADO, false)) {
            return;
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notif)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setContentTitle(titulo)
                .setTicker(titulo)
                .setContentText(msg)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(msg))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID_NOTIF, notificationBuilder.build());
    }
}
