package com.softhub.vendor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

public class AdapterOnlineRequest extends RecyclerView.Adapter<AdapterOnlineRequest.OnlineRequestViewHolder> {

    private List<FetchOnlineRequest> onlineRequests;
    private static Context context;

    private Session session;
    private String prefixString, mobileString, tiffinCountString;
    private ProgressDialog progressDialog;

    public AdapterOnlineRequest(List<FetchOnlineRequest> onlineRequests,Context context) {
        this.onlineRequests = onlineRequests;
        this.context = context;

        session = new Session(context);
        prefixString = session.prefs.getString("prefix",null);
        progressDialog = new ProgressDialog(context);

    }

    public static class OnlineRequestViewHolder extends RecyclerView.ViewHolder{

        private TextView sr, name, order_Id, mobile, mealName, mealType, count, price ;
        private Button codApprove, codDelete;


        public OnlineRequestViewHolder(@NonNull View itemView) {
            super(itemView);

            sr = itemView.findViewById(R.id.cod_sr);
            name = itemView.findViewById(R.id.cod_name);
            order_Id = itemView.findViewById(R.id.cod_orderId);
            mobile = itemView.findViewById(R.id.cod_mobile);
            mealName = itemView.findViewById(R.id.cod_mealName);
            mealType = itemView.findViewById(R.id.cod_mealType);
            count = itemView.findViewById(R.id.cod_count);
            price = itemView.findViewById(R.id.cod_price);

            codApprove = itemView.findViewById(R.id.cod_approve);
            codDelete = itemView.findViewById(R.id.cod_delete);

        }
    }


    @NonNull
    @Override
    public OnlineRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_onlinerequests_layout,parent,false);
        context = parent.getContext();

        return new OnlineRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnlineRequestViewHolder holder, int position) {
        final FetchOnlineRequest fetchOnlineRequest = onlineRequests.get(position);

        holder.sr.setText("Sr No:" + fetchOnlineRequest.getId());
        holder.name.setText("Name:" + fetchOnlineRequest.getName());
        holder.order_Id.setText("Order Id:" + fetchOnlineRequest.getOrder_id());
        holder.mobile.setText("Mobile No:" + fetchOnlineRequest.getMobile());
        holder.mealName.setText("Meal Name:" + fetchOnlineRequest.getMeal_Name());
        holder.mealType.setText("Meal Type:" + fetchOnlineRequest.getMeal_Type());
        holder.count.setText("Count:" + fetchOnlineRequest.getCount());
        holder.price.setText("Price:" + fetchOnlineRequest.getPrice());

        holder.codApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobileString = fetchOnlineRequest.getName();
                tiffinCountString = fetchOnlineRequest.getCount();
                approve();
            }
        });

        holder.codDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobileString = fetchOnlineRequest.getName();
                Delete();
            }
        });

    }

    @Override
    public int getItemCount() {
        return onlineRequests.size();
    }

    private void approve(){

     //   String uri = "https://meradaftar.com/demoVendor/cod_approve.php";

        String uri = context.getString(R.string.server_name) + "cod_approve.php";
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
                params.put("count",tiffinCountString);
                return params;
            }
        };
        queue.add(stringRequest);




    }

    private void Delete(){

       // String uri = "https://meradaftar.com/demoVendor/cod_delete.php";

        String uri = context.getString(R.string.server_name) + "cod_delete.php";
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
