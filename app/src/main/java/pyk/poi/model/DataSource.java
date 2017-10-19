package pyk.poi.model;

import android.database.Cursor;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

import pyk.poi.POIApplication;
import pyk.poi.model.database.DatabaseOpenHelper;
import pyk.poi.model.database.table.POITable;

public class DataSource {
  private DatabaseOpenHelper databaseOpenHelper;
  private POITable           poiTable;
  
  public DataSource() {
    poiTable = new POITable();
    databaseOpenHelper = new DatabaseOpenHelper(POIApplication.getSharedInstance(),
                                                poiTable);
    
    new Thread(new Runnable() {
      @Override
      public void run() {
        if (false) {
          POIApplication.getSharedInstance().deleteDatabase("poi_db");
        }
      }
    }).start();
  }
  
  public POITable getPOITable() {
    return poiTable;
  }
  
  public POIItem getPOIItemByMarker(Marker marker) {
    Cursor cursor = getPOITable().fetchRowByLatLng(databaseOpenHelper.getReadableDatabase(),
                                                   marker.getPosition().latitude,
                                                   marker.getPosition().longitude);
    POIItem poiItem = null;
    if (cursor.moveToFirst()) {
      poiItem = itemFromCursor(cursor);
      cursor.close();
    }
    return poiItem;
  }
  
  public List<POIItem> getPOIList() {
    ArrayList<POIItem> poiItems = new ArrayList<>();
    Cursor
        cursor = getPOITable().fetchAllItems(databaseOpenHelper.getReadableDatabase());
    if (cursor.moveToFirst()) {
      do {
        poiItems.add(itemFromCursor(cursor));
      } while (cursor.moveToNext());
      cursor.close();
    }
    return poiItems;
  }
  
  public List<POIItem> getPOIListByCategory(String category) {
    ArrayList<POIItem> poiItems = new ArrayList<>();
    Cursor cursor = getPOITable().fetchAllPoiByCategory(
        databaseOpenHelper.getReadableDatabase(), category);
    if (cursor.moveToFirst()) {
      do {
        poiItems.add(itemFromCursor(cursor));
      } while (cursor.moveToNext());
      cursor.close();
    }
    return poiItems;
  }
  
  private static POIItem itemFromCursor(Cursor cursor) {
    return new POIItem(Double.parseDouble(POITable.getLatitude(cursor)),
                       Double.parseDouble(POITable.getLongitude(cursor)),
                       POITable.getName(cursor),
                       POITable.getCategory(cursor),
                       POITable.getNotes(cursor),
                       POITable.getViewed(cursor));
  }
  
  public void savePOI(POIItem poiItem) {
    POITable.Builder builder = new POITable.Builder()
        .setLatitude(poiItem.getLat())
        .setLongitude(poiItem.getLng())
        .setName(poiItem.getName())
        .setCategory(poiItem.getCategory())
        .setNotes(poiItem.getNotes())
        .setViewed(poiItem.isViewed());
    builder.insert(databaseOpenHelper.getWritableDatabase());
  }
  
}
