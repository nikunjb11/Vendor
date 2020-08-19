package com.softhub.vendor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterGroupList extends RecyclerView.Adapter<AdapterGroupList.GroupListViewHolder> {

    private List<FetchGroupList> fetchGroupList;
    private Context context;
    private String sr;

    public AdapterGroupList(List<FetchGroupList> fetchGroupList, Context context) {
        this.fetchGroupList = fetchGroupList;
        this.context = context;
    }

    public static class GroupListViewHolder extends RecyclerView.ViewHolder{

        private TextView group_id, group_name, group_address, group_preference;
        private Button group_Action;


        public GroupListViewHolder(@NonNull View itemView) {
            super(itemView);

            group_id = itemView.findViewById(R.id.group_id);
            group_name = itemView.findViewById(R.id.group_name);
            group_address = itemView.findViewById(R.id.group_address);
            group_preference = itemView.findViewById(R.id.group_preference);

            group_Action = itemView.findViewById(R.id.group_action);

        }
    }

    @NonNull
    @Override
    public GroupListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_groupaddress_layout,parent,false);
        context = parent.getContext();

        return new GroupListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GroupListViewHolder holder, int position) {

        final FetchGroupList currentGroupList = fetchGroupList.get(position);

      //  holder.group_id.setText(currentGroupList.getGroup_sr());
      //  holder.group_name.setText(currentGroupList.getGroup_name());
        holder.group_address.setText(currentGroupList.getGroup_address());
        holder.group_preference.setText(currentGroupList.getGroup_preference());

      /*  holder.group_Action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sr = currentGroupList.getGroup_sr();
                delete();
            }
        });  */

     }

    @Override
    public int getItemCount() {
        return fetchGroupList.size();
    }

    private void delete(){

    }

}
