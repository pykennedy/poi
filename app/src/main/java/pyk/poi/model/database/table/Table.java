package pyk.poi.model.database.table;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class Table {
  public static interface Builder {
    public long insert(SQLiteDatabase writeableDB);
  }
  
  protected static final String LAT = "lat";
  protected static final String LNG = "lng";
  
  public abstract String getName();
  
  public abstract String getCreateStatement();
  
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
  
  protected static String getString(Cursor cursor, String column) {
    int columnIndex = cursor.getColumnIndex(column);
    if (columnIndex == -1) {
      return "";
    }
    return cursor.getString(columnIndex);
  }
  
  protected static long getLong(Cursor cursor, String column) {
    int columnIndex = cursor.getColumnIndex(column);
    if (columnIndex == -1) {
      return -1l;
    }
    return cursor.getLong(columnIndex);
  }
  
  protected static boolean getBoolean(Cursor cursor, String column) {
    return getLong(cursor, column) == 1l;
  }
}
