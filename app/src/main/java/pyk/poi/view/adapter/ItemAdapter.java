package pyk.poi.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import pyk.poi.POIApplication;
import pyk.poi.R;
import pyk.poi.controller.activity.MapsActivity;
import pyk.poi.model.DataSource;
import pyk.poi.model.POIItem;
import pyk.poi.view.animator.Animator;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemAdapterViewHolder> {
  DataSource sharedDataSource = POIApplication.getSharedDataSource();
  GoogleMap map;
  
  private List<POIItem> poiItemList = null;
  
  public ItemAdapter(GoogleMap map) {
    poiItemList = sharedDataSource.getPOIList();
    this.map = map;
  }
  
  @Override
  public ItemAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int index) {
    View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.poi_item, viewGroup, false);
    return new ItemAdapterViewHolder(inflate);
  }
  
  @Override
  public void onBindViewHolder(ItemAdapterViewHolder itemAdapterViewHolder, int index) {
    itemAdapterViewHolder.update(poiItemList.get(index));
  }
  
  @Override
  public int getItemCount() {
    return poiItemList.size();
  }
  
  public void setCategory(String category) {
    poiItemList = sharedDataSource.getPOIListByCategory(category);
    this.notifyDataSetChanged();
  }
  
  public void setAll() {
    poiItemList = sharedDataSource.getPOIList();
    this.notifyDataSetChanged();
  }
  
  
  class ItemAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView title;
    POIItem  poiItem;
    
    public ItemAdapterViewHolder(View itemView) {
      super(itemView);
      title = (TextView) itemView.findViewById(R.id.poi_item_name);
      itemView.setOnClickListener(this);
    }
    
    void update(POIItem poiItem) {
      this.poiItem = poiItem;
      title.setText(poiItem.getName());
    }
    
    @Override
    public void onClick(View v) {
      Log.i("", poiItem.getCategory());
      Animator.centerMapOnPoint(new LatLng(poiItem.getLat(), poiItem.getLng()),
                                MapsActivity.STANDARD_CAMERA_SPEED, MapsActivity.STANDARD_ZOOM,
                                map);
      Animator.windowDown(MapsActivity.popupWindow);
      MapsActivity.windowIsOpen = false;
    }
  }
  
}
