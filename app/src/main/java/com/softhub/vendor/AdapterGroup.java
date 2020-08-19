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

public class AdapterGroup extends ArrayAdapter<FetchGroup> {

    public AdapterGroup(Context context, ArrayList<FetchGroup> groupList){
        super(context,0,groupList);
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
                    R.layout.group_spinner_row,parent,false
            );
        }

        TextView textViewId = convertView.findViewById(R.id.text_id);
        TextView textViewGroup = convertView.findViewById(R.id.text_group);

        FetchGroup currentItem = getItem(position);

        if(currentItem != null) {
            textViewId.setText(currentItem.getId());
            textViewGroup.setText(currentItem.getName());
        }

        return convertView;

    }
}
