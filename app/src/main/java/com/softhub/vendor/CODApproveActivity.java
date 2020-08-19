package com.softhub.vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CODApproveActivity extends AppCompatActivity {

    private ImageView backButton;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<FetchCODApprove> fetchCODApproves;

    private ProgressDialog progressDialog;
    private String prefixString ;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codapprove);
        getSupportActionBar().hide();

        progressDialog = new ProgressDialog(CODApproveActivity.this);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        session = new Session(CODApproveActivity.this);
        prefixString = session.prefs.getString("prefix",null);

        backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        fetchCODApproves = new ArrayList<>();
        adapter = new AdapterCODApprove(fetchCODApproves,this);
        recyclerView.setAdapter(adapter);

        loadCODApprove();

        }

        private void loadCODApprove(){

            //String uri = "https://meradaftar.com/demoVendor/cod_approved_list.php";

            String uri = getResources().getString(R.string.server_name) + "cod_approved_list.php";
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    uri,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                JSONObject JO = new JSONObject(response);
                                String data_code = JO.getString("data_code");

                                if(data_code.equals("200")) {

                                    JSONArray JA = JO.getJSONArray("res");
                                    for(int i = 0; i<JA.length();i++) {
                                        JSONObject JO1 = JA.getJSONObject(i);

                                        FetchCODApprove fetchCOD = new FetchCODApprove(JO1.getString("id"),
                                                JO1.getString("username"),
                                                JO1.getString("order_id"),
                                                JO1.getString("meal_name"),
                                                JO1.getString("meal_time"),
                                                JO1.getString("meal_type"),
                                                JO1.getString("meal_count"),
                                                JO1.getString("price"),
                                                JO1.getString("amount_paid")
                                        );
                                        fetchCODApproves.add(fetchCOD);
                                    }
                                    adapter = new AdapterCODApprove(fetchCODApproves,getApplicationContext());
                                    recyclerView.setAdapter(adapter);
                                    progressDialog.dismiss();
                                }

                                else
                                {
                                    Toast.makeText(CODApproveActivity.this, JO.getString("Message"), Toast.LENGTH_SHORT).show();
                                }


                            }catch (JSONException e){
                                e.printStackTrace();
                                Toast.makeText(CODApproveActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                Log.d("Error-------------",e.toString());
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(CODApproveActivity.this, "Volley error" + error, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            }){
                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<>();
                    params.put("prefix",prefixString);
                    return params;
                }
            };
            queue.add(stringRequest);
        }
}





