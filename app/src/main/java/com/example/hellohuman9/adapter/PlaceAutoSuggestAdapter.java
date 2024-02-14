package com.example.hellohuman9.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;

import com.example.hellohuman9.ProfileFragment;
import com.example.hellohuman9.model.PlaceApi;

import java.util.ArrayList;


public class PlaceAutoSuggestAdapter extends ArrayAdapter implements Filterable {

    ArrayList<String> results;
    int resource;
    ProfileFragment context;
    Context context2;
    PlaceApi placeApi = new PlaceApi();

    public PlaceAutoSuggestAdapter(ProfileFragment context,int resId){
        super(context.requireContext(), resId);
        this.context=context;
        this.resource=resId;
    }

    public PlaceAutoSuggestAdapter(Context context2,int resId){
        super(context2, resId);
        this.context2=context2;
        this.resource=resId;
    }

    @Override
    public int getCount(){
        return results.size();
    }

    @Override
    public String getItem(int pos){
        return results.get(pos);
    }

    @NonNull
    @Override
    public Filter getFilter(){
        Filter filter=new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint!=null){
                    results = placeApi.autoComplete(constraint.toString());

                    filterResults.values=results;
                    filterResults.count=results.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results!=null && results.count>0){
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

}
