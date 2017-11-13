package pyk.poi.view.animator;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import pyk.poi.controller.activity.MapsActivity;

public class Animator {
  private static long SLIDE_DURATION = 250;
  
  public static void windowUp(final View v, final int targetHeight) {
    v.getLayoutParams().height = 1;
    v.setVisibility(View.VISIBLE);
    Animation a = new Animation() {
      @Override
      protected void applyTransformation(float interpolatedTime, Transformation t) {
        v.getLayoutParams().height = (int) (targetHeight * interpolatedTime);
        v.requestLayout();
      }
      
      @Override
      public boolean willChangeBounds() {
        return true;
      }
    };
    a.setDuration(SLIDE_DURATION);
    a.setInterpolator(new LinearInterpolator());
    v.startAnimation(a);
  }
  
  public static void windowDown(final View v) {
    if (v.getVisibility() == View.GONE) {
      return;
    }
    
    final int initialHeight = v.getMeasuredHeight();
    
    Animation a = new Animation() {
      @Override
      protected void applyTransformation(float interpolatedTime, Transformation t) {
        if (interpolatedTime == 1) {
          v.setVisibility(View.GONE);
        } else {
          v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
          v.requestLayout();
        }
      }
      
      @Override
      public boolean willChangeBounds() {
        return true;
      }
    };
    a.setDuration(SLIDE_DURATION);
    a.setInterpolator(new LinearInterpolator());
    v.startAnimation(a);
  }
  
  public static void centerMapOnPoint(LatLng location, int speed, int zoom, GoogleMap map) {
    map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, zoom), speed,
                      new GoogleMap.CancelableCallback() {
                        @Override
                        public void onFinish() {
                        }
      
                        @Override
                        public void onCancel() {
                        }
                      });
  }
  
  public static void offsetCenterMapOnPoint(LatLng location, int speed, int zoom, GoogleMap map) {
    double offset = (location == MapsActivity.user) ? 0.0014 : 0.0012;
    LatLng temp   = new LatLng(location.latitude - offset, location.longitude);
    map.animateCamera(CameraUpdateFactory.newLatLngZoom(temp, zoom), speed,
                      new GoogleMap.CancelableCallback() {
                        @Override
                        public void onFinish() {}
      
                        @Override
                        public void onCancel() {}
                      });
  }
}
