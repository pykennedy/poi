package pyk.poi.utility;

import android.content.Context;
import android.util.DisplayMetrics;

public class Converter {
  public int pxToDp(Context context, int px) {
    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
    int dp = Math.round(
        px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    return dp;
  }
  
  public int dpToPx(Context context, int dp) {
    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
    int px = Math.round(
        dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    return px;
  }
}
