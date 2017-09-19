package pyk.poi.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import pyk.poi.R;

public class MapsActivity extends AppCompatActivity
    implements OnMapReadyCallback, View.OnClickListener {
  
  private GoogleMap map;
  private Toolbar   toolbar;
  private ImageView list;
  private ImageView add;
  private ImageView search;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);
    
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
  }
  
  @Override
  public void onClick(View v) {
    switch(v.getId()) {
      case R.id.iv_list_button:
        Toast.makeText(this, "List", Toast.LENGTH_SHORT).show();
        break;
      case R.id.iv_add_button:
        Toast.makeText(this, "Add", Toast.LENGTH_SHORT).show();
        break;
      case R.id.iv_search_button:
        Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
        break;
      default:
        break;
    }
  }
  
  
  /**
   * Manipulates the map once available.
   * This callback is triggered when the map is ready to be used.
   * This is where we can add markers or lines, add listeners or move the camera. In this case,
   * we just add a marker near Sydney, Australia.
   * If Google Play services is not installed on the device, the user will be prompted to install
   * it inside the SupportMapFragment. This method will only be triggered once the user has
   * installed Google Play services and returned to the app.
   */
  @Override
  public void onMapReady(GoogleMap googleMap) {
    map = googleMap;
    
    // Add a marker in Sydney and move the camera
    LatLng sydney = new LatLng(-34, 151);
    map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
    map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
  }
}
