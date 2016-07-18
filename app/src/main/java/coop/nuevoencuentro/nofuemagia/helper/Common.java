package coop.nuevoencuentro.nofuemagia.helper;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import coop.nuevoencuentro.nofuemagia.fragments.ActividadesFragment;
import coop.nuevoencuentro.nofuemagia.fragments.ComprasComunitariasFragment;
import coop.nuevoencuentro.nofuemagia.fragments.TalleresFragment;
import coop.nuevoencuentro.nofuemagia.model.Actividades;
import coop.nuevoencuentro.nofuemagia.model.Bolsones;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.HttpClient;

/**
 * Created by Tano on 16/06/2016.
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

    public static String REGISTRAR_URL = "http://nofuemagia.ueuo.com/Nuevo/backend/usuarios/crearUsuario.php";
    public static String imagenURL = "http://nofuemagia.ueuo.com/Nuevo/imagenes/";

    public static String urlActividades = "http://nofuemagia.ueuo.com/Nuevo/backend/actividades/listActividades.php?fid=-2";
    public static String urlBolsones = "http://nofuemagia.ueuo.com/Nuevo/backend/bolsones/listBolsones.php?fid=-2";

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

    public static void SincronizarBolsones(SyncHttpClient client) {
        client.get(urlBolsones, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                guadarBolsones(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public static void SincronizarBolsones(AsyncHttpClient client, final ComprasComunitariasFragment frag) {
        client.get(urlBolsones, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                guadarBolsones(response);
                frag.recargar();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private static void guadarBolsones(JSONObject response) {
        ActiveAndroid.beginTransaction();
        try {
            Bolsones bolson = new Bolsones();
            bolson.setIdBolson(response.getInt("idBolson"));
            bolson.setLink(response.getString("link"));
            bolson.setCreadoEl(response.getString("creadoEl"));
            bolson.save();

            ActiveAndroid.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } finally {
            System.out.println("Bien bolson");
            ActiveAndroid.endTransaction();
        }
    }

    public static void SincronizarActividades(SyncHttpClient client) {
        client.get(urlActividades, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                guardarActividades(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public static void SincronizarActividades(AsyncHttpClient client, final Fragment frag) {
        client.get(urlActividades, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                guardarActividades(response);
                if (frag instanceof ActividadesFragment)
                    ((ActividadesFragment) frag).recargar();
                else if (frag instanceof TalleresFragment)
                    ((TalleresFragment) frag).recargar();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private static void guardarActividades(JSONArray response) {
        ActiveAndroid.beginTransaction();
        try {

            Gson gson = new Gson();
            Type listType = new TypeToken<List<Actividades>>() {
            }.getType();


            for (Actividades local : Actividades.GetAll(false)) {
                new Delete().from(Actividades.class).where("idActividad = ?", local.getId()).execute();
            }

            List<Actividades> remoto = gson.fromJson(response.toString(), listType);
            for (Actividades carRemoto : remoto) {
                Actividades nueva = new Actividades(carRemoto.idActividad, carRemoto.nombre, carRemoto.descripcion, carRemoto.cuando, carRemoto.repeticion, carRemoto.esTaller);
                nueva.save();
            }

            ActiveAndroid.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } finally {
            System.out.println("Bien actividad");
            ActiveAndroid.endTransaction();
        }
    }
}
