package pyk.poi.controller.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;

import pyk.poi.POIApplication;
import pyk.poi.R;
import pyk.poi.controller.activity.MapsActivity;
import pyk.poi.model.DataSource;
import pyk.poi.model.POIItem;
import pyk.poi.model.geofence.GeofenceHelper;
import pyk.poi.view.animator.Animator;

public class SaveFragment extends Fragment
    implements View.OnClickListener {
  private DataSource dataSource = POIApplication.getSharedDataSource();
  
  View     view;
  EditText name, description;
  ImageView cancel, notification;
  Spinner category;
  Marker  marker;
  boolean isNotify = false;
  
  public SaveFragment() {
    super();
    this.marker = MapsActivity.currentMarker;
  }
  
  public void triggerSave(MapsActivity mapsActivity) {
    String tempName        =
        (name.getText().toString().equals("")) ? "[Blank]" : name.getText().toString();
    String tempDescription =
        (description.getText().toString().equals("")) ? "[Blank]" : description.getText().toString();
    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
    POIItem poiItem = new POIItem(marker.getPosition().latitude, marker.getPosition().longitude,
                                  tempName, category.getSelectedItem().toString(),
                                  tempDescription, false, isNotify);
    dataSource.savePOI(poiItem);
    GeofenceHelper.updateAllFences(MapsActivity.apiClient, MapsActivity.geofenceList,
                                   MapsActivity.pendingIntent, mapsActivity);
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
    cancel = (ImageView) view.findViewById(R.id.iv_cancel_save);
    cancel.setOnClickListener(this);
    notification = (ImageView) view.findViewById(R.id.iv_notification_save);
    notification.setOnClickListener(this);
    
    if (marker.getTitle() != null) {
      name.setText(marker.getTitle());
    }
    
    return view;
  }
  
  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.iv_cancel_save:
        marker.remove();
        Animator.windowDown(MapsActivity.popupWindow);
        MapsActivity.windowIsOpen = false;
        break;
      case R.id.iv_notification_save:
        isNotify = true;
        notification.getDrawable().setTint(
            (isNotify) ? ContextCompat.getColor(view.getContext(), R.color.primary_accent)
                       : ContextCompat.getColor(view.getContext(), R.color.black_54));
        break;
      default:
        break;
    }
  }
}
