package com.softhub.vendor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterPackageDetails extends RecyclerView.Adapter<AdapterPackageDetails.PackageViewHolder> {

    private List<FetchPackageDetails> mPackDetails;
    private static Context context;

    public AdapterPackageDetails(List<FetchPackageDetails> mPackDetails, Context context) {
        this.mPackDetails = mPackDetails;
        this.context = context;
    }

    public static class PackageViewHolder extends RecyclerView.ViewHolder{

            private TextView id, packageName, coupon, price, packType;
            private Button renew;

        public PackageViewHolder(@NonNull View itemView) {
            super(itemView);

          //  id = itemView.findViewById(R.id.pack_id);
            packageName = itemView.findViewById(R.id.pack_name);
            coupon = itemView.findViewById(R.id.coupon_count);
            price = itemView.findViewById(R.id.prize);
          //  packType = itemView.findViewById(R.id.pack_mealType);
          //  renew = itemView.findViewById(R.id.renew);
        }
    }

    @NonNull
    @Override
    public PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_pack_layout,parent,false);
        context = parent.getContext();

        return new PackageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PackageViewHolder holder, int position) {
            final FetchPackageDetails listOfPackages = mPackDetails.get(position);

//            holder.id.setText("Package ID: " + listOfPackages.getPack_id());
            holder.packageName.setText("Package Name: " + listOfPackages.getPack_name());
            holder.coupon.setText("Package Coupon: " + listOfPackages.getPack_coupon());
            holder.price.setText("Package Price: " + listOfPackages.getPack_price());
  //          holder.packType.setText("Package Type: " + listOfPackages.getPack_type());

    /*        holder.renew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context,CODRequsetActivity.class);
                    context.startActivity(i);
                }
            });  */
    }

    @Override
    public int getItemCount() {
        return mPackDetails.size();
    }
}
