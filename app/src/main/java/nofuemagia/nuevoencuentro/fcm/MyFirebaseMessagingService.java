package nofuemagia.nuevoencuentro.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.sql.Date;
import java.util.Map;

import nofuemagia.nuevoencuentro.R;
import nofuemagia.nuevoencuentro.activities.PantallaPrincipal;

/**
 * Created by jlionti on 14/06/2016. No Fue Magia
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        System.out.println("Mensaje" + " From: " + remoteMessage.getFrom());

        sendNotification(remoteMessage.getData());
    }

    private void sendNotification(Map<String, String> messageBody) {
        Intent intent = new Intent(this, PantallaPrincipal.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String titulo = messageBody.get("title");
        String msg = messageBody.get("body");

        if (titulo == "" && msg == "" ){
            titulo = "SyncAdaptaer";
            msg = "SyncAdaptaer";
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notif)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentTitle(titulo)
                .setTicker(titulo)
                .setContentText(msg)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(msg))
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
}
