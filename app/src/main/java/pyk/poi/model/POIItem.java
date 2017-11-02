package pyk.poi.model;

public class POIItem {
  private String  name;
  private String  category;
  private String  notes;
  private double  lat;
  private double  lng;
  private boolean viewed;
  private boolean notify;
  
  public POIItem(double lat, double lng, String name, String category, String notes,
                 boolean viewed, boolean notify) {
    this.lat = lat;
    this.lng = lng;
    this.name = name;
    this.category = category;
    this.notes = notes;
    this.viewed = viewed;
    this.notify = notify;
  }
  
  public double getLat() {
    return lat;
  }
  
  public double getLng() {
    return lng;
  }
  
  public String getName() {
    return name;
  }
  
  public String getCategory() {
    return category;
  }
  
  public String getNotes() {
    return notes;
  }
  
  public boolean isViewed() {
    return viewed;
  }
  
  public boolean isNotify() {
    return notify;
  }
  
  public void setLat(double lat) {
    this.lat = lat;
  }
  
  public void setLng(double lng) {
    this.lng = lng;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setCategory(String category) {
    this.category = category;
  }
  
  public void setNotes(String notes) {
    this.notes = notes;
  }
  
  public void setViewed(boolean isViewed) {
    this.viewed = isViewed;
  }
  
  public void setNotify(boolean notify) { this.notify = notify; }
}
