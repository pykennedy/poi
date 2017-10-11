package pyk.poi.controller.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;

import pyk.poi.POIApplication;
import pyk.poi.R;
import pyk.poi.controller.activity.MapsActivity;
import pyk.poi.model.DataSource;
import pyk.poi.model.POIItem;

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
  
  public void triggerSave() {
    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
    POIItem poiItem = new POIItem(name.getText().toString(), category.getSelectedItem().toString(),
                                  description.getText().toString(), marker.getPosition().latitude,
                                  marker.getPosition().longitude, false);
    dataSource.savePOI(poiItem);
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
    
    return view;
  }
}
