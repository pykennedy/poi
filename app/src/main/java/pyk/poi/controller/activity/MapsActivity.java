package pyk.poi.controller.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import pyk.poi.R;
import pyk.poi.controller.fragment.ListFragment;
import pyk.poi.controller.fragment.SaveFragment;
import pyk.poi.controller.fragment.SearchFragment;
import pyk.poi.view.animator.Animator;

public class MapsActivity extends AppCompatActivity
    implements OnMapReadyCallback, View.OnClickListener {
  
  private static View popupWindow;
  
  private GoogleMap map;
  private Toolbar   toolbar;
  private ImageView list;
  private ImageView add;
  private ImageView search;
  
  private        boolean windowIsOpen;
  private static int     defaultHeight;
  
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
    
    LatLng sydney = new LatLng(-34, 151);
    map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
    map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
  }
  
  private void replaceFragment(final Fragment f) {
    if (windowIsOpen) { // wait to start expanding until window finishes collapsing
      Animator.windowDown(popupWindow);
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
      getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.popupWindow, f)
          .commit();
      Animator.windowUp(popupWindow, defaultHeight);
    }
    windowIsOpen = true;
  }
}
