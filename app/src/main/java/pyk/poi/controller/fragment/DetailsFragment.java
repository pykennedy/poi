package pyk.poi.controller.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import pyk.poi.POIApplication;
import pyk.poi.R;
import pyk.poi.controller.activity.MapsActivity;
import pyk.poi.model.POIItem;

public class DetailsFragment extends Fragment
    implements View.OnClickListener {
  View      view;
  TextView  name;
  TextView  category;
  TextView  description;
  ImageView viewed;
  POIItem   poiItem;
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_details, container, false);
    name = (TextView) view.findViewById(R.id.tv_name_details);
    category = (TextView) view.findViewById(R.id.tv_category_details);
    description = (TextView) view.findViewById(R.id.tv_description_details);
    viewed = (ImageView) view.findViewById(R.id.iv_viewed_details);
    viewed.setOnClickListener(this);
    
    poiItem = POIApplication.getSharedDataSource().getPOIItemByMarker(
        MapsActivity.currentMarker);
    
    name.setText(poiItem.getName());
    category.setText(poiItem.getCategory());
    description.setText(poiItem.getNotes());
    viewed.getDrawable().setTint(
        (poiItem.isViewed()) ? ContextCompat.getColor(view.getContext(), R.color.primary_accent)
                             : ContextCompat.getColor(view.getContext(), R.color.black_54));
    return view;
  }
  
  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.iv_viewed_details:
        poiItem.setViewed(!poiItem.isViewed());
        POIApplication.getSharedDataSource().updatePOI(poiItem);
        viewed.getDrawable().setTint(
            (poiItem.isViewed()) ? ContextCompat.getColor(view.getContext(), R.color.primary_accent)
                                 : ContextCompat.getColor(view.getContext(), R.color.black_54));
        break;
      case R.id.iv_notification_details:
        break;
      default:
        break;
    }
  }
}