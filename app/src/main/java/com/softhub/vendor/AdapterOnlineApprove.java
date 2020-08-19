package com.softhub.vendor;

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

public class AdapterOnlineApprove extends RecyclerView.Adapter<AdapterOnlineApprove.OnlineApproveAdapter> {

    private List<FetchOnlineApprove> onlineApproves;
    private static Context context;

    private Session session;
    private String prefixString, mobileString, tiffinCountString;

    public AdapterOnlineApprove(List<FetchOnlineApprove> onlineApproves, Context context) {
        this.onlineApproves = onlineApproves;
        this.context = context;

        session = new Session(context);
        prefixString = session.prefs.getString("prefix",null);

    }

    public static class OnlineApproveAdapter extends RecyclerView.ViewHolder{
        private TextView sr, name, orderId, mealName, mealTime, mealType, count, price ;
        private Button codDelete;

        public OnlineApproveAdapter(@NonNull View itemView) {
            super(itemView);

            sr = itemView.findViewById(R.id.cod_sr);
            name = itemView.findViewById(R.id.cod_name);
            orderId = itemView.findViewById(R.id.cod_orderId);
            mealName = itemView.findViewById(R.id.cod_mealName);
            mealTime = itemView.findViewById(R.id.cod_mealTime);
            mealType = itemView.findViewById(R.id.cod_mealType);
            count = itemView.findViewById(R.id.cod_count);
            price = itemView.findViewById(R.id.cod_price);

            codDelete = itemView.findViewById(R.id.cod_delete);

        }
    }

    @NonNull
    @Override
    public OnlineApproveAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_onlineapproves_layout,parent,false);
        context = parent.getContext();

        return new AdapterOnlineApprove.OnlineApproveAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnlineApproveAdapter holder, int position) {

        final FetchOnlineApprove fetchOnlineApprove = onlineApproves.get(position);

        holder.sr.setText("Sr No:- " + fetchOnlineApprove.getId());
        holder.name.setText("Name:- " + fetchOnlineApprove.getName());
        holder.orderId.setText("Order Id:- " + fetchOnlineApprove.getOrder_id());
        holder.mealName.setText("Meal Name:- " + fetchOnlineApprove.getMeal_Name());
        holder.mealTime.setText("Meal Time:- " + fetchOnlineApprove.getMeal_Time());
        holder.mealType.setText("Meal Type:- " + fetchOnlineApprove.getMeal_Type());
        holder.count.setText("Count:- " + fetchOnlineApprove.getCount());
        holder.price.setText("Price:- " + fetchOnlineApprove.getPrice());

        holder.codDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobileString = fetchOnlineApprove.getName();
                Delete();
            }
        });

    }

        @Override
        public int getItemCount() {
        return onlineApproves.size();
        }

        private void Delete(){

        //String uri = "https://meradaftar.com/demoVendor/cod_delete.php";

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
                //    progressDialog.dismiss();

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



