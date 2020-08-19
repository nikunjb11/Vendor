package com.softhub.vendor;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

    public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.ViewHolder> {

        private List<FetchCategories> listItems;
        private Context context;

        String rupee;



        public AdapterCategory(List<FetchCategories> listItems, Context context) {
            this.listItems = listItems;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_category_name, parent, false);


            context = parent.getContext();
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final FetchCategories listItem = listItems.get(position);

            holder.category_name.setText((position+1)+". "+listItem.getName());

        }

        @Override
        public int getItemCount() {
            return listItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView category_name;

            public ViewHolder(View itemView) {
                super(itemView);

                category_name = (TextView) itemView.findViewById(R.id.category_name);


            }
        }
    }