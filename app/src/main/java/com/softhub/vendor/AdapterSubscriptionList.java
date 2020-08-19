package com.softhub.vendor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterSubscriptionList extends RecyclerView.Adapter<AdapterSubscriptionList.SubscriptionListViewHolder> {

    private List<FetchUserSubscription> userSubscriptionsList;
    private static Context context;

    private Session session;
    private String prefixString, mobileString, tiffinCountString;
    private ProgressDialog progressDialog;
    private String status, id;


    public AdapterSubscriptionList(List<FetchUserSubscription> userSubscriptionsList,Context context) {
        this.userSubscriptionsList = userSubscriptionsList;
        this.context = context;

        session = new Session(context);
        prefixString = session.prefs.getString("prefix",null);
        progressDialog = new ProgressDialog(context);
    }

    public static class SubscriptionListViewHolder extends RecyclerView.ViewHolder{

        private TextView sr, name, order_Id, mobile, mealName, mealType, count, price ;
        private Button codApprove, codDelete;

        public SubscriptionListViewHolder(@NonNull View itemView) {
            super(itemView);


            sr = itemView.findViewById(R.id.sub_sr);
            name = itemView.findViewById(R.id.sub_name);
            order_Id = itemView.findViewById(R.id.sub_orderId);
            mobile = itemView.findViewById(R.id.sub_mobile);
            mealName = itemView.findViewById(R.id.sub_mealName);
            mealType = itemView.findViewById(R.id.sub_mealType);
            count = itemView.findViewById(R.id.sub_count);
            price = itemView.findViewById(R.id.sub_price);

            codApprove = itemView.findViewById(R.id.sub_approve);
            codDelete = itemView.findViewById(R.id.sub_delete);

        }
    }


    @NonNull
    @Override
    public SubscriptionListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_usersub_layout,parent,false);
        context = parent.getContext();
        return new AdapterSubscriptionList.SubscriptionListViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull SubscriptionListViewHolder holder, int position) {
        final FetchUserSubscription currentUser = userSubscriptionsList.get(position);

        holder.sr.setText("Sr No:" + currentUser.getId());
        holder.name.setText("Name:" + currentUser.getName());
        holder.order_Id.setText("Order Id:" + currentUser.getOrder_id());
        holder.mobile.setText("Mobile No:" + currentUser.getMobile());
        holder.mealName.setText("Meal Name:" + currentUser.getMeal_Name());
        holder.mealType.setText("Meal Type:" + currentUser.getMeal_Type());
        holder.count.setText("Count:" + currentUser.getCount());
        holder.price.setText("Price:" + currentUser.getPrice());

        if(currentUser.getStatus().equals("Y")){
            holder.codApprove.setText("De-activate");
        }else{
            holder.codApprove.setText("Activate");
        }

        holder.codApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobileString = currentUser.getName();
                tiffinCountString = currentUser.getCount();
                if(currentUser.getStatus().equals("Y")){
                    status = "N";
                }else{
                    status = "Y";
                }
                id= currentUser.getId();
                approve();
            }
        });

        holder.codDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobileString = currentUser.getName();
                Delete();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userSubscriptionsList.size();
    }

    private void approve(){

     //   String uri = "https://meradaftar.com/demoVendor/cod_approve.php";

        String uri = context.getString(R.string.server_name) + "cod_activate.php";
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                uri,
                new Response.Listener<String>() {
                     @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject JO = new JSONObject(response);
                            String data_code = JO.getString("data_code");

                            if(data_code.equals("200")) {
                                Toast.makeText(context, JO.getString("Message"), Toast.LENGTH_SHORT).show();
                                openHome();

                            }

                            else
                            {
                                Toast.makeText(context, JO.getString("Message"), Toast.LENGTH_SHORT).show();
                            }


                        }catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Volley error" + error, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("prefix",prefixString);
                params.put("id",id);
                params.put("status",status);
                return params;
            }
        };
        queue.add(stringRequest);
 }


    private void Delete(){

        String uri = "https://meradaftar.com/demoVendor/cod_delete.php";
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject JO = new JSONObject(response);
                            String data_code = JO.getString("data_code");

                            if(data_code.equals("200")) {
                                Toast.makeText(context, JO.getString("Message"), Toast.LENGTH_SHORT).show();
                                openHome();

                            }

                            else
                            {
                                Toast.makeText(context, JO.getString("Message"), Toast.LENGTH_SHORT).show();
                            }


                        }catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Volley error" + error, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("prefix",prefixString);
                params.put("mobile",mobileString);
                return params;
            }
        };
        queue.add(stringRequest);

    }

    private void openHome(){

        Intent i = new Intent(context,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);
    }
 }
