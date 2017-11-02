package pyk.poi.controller.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;

import pyk.poi.POIApplication;
import pyk.poi.R;
import pyk.poi.controller.activity.MapsActivity;
import pyk.poi.model.DataSource;
import pyk.poi.model.POIItem;
import pyk.poi.model.geofence.GeofenceHelper;

public class SaveFragment extends Fragment {
  private DataSource dataSource = POIApplication.getSharedDataSource();
  
  View     view;
  EditText name, description;
  Spinner category;
  Marker  marker;
  
  public SaveFragment() {
    super();
    this.marker = MapsActivity.currentMarker;
  }
  
  public void triggerSave(MapsActivity mapsActivity) {
    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
    POIItem poiItem = new POIItem(marker.getPosition().latitude, marker.getPosition().longitude,
                                  name.getText().toString(), category.getSelectedItem().toString(),
                                  description.getText().toString(), false, false);
    dataSource.savePOI(poiItem);
    GeofenceHelper.updateAllFences(MapsActivity.apiClient, MapsActivity.geofenceList, MapsActivity.pendingIntent, mapsActivity);
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_save, container, false);
    name = (EditText) view.findViewById(R.id.et_name_save);
    description = (EditText) view.findViewById(R.id.et_description_save);
    category = (Spinner) view.findViewById(R.id.spinner_save);
    category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String catString = category.getItemAtPosition(position).toString();
      }
      
      @Override
      public void onNothingSelected(AdapterView<?> parent) {}
    });
    ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.list_categories,
                                                           R.layout.spinner_item);
    adapter.setDropDownViewResource(R.layout.spinner_dropdown);
    category.setAdapter(adapter);
    
    if(marker.getTitle() != null) {
      name.setText(marker.getTitle());
    }
    
    return view;
  }
}
