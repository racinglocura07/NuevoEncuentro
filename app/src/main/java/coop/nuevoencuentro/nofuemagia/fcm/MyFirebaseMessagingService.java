package coop.nuevoencuentro.nofuemagia.fcm;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import coop.nuevoencuentro.nofuemagia.R;
import coop.nuevoencuentro.nofuemagia.activities.FullscreenActivity;
import coop.nuevoencuentro.nofuemagia.activities.PantallaPrincipal;
import coop.nuevoencuentro.nofuemagia.helper.Common;
import coop.nuevoencuentro.nofuemagia.sync.SyncUtils;

/**
 * Created by jlionti on 14/06/2016. No Fue Magia
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        System.out.println("Mensaje" + " From: " + remoteMessage.getFrom());

        System.out.println(remoteMessage.getData());
        checkNotification(remoteMessage.getData());
    }

    private void checkNotification(Map<String, String> messageBody) {


        String titulo = messageBody.get("title");
        String msg = messageBody.get("body");
        int idActividad = Integer.parseInt(messageBody.get("idActividad"));

        if (titulo == null || titulo.equals("")) {
            Bundle params = new Bundle();
            params.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
            params.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
            params.putString("QUE", msg);
            ContentResolver.requestSync(SyncUtils.getAccount(this), getString(R.string.provider), params);

            return;
        }

        Intent intent = new Intent(this, PantallaPrincipal.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (idActividad > -1) {
            String imagenUrl = Common.imagenURL + "actividad-" + idActividad + ".jpg";

            Bundle args = new Bundle();
            args.putString(FullscreenActivity.IMAGEN_FULL, imagenUrl);

            intent = new Intent(this, FullscreenActivity.class);
            intent.putExtras(args);
        }
        else if ( idActividad == -2 ){
            intent.putExtra(Common.ABRIR_DONDE, Common.BOLSONES);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        SharedPreferences preferences = getSharedPreferences(Common.PREFERENCES, Context.MODE_PRIVATE);
        if (preferences.getBoolean(Common.RECIBIR_NOTICIA, true))
            Common.sendNotification(this, titulo, msg, pendingIntent);
    }
}
