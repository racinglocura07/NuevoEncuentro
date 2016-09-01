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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
//import com.loopj.android.http.AsyncHttpClient;
//import com.loopj.android.http.JsonHttpResponseHandler;
//import com.loopj.android.http.SyncHttpClient;

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
import coop.nuevoencuentro.nofuemagia.xml.RSSItems;
//import cz.msebera.android.httpclient.Header;

/**
 * Created by Tano on 16/06/2016.
 * Nuevo Encuentro
 * No Fue Magia
 */
public class Common {

    public static final String PREFERENCES = "NOFUEMAGIA_NUEVOENCUENTRO";
    public static final String YA_REGISTRADO = "YA_REGISTRADO";

    public static final String PLAY_URL = "https://play.google.com/store/apps/details?id=coop.nuevoencuentro.nofuemagia&hl=es";
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
    public static final String ES_ADMIN = "ES_ADMIN";

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


    public static final String AGREGAR_ACTIVIDAD = MAIN_URL + "backend/actividades/crearActividad.php";
    public static final String EDITAR_ACTIVIDAD = MAIN_URL + "backend/actividades/editarActividad.php";
    public static final String BORRAR_ACTIVIDAD = MAIN_URL + "backend/actividades/borrarActividad.php";

    public static final String AGREGAR_NOTICIA = MAIN_URL + "backend/noticias/crearNoticia.php";
    public static final String EDITAR_NOTICIA = MAIN_URL + "backend/noticias/editarNoticia.php";

    public static final String AGREGARBOLSON = MAIN_URL + "backend/bolsones/crearBolson.php";

    public static final String ENVIARNOTIFICACION = MAIN_URL + "backend/enviar.php";

    public static final String TWITTER = "TWITTER";
    public static final String COMUNIDAD_BSAS = "http://comunidadbsas.com.ar/?feed=rss2";
    public static final String CONTENT_TYPE = "application/x-www-form-urlencoded; charset=UTF-8";


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

    public static void SincronizarBolsones(final Context mContext, RequestQueue req, final SyncResult result) {
        CustomRequest ultimas = new CustomRequest(Request.Method.POST, urlBolsones, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                guadarBolsones(response, result);

                Intent intent = new Intent(mContext, PantallaPrincipal.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Common.ABRIR_DONDE, BOLSONES);

                PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                SharedPreferences preferences = mContext.getSharedPreferences(Common.PREFERENCES, Context.MODE_PRIVATE);
                if (preferences.getBoolean(Common.RECIBIR_BOLSON, true))
                    Common.sendNotification(mContext, mContext.getString(R.string.titulo_notif_bolson), mContext.getString(R.string.desc_notif_bolson), pendingIntent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        req.add(ultimas);
    }

    public static void SincronizarBolsones(final ComprasComunitariasFragment frag) {
        RequestQueue mRequestQueue = ((PantallaPrincipal) frag.getActivity()).GetRequest();
        CustomRequest ultimas = new CustomRequest(Request.Method.POST, urlBolsones, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                guadarBolsones(response, null);
                frag.recargar();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(ultimas);
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
        } finally {
            System.out.println("Bien bolson");
            ActiveAndroid.endTransaction();
        }
    }

    public static void SincronizarActividades(final Context mContext, RequestQueue req, final SyncResult result) {
        JsonArrayRequest ultimas = new JsonArrayRequest(Request.Method.POST, urlActividades, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                guardarActividades(response, result);

                Intent intent = new Intent(mContext, PantallaPrincipal.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Common.ABRIR_DONDE, ACTIVIDADES);

                PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                SharedPreferences preferences = mContext.getSharedPreferences(Common.PREFERENCES, Context.MODE_PRIVATE);
                if (preferences.getBoolean(Common.RECIBIR_ACTIVIDAD, false))
                    Common.sendNotification(mContext, mContext.getString(R.string.titulo_notif_actividades), mContext.getString(R.string.desc_notif_actividades), pendingIntent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        req.add(ultimas);
    }

    public static void SincronizarActividades(final Fragment frag) {
        RequestQueue mRequestQueue = ((PantallaPrincipal) frag.getActivity()).GetRequest();
        JsonArrayRequest ultimas = new JsonArrayRequest(Request.Method.POST, urlActividades, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                guardarActividades(response, null);
                if (frag instanceof ActividadesFragment)
                    ((ActividadesFragment) frag).recargar();
                else if (frag instanceof TalleresFragment)
                    ((TalleresFragment) frag).recargar();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(ultimas);
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

    public static void SincronizarNoticias(final Context mContext, RequestQueue req, final SyncResult result) {
        JsonArrayRequest ultimas = new JsonArrayRequest(Request.Method.POST, urlNoticias, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                guardarNoticias(response, result);

                Intent intent = new Intent(mContext, PantallaPrincipal.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Common.ABRIR_DONDE, NOTICIAS);

                PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                SharedPreferences preferences = mContext.getSharedPreferences(Common.PREFERENCES, Context.MODE_PRIVATE);
                if (preferences.getBoolean(Common.RECIBIR_NOTICIA, false))
                    Common.sendNotification(mContext, mContext.getString(R.string.titulo_notif_noticias), mContext.getString(R.string.desc_notif_noticias), pendingIntent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        req.add(ultimas);
    }

    public static void SincronizarNoticias(final NoticiasImagenFragment frag) {
        RequestQueue mRequestQueue = ((PantallaPrincipal) frag.getActivity()).GetRequest();
        JsonArrayRequest ultimas = new JsonArrayRequest(Request.Method.POST, urlActividades, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                guardarNoticias(response, null);
                frag.recargar();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(ultimas);
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
