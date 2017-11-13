package pyk.poi.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

public class EditTextWithDone extends android.support.v7.widget.AppCompatEditText {
  public EditTextWithDone(Context context) {
    super(context);
  }
  
  public EditTextWithDone(Context context, AttributeSet attrs) {
    super(context, attrs);
  }
  
  public EditTextWithDone(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }
  
  @Override
  public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
    InputConnection conn = super.onCreateInputConnection(outAttrs);
    outAttrs.imeOptions &= ~EditorInfo.IME_FLAG_NO_ENTER_ACTION;
    return conn;
  }
}