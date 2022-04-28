package com.example.trabajoindividual_1;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class Firebase extends FirebaseMessagingService {
    public Firebase(){

    }
    public void onMessageReceived(RemoteMessage remoteMessage){
        /*
        Pre: Un RemoteMessage
        Post: Se ha enviado una notificación
        */

        if (remoteMessage.getNotification() != null){ // Si el mensaje es una notificación
            // Creamos una notificación local
            NotificationManager elManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder elBuilder = new NotificationCompat.Builder(this, "IdCanal");
            NotificationChannel elCanal = new NotificationChannel("IdCanal", "NombreCanal", NotificationManager.IMPORTANCE_DEFAULT);

            elCanal.enableLights(true);
            elCanal.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            elCanal.enableVibration(true);

            elManager.createNotificationChannel(elCanal);
            elBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.baseline_notifications_black_24dp))
                    .setSmallIcon(R.drawable.baseline_notifications_black_24dp)
                    .setContentTitle(getString(R.string.notificacionFCM))
                    .setContentText(getString(R.string.vistopelicula))
                    .setVibrate(new long[]{0, 1000, 500, 1000})
                    .setAutoCancel(true);
            elManager.notify(1, elBuilder.build());

        }
    }
}
