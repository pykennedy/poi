package pyk.poi.controller.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import pyk.poi.R;

public class SaveFragment extends Fragment {
  View    view;
  Spinner category;
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_save, container, false);
    
    category = (Spinner) view.findViewById(R.id.spinner_save);
    category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String catString = category.getItemAtPosition(position).toString();
      }
      
      @Override
      public void onNothingSelected(AdapterView<?> parent) {}
    });
    
    return view;
  }
}
