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

public class AdapterCODApprove extends RecyclerView.Adapter<AdapterCODApprove.CODApproveViewHolder> {

    private List<FetchCODApprove> codApproves;
    private static Context context;

    private Session session;
    private String prefixString, mobileString, tiffinCountString, id;

    public AdapterCODApprove(List<FetchCODApprove> codApproves, Context context) {
        this.codApproves = codApproves;
        this.context = context;

        session = new Session(context);
        prefixString = session.prefs.getString("prefix",null);

    }

    public static class CODApproveViewHolder extends RecyclerView.ViewHolder{

        private TextView sr, name, orderId, mealName, mealTime, mealType, count, price, pending_amount ;
        private Button codDelete, clear_balance;

        public CODApproveViewHolder(@NonNull View itemView) {
            super(itemView);

            sr = itemView.findViewById(R.id.cod_sr);
            name = itemView.findViewById(R.id.cod_name);
            orderId = itemView.findViewById(R.id.cod_orderId);
            mealName = itemView.findViewById(R.id.cod_mealName);
            mealTime = itemView.findViewById(R.id.cod_mealTime);
            mealType = itemView.findViewById(R.id.cod_mealType);
            count = itemView.findViewById(R.id.cod_count);
            price = itemView.findViewById(R.id.cod_price);
            pending_amount = itemView.findViewById(R.id.pending_amount);

            codDelete = itemView.findViewById(R.id.cod_delete);
            clear_balance = itemView.findViewById(R.id.clear_balance);
        }
    }

    @NonNull
    @Override
    public CODApproveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_codapproves_layout,parent,false);
        context = parent.getContext();

        return new CODApproveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CODApproveViewHolder holder, int position) {
        final FetchCODApprove fetchCODApprove = codApproves.get(position);

        holder.sr.setText("Sr No:- " + fetchCODApprove.getId());
        holder.name.setText("Name:- " + fetchCODApprove.getName());
        holder.orderId.setText("Order Id:- " + fetchCODApprove.getOrder_id());
        holder.mealName.setText("Meal Name:- " + fetchCODApprove.getMeal_Name());
        holder.mealTime.setText("Meal Time:- " + fetchCODApprove.getMeal_Time());
        holder.mealType.setText("Meal Type:- " + fetchCODApprove.getMeal_Type());
        holder.count.setText("Count:- " + fetchCODApprove.getCount());
        holder.price.setText("Price:- " + fetchCODApprove.getPrice());
        if(!fetchCODApprove.getPaid_amount().equals("")){
            int x = Integer.parseInt(fetchCODApprove.getPrice())-Integer.parseInt(fetchCODApprove.getPaid_amount());
            holder.pending_amount.setText(""+x);
            if(x>0){
                holder.clear_balance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        id = fetchCODApprove.getId();
                        PayRemainingBalance();
                    }
                });
            }
        }else{
            holder.clear_balance.setVisibility(View.GONE);
        }




        holder.codDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobileString = fetchCODApprove.getName();
                Delete();
            }
        });
    }

    @Override
    public int getItemCount() {
        return codApproves.size();
    }


    private void PayRemainingBalance(){

        //String uri = "https://meradaftar.com/demoVendor/cod_delete.php";

        String uri = context.getString(R.string.server_name) + "cod_pay_remaining_amount.php";
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
                params.put("id",id);
                return params;
            }
        };
        queue.add(stringRequest);
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
