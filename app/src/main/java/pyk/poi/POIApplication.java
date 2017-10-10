package pyk.poi;

import android.app.Application;

import pyk.poi.model.DataSource;

public class POIApplication extends Application {
  
  private static POIApplication sharedInstance;
  private        DataSource     dataSource;
  
  public static POIApplication getSharedInstance() {
    return sharedInstance;
  }
  
  public static DataSource getSharedDataSource() {
    return POIApplication.getSharedInstance().getDataSource();
  }
  
  @Override
  public void onCreate() {
    super.onCreate();
    sharedInstance = this;
    dataSource = new DataSource();
  }
  
  public DataSource getDataSource() {
    return dataSource;
  }
}
