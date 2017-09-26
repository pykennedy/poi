package pyk.poi.view.animator;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

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
}
