package com.softhub.vendor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterPackageItems extends RecyclerView.Adapter<AdapterPackageItems.ViewHolderPackageItem> {

        private List<FetchPackageItems> mPackItems;
        private static Context context;


    public AdapterPackageItems(List<FetchPackageItems> mPackItems,Context context) {
        this.mPackItems = mPackItems;
        this.context = context;
    }

    public static class ViewHolderPackageItem extends RecyclerView.ViewHolder{

        TextView pSr, pName, pQuntity;


        public ViewHolderPackageItem(@NonNull View itemView) {
            super(itemView);

            pSr = itemView.findViewById(R.id.pItem_sr);
            pName = itemView.findViewById(R.id.pItemName);
            pQuntity = itemView.findViewById(R.id.pItemQuentity);

        }
    }

    @NonNull
    @Override
    public ViewHolderPackageItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pack_list_layout,parent,false);
        context = parent.getContext();

        return new ViewHolderPackageItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPackageItem holder, int position) {

        final FetchPackageItems packageItems = mPackItems.get(position);

        holder.pSr.setText(""+(position+1));
        holder.pName.setText(packageItems.getPackItem_name());
        holder.pQuntity.setText(packageItems.getPackItem_quntity());
    }

    @Override
    public int getItemCount() {
        return mPackItems.size();
    }
}
