package pyk.poi.controller.activity;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import pyk.poi.R;
import pyk.poi.controller.fragment.ListFragment;
import pyk.poi.controller.fragment.SaveFragment;
import pyk.poi.controller.fragment.SearchFragment;
import pyk.poi.view.animator.Animator;

public class MapsActivity extends AppCompatActivity
    implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {
  
  private View popupWindow;
  
  private GoogleMap map;
  private Toolbar   toolbar;
  private ImageView list;
  private ImageView add;
  private ImageView search;
  
  private        boolean windowIsOpen;
  private static int     defaultHeight;
  private        boolean intentToAdd;
  
  public static LatLng           user;
  public static Marker           currentMarker;
  private       LocationCallback locationCallback;
  FusedLocationProviderClient fusedLocationProviderClient;
  boolean accessGranted = false;
  
  private SaveFragment saveFragment;
  
  public static final  int STANDARD_CAMERA_SPEED = 400;
  private static final int SLOWER_CAMERA_SPEED   = 700;
  private static final int STANDARD_ZOOM         = 17;
  private static final int FAR_ZOOM              = 15;
  private static final int NEAR_ZOOM             = 18;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);
    
    popupWindow = findViewById(R.id.popupWindow);
    
    toolbar = (Toolbar) findViewById(R.id.tb_maps_activity);
    setSupportActionBar(toolbar);
    
    list = (ImageView) findViewById(R.id.iv_list_button);
    list.setOnClickListener(this);
    add = (ImageView) findViewById(R.id.iv_add_button);
    add.setOnClickListener(this);
    search = (ImageView) findViewById(R.id.iv_search_button);
    search.setOnClickListener(this);
    
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
    
    ViewTreeObserver viewTreeObserver = popupWindow.getViewTreeObserver();
    if (viewTreeObserver.isAlive()) {
      viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
          if (defaultHeight == 0) {
            defaultHeight = popupWindow.getHeight();
            popupWindow.setVisibility(View.GONE);
            windowIsOpen = false;
          }
        }
      });
    }
    
    setupPermissions();
  }
  
  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.iv_list_button:
        toggleAdd(false);
        final ListFragment listFragment = new ListFragment();
        replaceFragment(listFragment, user);
        break;
      case R.id.iv_add_button:
        if (intentToAdd) {
          LatLng loc = (currentMarker != null) ? currentMarker.getPosition() : user;
          Animator.centerMapOnPoint(loc, STANDARD_CAMERA_SPEED, STANDARD_ZOOM, map);
          if (windowIsOpen) {
            saveFragment.triggerSave();
          }
        }
        Animator.windowDown(popupWindow);
        windowIsOpen = false;
        toggleAdd(true);
        break;
      case R.id.iv_search_button:
        toggleAdd(false);
        SearchFragment searchFragment = new SearchFragment();
        replaceFragment(searchFragment, user);
        break;
      default:
        break;
    }
  }
  
  @Override
  public void onMapReady(GoogleMap googleMap) {
    map = googleMap;
    map.setOnMapClickListener(this);
    if (ActivityCompat.checkSelfPermission(this,
                                           Manifest.permission.ACCESS_FINE_LOCATION) ==
        PackageManager.PERMISSION_GRANTED) {
      setupLocation(map);
    }
    
  }
  
  private void replaceFragment(final Fragment f, LatLng latLng) {
    if (windowIsOpen) { // wait to start expanding until window finishes collapsing
      Animator.windowDown(popupWindow);
      Animator.offsetCenterMapOnPoint(latLng, SLOWER_CAMERA_SPEED, STANDARD_ZOOM, map);
      Handler handler = new Handler();
      handler.postDelayed(new Runnable() {
        public void run() {
          getSupportFragmentManager()
              .beginTransaction()
              .replace(R.id.popupWindow, f)
              .commit();
          Animator.windowUp(popupWindow, defaultHeight);
        }
      }, 350);
    } else {
      Animator.offsetCenterMapOnPoint(latLng, STANDARD_CAMERA_SPEED, STANDARD_ZOOM, map);
      getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.popupWindow, f)
          .commit();
      Animator.windowUp(popupWindow, defaultHeight);
    }
    windowIsOpen = true;
  }
  
  private void toggleAdd(boolean initFromAdd) {
    // TODO: add code to remove unsaved markers
    intentToAdd = (initFromAdd) && !intentToAdd;
    add.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.add));
    add.getDrawable().setTint((intentToAdd) ? ContextCompat.getColor(this, R.color.white_primary)
                                            : ContextCompat
                                  .getColor(this, R.color.white_primary_87));
    if (intentToAdd) {
      Toast.makeText(this, "Click on the map to add a POI", Toast.LENGTH_SHORT).show();
    }
  }
  
  private void setupPermissions() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
        PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        0x1);
    } else {
      accessGranted = true;
    }
    
  }
  
  @Override
  public void onRequestPermissionsResult(int requestCode,
                                         String permissions[], int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == 0x1) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        Toast.makeText(this, "Access Granted", Toast.LENGTH_SHORT).show();
        setupLocation(map);
      } else {
        Toast.makeText(this, "Access Denied", Toast.LENGTH_LONG).show();
      }
    }
  }
  
  private void setupLocation(final GoogleMap map) {
    map.setMyLocationEnabled(true);
    LocationRequest locationRequest = new LocationRequest();
    locationRequest.setInterval(10000);
    locationRequest.setFastestInterval(5000);
    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    
    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
        .addLocationRequest(locationRequest);
    SettingsClient                 client = LocationServices.getSettingsClient(this);
    Task<LocationSettingsResponse> task   = client.checkLocationSettings(builder.build());
    
    task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
      @Override
      public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
        // All location settings are satisfied. The client can initialize
        // location requests here.
        // ...
      }
    });
    
    task.addOnFailureListener(this, new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        int statusCode = ((ApiException) e).getStatusCode();
        switch (statusCode) {
          case CommonStatusCodes.RESOLUTION_REQUIRED:
            // Location settings are not satisfied, but this can be fixed
            // by showing the user a dialog.
            try {
              // Show the dialog by calling startResolutionForResult(),
              // and check the result in onActivityResult().
              ResolvableApiException resolvable = (ResolvableApiException) e;
              resolvable.startResolutionForResult(MapsActivity.this, 0x1);
            } catch (IntentSender.SendIntentException sendEx) {
              // Ignore the error.
            }
            break;
          case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
            // Location settings are not satisfied. However, we have no way
            // to fix the settings so we won't show the dialog.
            break;
        }
      }
    });
    
    
    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                                                                                Manifest.permission.ACCESS_COARSE_LOCATION) !=
                                             PackageManager.PERMISSION_GRANTED) {
      // TODO: Consider calling
      //    ActivityCompat#requestPermissions
      // here to request the missing permissions, and then overriding
      //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
      //                                          int[] grantResults)
      // to handle the case where the user grants the permission. See the documentation
      // for ActivityCompat#requestPermissions for more details.
      return;
    }
    fusedLocationProviderClient.getLastLocation()
                               .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                 @Override
                                 public void onSuccess(Location location) {
                                   if (location != null) {
                                     user = new LatLng(location.getLatitude(),
                                                       location.getLongitude());
                                     Animator.centerMapOnPoint(user, SLOWER_CAMERA_SPEED,
                                                               STANDARD_ZOOM, map);
                                   }
                                 }
                               });
    locationCallback = new LocationCallback() {
      @Override
      public void onLocationResult(LocationResult locationResult) {
        for (Location location : locationResult.getLocations()) {
          user = new LatLng(location.getLatitude(),
                            location.getLongitude());
        }
      }
    };
    
    fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                                                       locationCallback,
                                                       null /* Looper */);
  }
  
  @Override public void onMapClick(LatLng latLng) {
    if (intentToAdd) {
      currentMarker = map.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .icon(BitmapDescriptorFactory
                                                  .defaultMarker(
                                                      BitmapDescriptorFactory.HUE_YELLOW)));
      add.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.done));
      add.getDrawable().setTint(ContextCompat.getColor(this, R.color.white_primary));
      saveFragment = new SaveFragment();
      replaceFragment(saveFragment, currentMarker.getPosition());
    }
  }
  
  @Override public boolean onMarkerClick(Marker marker) {
    return false;
  }
  
}
