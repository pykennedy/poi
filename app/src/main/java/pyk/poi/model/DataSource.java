package pyk.poi.model;

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
  
  public POITable getPoiTable() {
    return poiTable;
  }
}
