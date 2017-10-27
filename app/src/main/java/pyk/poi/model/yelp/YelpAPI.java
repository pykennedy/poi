package pyk.poi.model.yelp;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.SearchResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pyk.poi.POIApplication;
import pyk.poi.controller.activity.MapsActivity;
import pyk.poi.model.POIItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YelpAPI {
  private final String APPID     = "W0BX9j7SsrBS6AEfl6snRQ";
  private final String APPSECRET =
      "4ydvPVDr3sNjXLGsr9KVoHoD6pZQv1HKAXTE5okILlX7UTkfdwccqyKlUTysuwVn";
  
  YelpFusionApiFactory apiFactory;
  YelpFusionApi        yelpFusionApi;
  
  public YelpAPI() {
    apiFactory = new YelpFusionApiFactory();
    try {
      yelpFusionApi = apiFactory.createAPI(APPID, APPSECRET);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public void searchYelp(String term, LatLng location) {
    
    
    Map<String, String> params = new HashMap<>();
    params.put("term", term);
    params.put("latitude", Double.toString(location.latitude));
    params.put("longitude", Double.toString(location.longitude));
    
    Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(params);
    Callback<SearchResponse> callback = new Callback<SearchResponse>() {
      @Override
      public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
        List<POIItem>  poiList        = POIApplication.getSharedDataSource().getPOIList();
        SearchResponse searchResponse = response.body();
        for (Business business : searchResponse.getBusinesses()) {
          boolean exists = false;
          for (POIItem poiItem : poiList) {
            if (poiItem.getLat() == business.getCoordinates().getLatitude()
                && poiItem.getLng() == business.getCoordinates().getLongitude()) {
              exists = true;
            }
          }
          if (!exists) {
            POIItem poiItem = new POIItem(business.getCoordinates().getLatitude(),
                                          business.getCoordinates().getLongitude(),
                                          business.getName(),
                                          null, null, false);
            MapsActivity.map.addMarker(
                new MarkerOptions().position(new LatLng(poiItem.getLat(), poiItem.getLng()))
                                   .icon(BitmapDescriptorFactory
                                             .defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                   .title(poiItem.getName()));
          }
        }
      }
      
      @Override
      public void onFailure(Call<SearchResponse> call, Throwable t) {
      }
    };
    
    call.enqueue(callback);
  }
}
