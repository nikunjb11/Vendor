package com.softhub.vendor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterUserDetails extends RecyclerView.Adapter<AdapterUserDetails.UserViewHolder> {

    private List<FetchUserDetails> mUserDetails;
    private static Context context;


    public AdapterUserDetails(List<FetchUserDetails> mUserDetails, Context context) {
        this.mUserDetails = mUserDetails;
        this.context = context;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView Name, Email, Mobile, uAddress, count;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = (TextView) itemView.findViewById(R.id.cust_name);
            Email = (TextView) itemView.findViewById(R.id.email);
            Mobile = (TextView) itemView.findViewById(R.id.mobile);
            count = (TextView) itemView.findViewById(R.id.count);
            //uAddress = (TextView) itemView.findViewById(R.id.user_address);

        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_user_layout, parent, false);
        context = parent.getContext();

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

        final FetchUserDetails userDetails = mUserDetails.get(position);

        holder.Name.setText(userDetails.getCust_name());
        holder.Email.setText(userDetails.getCust_email());
        holder.Mobile.setText(userDetails.getCust_mob());
        holder.count.setText(""+(position+1));
      //  holder.uAddress.setText(userDetails.getCust_addr());
    }

    @Override
    public int getItemCount() {
        return mUserDetails.size();
    }
}
