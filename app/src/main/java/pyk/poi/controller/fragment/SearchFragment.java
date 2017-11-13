package pyk.poi.controller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import pyk.poi.R;
import pyk.poi.controller.activity.MapsActivity;
import pyk.poi.model.yelp.YelpAPI;
import pyk.poi.view.animator.Animator;

import static pyk.poi.controller.activity.MapsActivity.user;

public class SearchFragment extends Fragment {
  View     view;
  EditText search;
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_search, container, false);
    search = (EditText) view.findViewById(R.id.et_search);
    search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
              try {
                YelpAPI yelpAPI = new YelpAPI();
                yelpAPI.searchYelp(search.getText().toString(), user);
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          });
          
          thread.start();
          InputMethodManager
              imm = (InputMethodManager) getContext().getSystemService(
              Context.INPUT_METHOD_SERVICE);
          imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
          
          Animator.windowDown(MapsActivity.popupWindow);
          MapsActivity.windowIsOpen = false;
          return true;
        }
        return false;
      }
    });
    
    return view;
  }
}