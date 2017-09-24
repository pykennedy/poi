package pyk.poi.controller.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import pyk.poi.R;


public class ListFragment extends android.support.v4.app.Fragment {
  private View    view;
  private Spinner category;
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_list, container, false);
    
    category = (Spinner) view.findViewById(R.id.spinner_list);
    
    ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.list_categories,
                                                           R.layout.spinner_item);
    adapter.setDropDownViewResource(R.layout.spinner_dropdown);
    category.setAdapter(adapter);
    
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
