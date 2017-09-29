package pyk.poi.controller.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import pyk.poi.R;
import pyk.poi.controller.fragment.ListFragment;
import pyk.poi.controller.fragment.SaveFragment;
import pyk.poi.controller.fragment.SearchFragment;
import pyk.poi.view.animator.Animator;

public class MapsActivity extends AppCompatActivity
    implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, LocationListener {
  
  private View popupWindow;
  
  private GoogleMap map;
  private Toolbar   toolbar;
  private ImageView list;
  private ImageView add;
  private ImageView search;
  
  private        boolean windowIsOpen;
  private static int     defaultHeight;
  
  public static LatLng user;
  
  public static final  int STANDARD_CAMERA_SPEED = 400;
  private static final int SLOWER_CAMERA_SPEED   = 700;
  private static final int STANDARD_ZOOM = 17;
  private static final int FAR_ZOOM = 15;
  private static final int NEAR_ZOOM = 18;
  
  private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);
    
    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      checkLocationPermission();
    }
    
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
  }
  
  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.iv_list_button:
        final ListFragment listFragment = new ListFragment();
        replaceFragment(listFragment);
        break;
      case R.id.iv_add_button:
        SaveFragment saveFragment = new SaveFragment();
        replaceFragment(saveFragment);
        break;
      case R.id.iv_search_button:
        SearchFragment searchFragment = new SearchFragment();
        replaceFragment(searchFragment);
        break;
      default:
        break;
    }
  }
  
  @Override
  public void onMapReady(GoogleMap googleMap) {
    map = googleMap;
    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (ActivityCompat.checkSelfPermission(this,
                                             Manifest.permission.ACCESS_FINE_LOCATION) ==
          PackageManager.PERMISSION_GRANTED) {
        map.setMyLocationEnabled(true);
      }
    } else {
      map.setMyLocationEnabled(true);
    }
    
    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    
    Location userLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    if(userLocation == null) {
      userLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
    }
    double   lat          = userLocation.getLatitude();
    double   lng          = userLocation.getLongitude();
    user = new LatLng(lat, lng);
    Animator.centerMapOnPoint(user, STANDARD_CAMERA_SPEED, STANDARD_ZOOM, map);
  }
  
  @Override
  public void onLocationChanged(Location location) {
    double lat = location.getLatitude();
    double lng = location.getLongitude();
    user = new LatLng(lat, lng);
    Animator.centerMapOnPoint(user, STANDARD_CAMERA_SPEED, STANDARD_ZOOM, map);
  }
  
  @Override public void onStatusChanged(String s, int i, Bundle bundle) {
    
  }
  
  @Override public void onProviderEnabled(String s) {
    
  }
  
  @Override public void onProviderDisabled(String s) {
    
  }
  
  private void replaceFragment(final Fragment f) {
    if (windowIsOpen) { // wait to start expanding until window finishes collapsing
      Animator.windowDown(popupWindow);
      Animator.offsetCenterMapOnPoint(user, SLOWER_CAMERA_SPEED, STANDARD_ZOOM, map);
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
      Animator.offsetCenterMapOnPoint(user, STANDARD_CAMERA_SPEED, STANDARD_ZOOM, map);
      getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.popupWindow, f)
          .commit();
      Animator.windowUp(popupWindow, defaultHeight);
    }
    windowIsOpen = true;
  }
  
  @Override public void onMapClick(LatLng latLng) {
    
  }
  
  @Override public boolean onMarkerClick(Marker marker) {
    return false;
  }
  
  // credit to https://stackoverflow.com/questions/36510872/setmylocationenabled-error-on-android-6?answertab=votes#tab-top
  public boolean checkLocationPermission() {
    if (ContextCompat.checkSelfPermission(this,
                                          Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
      
      if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                                                              Manifest.permission.ACCESS_FINE_LOCATION)) {
        ActivityCompat.requestPermissions(this,
                                          new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                          MY_PERMISSIONS_REQUEST_LOCATION);
      } else {
        ActivityCompat.requestPermissions(this,
                                          new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                          MY_PERMISSIONS_REQUEST_LOCATION);
      }
      return false;
    } else {
      return true;
    }
  }
  
  @Override
  // credit to https://stackoverflow.com/questions/36510872/setmylocationenabled-error-on-android-6?answertab=votes#tab-top
  public void onRequestPermissionsResult(int requestCode,
                                         String[] permissions, int[] grantResults) {
    switch (requestCode) {
      case MY_PERMISSIONS_REQUEST_LOCATION: {
        if (grantResults.length > 0
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          if (ActivityCompat.checkSelfPermission(this,
                                                 Manifest.permission.ACCESS_FINE_LOCATION) ==
              PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
          }
        } else {
          Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show();
        }
      }
      
    }
  }
}
