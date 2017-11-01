package pyk.poi.model.geofence;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import pyk.poi.POIApplication;
import pyk.poi.controller.activity.MapsActivity;
import pyk.poi.model.DataSource;
import pyk.poi.model.POIItem;

public class GeofenceHelper {
  
  public static void updateAllFences(GoogleApiClient apiClient, List<Geofence> geofenceList,
                                     PendingIntent pendingIntent, MapsActivity mapsActivity) {
    if (ActivityCompat.checkSelfPermission(POIApplication.getContext(),
                                           Manifest.permission.ACCESS_FINE_LOCATION) !=
        PackageManager.PERMISSION_GRANTED) {
      Log.e("-----------------------", "permission eror");
      return;
    }
    
    DataSource    dataSource  = POIApplication.getSharedDataSource();
    List<POIItem> poiItemList = dataSource.getPOIList();
    
    // TODO: check for notify is true before adding to list, if not true then remove from list
    if (poiItemList != null && !poiItemList.isEmpty()) {
      for (int i = 0; i < poiItemList.size(); i++) {
        GeofenceHelper.addFence(geofenceList, poiItemList.get(i));
      }
      LocationServices.GeofencingApi.addGeofences(apiClient,
                                                  GeofenceHelper
                                                      .getGeofencingRequest(geofenceList),
                                                  GeofenceHelper
                                                      .getGeofencePendingIntent(mapsActivity,
                                                                                pendingIntent))
                                    .setResultCallback(mapsActivity);
    }
  }
  
  public static void addFence(List<Geofence> geofenceList, POIItem poiItem) {
    geofenceList.add(new Geofence.Builder()
                         .setRequestId("" + poiItem.getLat() + poiItem.getLng())
                         .setCircularRegion(poiItem.getLat(), poiItem.getLat(), 100)
                         .setExpirationDuration(Geofence.NEVER_EXPIRE)
                         .setLoiteringDelay(5000)
                         .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL)
                         .build());
  }
  
  public static GeofencingRequest getGeofencingRequest(List<Geofence> geofenceList) {
    return new GeofencingRequest.Builder()
        .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL)
        .addGeofences(geofenceList)
        .build();
  }
  
  public static PendingIntent getGeofencePendingIntent(MapsActivity mapsActivity,
                                                       PendingIntent pendingIntent) {
    if (pendingIntent != null) {
      return pendingIntent;
    }
    Intent intent = new Intent(mapsActivity, GeofenceTransitionIntentService.class);
    //POIApplication.getContext().startService(intent);
    return PendingIntent.getService(POIApplication.getContext(), 0, intent, PendingIntent.
        FLAG_UPDATE_CURRENT);
  }
}