package pyk.poi.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pyk.poi.model.database.table.Table;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
  
  private static final String NAME    = "poi_db";
  private static final int    VERSION = 1;
  
  private Table[] tables;
  
  public DatabaseOpenHelper(Context context, Table... tables) {
    super(context, NAME, null, VERSION);
    this.tables = tables;
  }
  
  @Override
  public void onCreate(SQLiteDatabase db) {
    for (Table table : tables) {
      db.execSQL(table.getCreateStatement());
    }
  }
  
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    onCreate(db);
    for (Table table : tables) {
      table.onUpgrade(db, oldVersion, newVersion);
    }
  }
}
