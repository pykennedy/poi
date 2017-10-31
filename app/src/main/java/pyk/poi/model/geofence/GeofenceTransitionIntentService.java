package pyk.poi.model.geofence;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

import pyk.poi.R;
import pyk.poi.controller.activity.MapsActivity;

public class GeofenceTransitionIntentService extends IntentService {
  public GeofenceTransitionIntentService() {
    super("");
  }
  
  @Override
  protected void onHandleIntent(Intent intent) {
    Log.e("-----------------------", "SEND INTENT");
    GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
    if (geofencingEvent.hasError()) {
      Log.e("", "errors and stuff");
      return;
    }
    
    int geofenceTransition = geofencingEvent.getGeofenceTransition();
    if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {
      List   triggeringGeofences       = geofencingEvent.getTriggeringGeofences();
      String geofenceTransitionDetails = "testing123";
      sendNotification(geofenceTransitionDetails);
      Log.i("", geofenceTransitionDetails);
    } else {
      Log.e("", "errors and stuff");
    }
  }
  
  private void sendNotification(String stuff) {
    NotificationManager notificationManager =
        (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
    Intent intent = new Intent(this, MapsActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0, intent,
                                                                        PendingIntent.FLAG_UPDATE_CURRENT);
    
    Notification notification = new NotificationCompat.Builder(this)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle("POI Detected")
        .setContentText("You are near a saved Point of Interest")
        .setContentIntent(pendingNotificationIntent)
        .setStyle(new NotificationCompat.BigTextStyle().bigText(Double.toString(Math.random())))
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .setDefaults(Notification.DEFAULT_SOUND)
        .build();
    
    notificationManager.notify(0, notification);
  }
}