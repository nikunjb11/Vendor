package com.softhub.vendor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AdapterItem extends ArrayAdapter<FetchItems> {


    public AdapterItem(@NonNull Context context, ArrayList<FetchItems> ItemsList) {
        super(context, 0,ItemsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }


    private View initView(int position, View convertView, ViewGroup parent){

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_spinner_row,parent,false
            );
        }

        TextView textViewId = convertView.findViewById(R.id.text_item_id);
        TextView textViewCat = convertView.findViewById(R.id.text_item_name);

        FetchItems currentItem = getItem(position);

        if(currentItem != null) {
            textViewId.setText(currentItem.getId());
            textViewCat.setText(currentItem.getName());
        }

        return convertView;

    }

}
