package com.softhub.vendor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterCODRequest extends RecyclerView.Adapter<AdapterCODRequest.CODRequestViewHolder> {

        private List<FetchCODRequest> codRequests;
        private static Context context;

        private Session session;
        private String prefixString, mobileString, tiffinCountString;
        private ProgressDialog progressDialog;
        private String paid_amount;


        public AdapterCODRequest(List<FetchCODRequest> codRequests, Context context) {
                this.codRequests = codRequests;
                this.context = context;

                session = new Session(context);
                prefixString = session.prefs.getString("prefix",null);
                progressDialog = new ProgressDialog(context);

        }

        public static class CODRequestViewHolder extends RecyclerView.ViewHolder{

               private TextView sr, name, order_Id, mobile, mealName, mealType, count, price ;
               private Button codApprove, codDelete;
               private EditText received_amount;

                public CODRequestViewHolder(@NonNull View itemView) {
                        super(itemView);


                        sr = itemView.findViewById(R.id.cod_sr);
                        name = itemView.findViewById(R.id.cod_name);
                        order_Id = itemView.findViewById(R.id.cod_orderId);
                        mobile = itemView.findViewById(R.id.cod_mobile);
                        mealName = itemView.findViewById(R.id.cod_mealName);
                        mealType = itemView.findViewById(R.id.cod_mealType);
                        count = itemView.findViewById(R.id.cod_count);
                        price = itemView.findViewById(R.id.cod_price);
                        received_amount = itemView.findViewById(R.id.received_amount);

                        codApprove = itemView.findViewById(R.id.cod_approve);
                        codDelete = itemView.findViewById(R.id.cod_delete);

                }
        }

        @NonNull
        @Override
        public CODRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_codrequests_layout,parent,false);
                context = parent.getContext();

                return new CODRequestViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final CODRequestViewHolder holder, int position) {
               final FetchCODRequest fetchCODRequest = codRequests.get(position);

                holder.sr.setText("Sr No:" + fetchCODRequest.getId());
                holder.name.setText("Name:" + fetchCODRequest.getName());
                holder.order_Id.setText("Order Id:" + fetchCODRequest.getOrder_id());
                holder.mobile.setText("Mobile No:" + fetchCODRequest.getMobile());
                holder.mealName.setText("Package Name:" + fetchCODRequest.getMeal_Name());
                holder.mealType.setText("Package Type:" + fetchCODRequest.getMeal_Type());
                holder.count.setText("Count:" + fetchCODRequest.getCount());
                holder.price.setText("Price:" + fetchCODRequest.getPrice());

                holder.codApprove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mobileString = fetchCODRequest.getMobile();
                            tiffinCountString = fetchCODRequest.getCount();
                            if(holder.received_amount.getText().toString().equals("")){
                                holder.received_amount.setError("Enter Amount");
                            }else{
                                int i = Integer.parseInt(holder.received_amount.getText().toString());
                                int j = Integer.parseInt(fetchCODRequest.getPrice());
                                if(i>j){
                                    holder.received_amount.setError("Invalid Amount");
                                }else{
                                    paid_amount = holder.received_amount.getText().toString();
                                    approve();
                                }
                            }
                        }
                });

                holder.codDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mobileString = fetchCODRequest.getName();
                            Delete();
                        }
                });

        }

        @Override
        public int getItemCount() {
        return codRequests.size();
        }

        private void approve(){
            progressDialog.setMessage("Loading...");
//            progressDialog.show();

        //    String uri = "https://meradaftar.com/demoVendor/cod_approve.php";

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
                    params.put("paid_amount","1200");
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
