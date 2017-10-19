package pyk.poi.controller.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pyk.poi.POIApplication;
import pyk.poi.R;
import pyk.poi.controller.activity.MapsActivity;
import pyk.poi.model.POIItem;

public class DetailsFragment extends Fragment {
  View     view;
  TextView name;
  TextView category;
  TextView description;
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_details, container, false);
    name = (TextView) view.findViewById(R.id.tv_name_details);
    category = (TextView) view.findViewById(R.id.tv_category_details);
    description = (TextView) view.findViewById(R.id.tv_description_details);
    
    POIItem poiItem = POIApplication.getSharedDataSource().getPOIItemByMarker(
        MapsActivity.currentMarker);
    
    name.setText(poiItem.getName());
    category.setText(poiItem.getCategory());
    description.setText(poiItem.getNotes());
    
    return view;
  }
}